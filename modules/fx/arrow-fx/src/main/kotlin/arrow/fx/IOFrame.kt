package arrow.fx

import arrow.core.Either
import arrow.fx.IO.Pure

/**
 * An [IOFrame] knows how to [recover] from a [Throwable] and how to map a value [A] to [B].
 *
 * Internal to `IO`'s implementations, used to specify
 * error handlers in their respective `Bind` internal states.
 *
 * To use an [IOFrame] you must use [IO.Bind] instead of `flatMap` or the [IOFrame]
 * is **not guaranteed** to execute.
 *
 * It's used to implement [attempt], [handleErrorWith] and [arrow.fx.internal.IOBracket]
 */
internal interface IOFrame<in E, in A, out B> : (A) -> B {
  override operator fun invoke(a: A): B
  fun recover(e: Throwable): B
  fun handleError(e: E): B

  companion object {

    internal class Redeem<E, A, B>(val ft: (Throwable) -> B, val fe: (E) -> B, val fb: (A) -> B) : IOFrame<E, A, BIO<Nothing, B>> {
      override fun invoke(a: A): BIO<Nothing, B> = Pure(fb(a))
      override fun recover(e: Throwable): BIO<Nothing, B> = Pure(ft(e))
      override fun handleError(e: E): BIO<Nothing, B> = Pure(fe(e))
    }

    internal class RedeemWith<E, A, E2, B>(val ft: (Throwable) -> BIOOf<E2, B>, val fe: (E) -> BIOOf<E2, B>, val fb: (A) -> BIOOf<E2, B>) : IOFrame<E, A, BIO<E2, B>> {
      override fun invoke(a: A): BIO<E2, B> = fb(a).fix()
      override fun recover(e: Throwable): BIO<E2, B> = ft(e).fix()
      override fun handleError(e: E): BIO<E2, B> = fe(e).fix()
    }

    internal class ErrorHandler<E, A, E2>(val ft: (Throwable) -> BIOOf<E2, A>, val fe: (E) -> BIOOf<E2, A>) : IOFrame<E, A, BIO<E2, A>> {
      override fun invoke(a: A): BIO<E2, A> = Pure(a)
      override fun recover(e: Throwable): BIO<E2, A> = ft(e).fix()
      override fun handleError(e: E): BIO<E2, A> = fe(e).fix()
    }

    @Suppress("UNCHECKED_CAST")
    fun <E, A> attemptBIO(): (A) -> BIO<Throwable, Either<E, A>> = AttemptBIO as (A) -> BIO<Throwable, Either<E, A>>
    fun <A> attemptIO(): (A) -> IO<Either<Throwable, A>> = AttemptIO as (A) -> IO<Either<Throwable, A>>

    private object AttemptBIO : IOFrame<Any?, Any?, BIO<Throwable, Either<Any?, Any?>>> {
      override operator fun invoke(a: Any?): BIO<Throwable, Either<Any?, Any?>> = Pure(Either.Right(a))
      override fun recover(e: Throwable): BIO<Throwable, Either<Any?, Any?>> = TODO()
      override fun handleError(e: Any?): BIO<Throwable, Either<Any?, Any?>> = Pure(Either.Left(e))
    }

    private object AttemptIO : IOFrame<Any?, Any?, BIO<Nothing, Either<Throwable, Any?>>> {
      override operator fun invoke(a: Any?): BIO<Nothing, Either<Throwable, Any?>> = Pure(Either.Right(a))
      override fun recover(e: Throwable): BIO<Nothing, Either<Throwable, Any?>> = Pure(Either.Left(e))
      override fun handleError(e: Any?): BIO<Nothing, Either<Throwable, Any?>> = TODO()
    }
  }
}
