package arrow.fx

import arrow.core.Either
import arrow.core.identity

/**
 * Redeem an [IO] to an [IO] of [B] by resolving the error **or** mapping the value [A] to [B].
 *
 * ```kotlin:ank:playground
 * import arrow.fx.IO
 *
 * fun main(args: Array<String>) {
 *   val result =
 *   //sampleStart
 *   IO.raiseError<Int>(RuntimeException("Hello from Error"))
 *     .redeem({ e -> e.message ?: "" }, Int::toString)
 *   //sampleEnd
 *   println(result.unsafeRunSync())
 * }
 * ```
 */
fun <A, B> IOOf<A>.redeem(ft: (Throwable) -> B, fb: (A) -> B): IO<B> =
  BIO.Bind(fix(), IOFrame.Companion.Redeem<Nothing, A, B>(ft, ::identity, fb))

/**
 * Redeem an [IO] to an [IO] of [B] by resolving the error **or** mapping the value [A] to [B] **with** an effect.
 *
 * ```kotlin:ank:playground
 * import arrow.fx.IO
 *
 * fun main(args: Array<String>) {
 *   val result =
 *   //sampleStart
 *   IO.just("1")
 *     .redeemWith({ e -> IO.just(-1) }, { str -> IO { str.toInt() } })
 *   //sampleEnd
 *   println(result.unsafeRunSync())
 * }
 * ```
 */
fun <A, B> IOOf<A>.redeemWith(fe: (Throwable) -> IOOf<B>, fb: (A) -> IOOf<B>): IO<B> =
  BIO.Bind(fix(), IOFrame.Companion.RedeemWith<Nothing, A, Nothing, B>(fe, ::identity, fb))

/**
 * Safely attempts the [IO] and lift any errors to the value side into [Either].
 *
 * ```kotlin:ank:playground
 * import arrow.fx.IO
 *
 * fun main(args: Array<String>) {
 *   //sampleStart
 *   val resultA = IO.raiseError<Int>(RuntimeException("Boom!")).attempt()
 *   val resultB = IO.just("Hello").attempt()
 *   //sampleEnd
 *   println("resultA: ${resultA.unsafeRunSync()}, resultB: ${resultB.unsafeRunSync()}")
 * }
 * ```
 *
 * @see flatMap if you need to act on the output of the original [IO].
 */
fun <A> IOOf<A>.attempt(): IO<Either<Throwable, A>> =
  BIO.Bind(fix(), IOFrame.attemptIO())

/**
 * Handle the error by mapping the error to a value of [A].
 *
 * ```kotlin:ank:playground
 * import arrow.fx.IO
 * import arrow.fx.handleError
 *
 * fun main(args: Array<String>) {
 *   //sampleStart
 *   val result = IO.raiseError<Int>(RuntimeException("Boom"))
 *     .handleError { e -> "Goodbye World! after $e" }
 *   //sampleEnd
 *   println(result.unsafeRunSync())
 * }
 * ```
 *
 * @see handleErrorWith for a version that can resolve the error using an effect
 */
fun <A> IOOf<A>.handleError(f: (Throwable) -> A): IO<A> =
  handleErrorWith { e -> BIO.Pure(f(e)) }

/**
 * Handle the error by resolving the error with an effect that results in [A].
 *
 * ```kotlin:ank:playground
 * import arrow.fx.IO
 * import arrow.fx.handleErrorWith
 * import arrow.fx.typeclasses.milliseconds
 *
 * fun main(args: Array<String>) {
 *   fun getMessage(e: Throwable): IO<String> = IO.sleep(250.milliseconds)
 *     .followedBy(IO.effect { "Delayed goodbye World! after $e" })
 *
 *   //sampleStart
 *   val result = IO.raiseError<Int>(RuntimeException("Boom"))
 *     .handleErrorWith { e -> getMessage(e) }
 *   //sampleEnd
 *   println(result.unsafeRunSync())
 * }
 * ```
 *
 * @see handleErrorWith for a version that can resolve the error using an effect
 */
fun <A> IOOf<A>.handleErrorWith(f: (Throwable) -> IOOf<A>): IO<A> =
  BIO.Bind(fix(), IOFrame.Companion.ErrorHandler<Nothing, A, Nothing>(f, ::identity))
