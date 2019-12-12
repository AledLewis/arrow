package arrow.fx

import arrow.core.Left
import arrow.core.Right
import arrow.core.internal.AtomicBooleanW
import arrow.fx.internal.BIOFiber
import arrow.fx.internal.BIOForkedStart
import arrow.fx.internal.BIOResult
import arrow.fx.internal.Platform
import arrow.fx.internal.UnsafePromise
import arrow.fx.typeclasses.Fiber
import kotlin.coroutines.CoroutineContext

/** Mix-in to enable `parMapN` 2-arity on IO's companion directly. */
interface IORacePair {

  /**
   * Race two tasks concurrently within a new [IO].
   * Race results in a winner and the other, yet to finish task running in a [Fiber].
   *
   * ```kotlin:ank:playground
   * import arrow.fx.*
   * import arrow.fx.extensions.fx
   * import kotlinx.coroutines.Dispatchers
   *
   * fun main(args: Array<String>) {
   *     //sampleStart
   *     val result = IO.fx {
   *       val racePair = !IO.racePair(Dispatchers.Default, never<Int>(), just("Hello World!"))
   *       racePair.fold(
   *         { _, _ -> "never cannot win race" },
   *         { _, winner -> winner }
   *       )
   *   }
   *   //sampleEnd
   *
   *   val r = result.unsafeRunSync()
   *   println("Race winner result is: $r")
   * }
   * ```
   *
   * @param ctx [CoroutineContext] to execute the source [IO] on.
   * @param ioA task to participate in the race
   * @param ioB task to participate in the race
   * @return [IO] either [Left] with product of the winner's result [ioA] and still running task [ioB],
   *   or [Right] with product of running task [ioA] and the winner's result [ioB].
   *
   * @see [arrow.fx.typeclasses.Concurrent.raceN] for a simpler version that cancels loser.
   */
  fun <E, A, B> racePair(ctx: CoroutineContext, ioA: BIOOf<E, A>, ioB: BIOOf<E, B>): BIO<E, RacePair<BIOPartialOf<E>, A, B>> =
    BIO.Async { conn, cb ->
      val active = AtomicBooleanW(true)

      val upstreamCancelToken = IO.defer { if (conn.isCanceled()) IO.unit else conn.cancel() }

      // Cancelable connection for the left value
      val connA = IOConnection()
      connA.push(upstreamCancelToken)
      val promiseA = UnsafePromise<E, A>()

      // Cancelable connection for the right value
      val connB = IOConnection()
      connB.push(upstreamCancelToken)
      val promiseB = UnsafePromise<E, B>()

      conn.pushPair(connA, connB)

      IORunLoop.startCancelable(BIOForkedStart(ioA, ctx), connA) { either: BIOResult<E, A> ->
        either.fold({ error ->
          if (active.getAndSet(false)) { // if an error finishes first, stop the race.
            connB.cancel().fix().unsafeRunAsync { r2 ->
              conn.pop()
              cb(BIOResult.Error(r2.fold({ Platform.composeErrors(error, it) }, { error }, { error })))
            }
          } else {
            promiseA.complete(BIOResult.Error(error))
          }
        }, { e ->
          if (active.getAndSet(false)) { // if an error finishes first, stop the race.
            connB.cancel().fix().unsafeRunAsync { r2 ->
              conn.pop()
              // TODO asyncErrorHandler r2
              cb(BIOResult.Left(e))
            }
          } else {
            promiseA.complete(BIOResult.Left(e))
          }
        }, { a ->
          if (active.getAndSet(false)) {
            conn.pop()
            cb(BIOResult.Right(RacePair.First(a, BIOFiber(promiseB, connB))))
          } else {
            promiseA.complete(BIOResult.Right(a))
          }
        })
      }

      IORunLoop.startCancelable(BIOForkedStart(ioB, ctx), connB) { either: BIOResult<E, B> ->
        either.fold({ error ->
          if (active.getAndSet(false)) { // if an error finishes first, stop the race.
            connA.cancel().fix().unsafeRunAsync { r2 ->
              conn.pop()
              cb(BIOResult.Error(r2.fold({ Platform.composeErrors(error, it) }, { error }, { error })))
            }
          } else {
            promiseB.complete(BIOResult.Error(error))
          }
        }, { e ->
          if (active.getAndSet(false)) { // if an error finishes first, stop the race.
            connB.cancel().fix().unsafeRunAsync { r2 ->
              conn.pop()
              // TODO asyncErrorHandler r2
              cb(BIOResult.Left(e))
            }
          } else {
            promiseB.complete(BIOResult.Left(e))
          }
        }, { b ->
          if (active.getAndSet(false)) {
            conn.pop()
            cb(BIOResult.Right(RacePair.Second(BIOFiber(promiseA, connA), b)))
          } else {
            promiseB.complete(BIOResult.Right(b))
          }
        })
      }
    }
}
