package arrow.fx.internal

import arrow.core.Either
import arrow.core.nonFatalOrThrow
import arrow.fx.BIO
import arrow.fx.BIOOf
import arrow.fx.ForIO
import arrow.fx.IO
import arrow.fx.IOConnection
import arrow.fx.IOFrame
import arrow.fx.IOOf
import arrow.fx.IORunLoop
import arrow.fx.fix
import arrow.fx.flatMap
import arrow.fx.typeclasses.CancelToken
import arrow.fx.typeclasses.ExitCase
import kotlinx.atomicfu.atomic

// class AsyncSyntax<E, A>(private val callback: (BIOResult<E, A>) -> Unit) {
//  fun success(a: A): Unit = callback(BIOResult.Right(a))
//  fun fail(e: E): Unit = callback(BIOResult.Left(e))
//  fun error(t: Throwable): Unit = callback(BIOResult.Error(t))
// }
sealed class BIOResult<out E, out A> {
  data class Right<A>(val a: A) : BIOResult<Nothing, A>()
  data class Left<E>(val e: E) : BIOResult<E, Nothing>()
  data class Error(val exception: Throwable) : BIOResult<Nothing, Nothing>()

  fun <R> fold(ifException: (Throwable) -> R, ifLeft: (E) -> R, ifRight: (A) -> R): R =
    when (this) {
      is Right -> ifRight(this.a)
      is Left -> ifLeft(this.e)
      is Error -> ifException(this.exception)
    }
}

fun <A> BIOResult<Nothing, A>.toEither(): Either<Throwable, A> =
  when (this) {
    is BIOResult.Right -> arrow.core.Right(this.a)
    is BIOResult.Left -> TODO()
    is BIOResult.Error -> Either.Left(this.exception)
  }

internal object BIOBracket {

  /**
   * Implementation for `IO.bracketCase`.
   */
  operator fun <E, E2 : E, A, B> invoke(acquire: BIOOf<E, A>, release: (A, ExitCase<E>) -> IOOf<Unit>, use: (A) -> BIOOf<E2, B>): BIO<E2, B> =
    BIO.Async { conn, cb ->
      // Placeholder for the future finalizer
      val deferredRelease = ForwardCancelable()
      conn.push(deferredRelease.cancel())

      // Race-condition check, avoiding starting the bracket if the connection
      // was cancelled already, to ensure that `cancel` really blocks if we
      // start `acquire` — n.b. `isCanceled` is visible here due to `push`
      if (!conn.isCanceled()) {
        // Note `acquire` is uncancelable due to usage of `IORunLoop.start`
        // (in other words it is disconnected from our IOConnection)
        IORunLoop.start(acquire, BracketStart<E, E2, A, B>(use, release, conn, deferredRelease, cb))
      } else {
        deferredRelease.complete(IO.unit)
      }
    }

  // Internals of `IO.bracketCase`.
  private class BracketStart<E, E2 : E, A, B>(
    val use: (A) -> BIOOf<E2, B>,
    val release: (A, ExitCase<E>) -> IOOf<Unit>,
    val conn: IOConnection,
    val deferredRelease: ForwardCancelable,
    val cb: (BIOResult<E2, B>) -> Unit
  ) : (BIOResult<E, A>) -> Unit {

    // This runnable is a dirty optimization to avoid some memory allocations;
    // This class switches from being a Callback to a Runnable, but relies on the internal IO callback protocol to be
    // respected (called at most once).
    private var result: Either<Throwable, A>? = null

    override fun invoke(ea: BIOResult<E, A>) {
      // Introducing a light async boundary, otherwise executing the required
      // logic directly will yield a StackOverflowException
      Platform.trampoline {
        when (ea) {
          is BIOResult.Right -> {
            val a = ea.a
            val frame = BracketReleaseFrame<E2, A, B>(a, release)
            val onNext = {
              val fb = try {
                use(a)
              } catch (e: Throwable) {
                IO.raiseException<B>(e.nonFatalOrThrow())
              }.fix()

              BIO.Bind(fb.fix(), frame)
            }

            // Registering our cancelable token ensures that in case cancellation is detected, release gets called
            deferredRelease.complete(frame.cancel)
            // Actual execution
            IORunLoop.startCancelable<E2, B>(onNext(), conn, cb)
          }
          is BIOResult.Left -> cb(ea as BIOResult<E2, B>)
          is BIOResult.Error -> cb(ea)
        }
      }
    }
  }

  fun <E, A> guaranteeCase(source: BIO<E, A>, release: (ExitCase<E>) -> IOOf<Unit>): BIO<E, A> =
    BIO.Async { conn, cb ->
      Platform.trampoline {
        val frame = EnsureReleaseFrame<E, A>(release)
        val onNext = source.flatMap(frame)
        // Registering our cancelable token ensures that in case
        // cancellation is detected, `release` gets called
        conn.push(frame.cancel)

        // Race condition check, avoiding starting `source` in case
        // the connection was already cancelled — n.b. we don't need
        // to trigger `release` otherwise, because it already happened
        if (!conn.isCanceled()) IORunLoop.startCancelable(onNext, conn, cb)
      }
    }

  private class BracketReleaseFrame<E, A, B>(val a: A, val releaseFn: (A, ExitCase<E>) -> IOOf<Unit>) :
    BaseReleaseFrame<E, A, B>() {

    override fun release(c: ExitCase<E>): CancelToken<ForIO> =
      releaseFn(a, c)
  }

  private class EnsureReleaseFrame<E, A>(val releaseFn: (ExitCase<E>) -> IOOf<Unit>) : BaseReleaseFrame<E, Unit, A>() {

    override fun release(c: ExitCase<E>): CancelToken<ForIO> = releaseFn(c)
  }

  private abstract class BaseReleaseFrame<E, A, B> : IOFrame<E, B, BIO<E, B>> {

    // Guard used for thread-safety, to ensure the idempotency
    // of the release; otherwise `release` can be called twice
    private val waitsForResult = atomic(true)

    abstract fun release(c: ExitCase<E>): CancelToken<ForIO>

    private fun applyRelease(e: ExitCase<E>): IO<Unit> =
      IO.defer {
        if (waitsForResult.compareAndSet(true, false))
          release(e)
        else
          IO.unit
      }

    val cancel: CancelToken<ForIO> = applyRelease(ExitCase.Canceled).fix().uncancelable()

    // Unregistering cancel token, otherwise we can have a memory leak;
    // N.B. conn.pop() happens after the evaluation of `release`, because
    // otherwise we might have a conflict with the auto-cancellation logic
    override fun recover(e: Throwable): IO<B> =
      BIO.ContextSwitch(applyRelease(ExitCase.exception(e)), BIO.ContextSwitch.makeUncancelable, disableUncancelableAndPop)
        .flatMap(ReleaseRecoverException(e))

    override operator fun invoke(a: B): IO<B> =
    // Unregistering cancel token, otherwise we can have a memory leak
    // N.B. conn.pop() happens after the evaluation of `release`, because
      // otherwise we might have a conflict with the auto-cancellation logic
      BIO.ContextSwitch(applyRelease(ExitCase.Completed), BIO.ContextSwitch.makeUncancelable, disableUncancelableAndPop)
        .map { a }

    override fun handleError(e: E): BIO<E, B> =
      BIO.ContextSwitch(applyRelease(ExitCase.error(e)), BIO.ContextSwitch.makeUncancelable, disableUncancelableAndPop)
        .flatMap(ReleaseRecoverError(e))
  }

  private class ReleaseRecoverException(val error: Throwable) : IOFrame<Any?, Unit, IO<Nothing>> {

    override fun recover(e: Throwable): IO<Nothing> =
      IO.raiseException(Platform.composeErrors(error, e))

    override fun invoke(a: Unit): IO<Nothing> = IO.raiseException(error)

    override fun handleError(e: Any?): IO<Nothing> = IO.raiseException(error)
  }

  private class ReleaseRecoverError<E>(val error: E) : IOFrame<E, Unit, BIO<E, Nothing>> {

    override fun handleError(e: E): BIO<E, Nothing> =
      BIO.RaiseError(error)

    override fun recover(e: Throwable): BIO<E, Nothing> =
      BIO.RaiseError(error)

    override fun invoke(a: Unit): BIO<E, Nothing> =
      BIO.RaiseError(error)
  }

  private val disableUncancelableAndPop: (Any?, Any?, Throwable?, IOConnection, IOConnection) -> IOConnection =
    { _, _, _, old, _ ->
      old.pop()
      old
    }
}
