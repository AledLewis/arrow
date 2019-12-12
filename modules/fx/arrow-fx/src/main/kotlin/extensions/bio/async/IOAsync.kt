package arrow.fx.extensions.bio.async

import arrow.Kind
import arrow.core.Either
import arrow.fx.BIO.Companion
import arrow.fx.BIOPartialOf
import arrow.fx.extensions.IOAsync
import arrow.fx.typeclasses.AsyncSyntax
import kotlin.Function0
import kotlin.PublishedApi
import kotlin.Suppress
import kotlin.Throwable
import kotlin.Unit
import kotlin.coroutines.CoroutineContext
import kotlin.jvm.JvmName

/**
 * cached extension
 */
@PublishedApi()
internal val async_singleton: IOAsync = object : arrow.fx.extensions.IOAsync {}

/**
 *  Delay a computation on provided [CoroutineContext].
 *
 *  @param ctx [CoroutineContext] to run evaluation on.
 *
 *  ```kotlin:ank:playground
 *  import arrow.fx.*
 * import arrow.fx.extensions.bio.async.*
 * import arrow.core.*
 *
 *
 *  import kotlinx.coroutines.Dispatchers
 *
 *  fun main(args: Array<String>) {
 *   //sampleStart
 *   fun <F> Async<F>.invokeOnDefaultDispatcher(): Kind<F, String> =
 *     later<String, String, String>(Dispatchers.Default, { Thread.currentThread().name })
 *
 *   val result = BIO.async<String>().invokeOnDefaultDispatcher()
 *   //sampleEnd
 *   println(result)
 *  }
 *  ```
 */
@JvmName("later")
@Suppress(
  "UNCHECKED_CAST",
  "USELESS_CAST",
  "EXTENSION_SHADOWED_BY_MEMBER",
  "UNUSED_PARAMETER"
)
fun <A> later(ctx: CoroutineContext, f: Function0<A>): Kind<BIOPartialOf<Nothing>, A> = arrow.fx.BIO
  .async()
  .later<A>(ctx, f) as arrow.Kind<BIOPartialOf<Nothing>, A>

/**
 *  Delay a computation on provided [CoroutineContext].
 *
 *  @param ctx [CoroutineContext] to run evaluation on.
 *
 *  ```kotlin:ank:playground
 *  import arrow.fx.*
 * import arrow.fx.extensions.bio.async.*
 * import arrow.core.*
 *
 *
 *  import kotlinx.coroutines.Dispatchers
 *
 *  fun main(args: Array<String>) {
 *   //sampleStart
 *   fun <F> Async<F>.invokeOnDefaultDispatcher(): Kind<F, String> =
 *     defer<String, String,
 * String>(Dispatchers.Default, { effect { Thread.currentThread().name } })
 *
 *   val result = BIO.async<String>().invokeOnDefaultDispatcher().fix().unsafeRunSync()
 *   //sampleEnd
 *   println(result)
 *  }
 *  ```
 */
@JvmName("defer")
@Suppress(
  "UNCHECKED_CAST",
  "USELESS_CAST",
  "EXTENSION_SHADOWED_BY_MEMBER",
  "UNUSED_PARAMETER"
)
fun <A> defer(ctx: CoroutineContext, f: Function0<Kind<BIOPartialOf<Nothing>, A>>): Kind<BIOPartialOf<Nothing>, A> = arrow.fx.BIO
  .async()
  .defer<A>(ctx, f) as arrow.Kind<BIOPartialOf<Nothing>, A>

/**
 *  Delay a computation on provided [CoroutineContext].
 *
 *  @param ctx [CoroutineContext] to run evaluation on.
 */
@JvmName("laterOrRaise")
@Suppress(
  "UNCHECKED_CAST",
  "USELESS_CAST",
  "EXTENSION_SHADOWED_BY_MEMBER",
  "UNUSED_PARAMETER"
)
fun <A> laterOrRaise(ctx: CoroutineContext, f: Function0<Either<Throwable, A>>): Kind<BIOPartialOf<Nothing>, A> =
  arrow.fx.BIO
    .async()
    .laterOrRaise<A>(ctx, f) as arrow.Kind<BIOPartialOf<Nothing>, A>

/**
 *  Shift evaluation to provided [CoroutineContext].
 *
 *  @param ctx [CoroutineContext] to run evaluation on.
 *
 *  ```kotlin:ank:playground
 *  import arrow.fx.*
 *  import arrow.fx.extensions.bio.async.async
 *  import kotlinx.coroutines.Dispatchers
 *
 *  fun main(args: Array<String>) {
 *   //sampleStart
 *   IO.async().run {
 *     val result = fx.monad {
 *       continueOn(Dispatchers.Default)
 *       Thread.currentThread().name
 *     }.fix().unsafeRunSync()
 *
 *     println(result)
 *   }
 *   //sampleEnd
 *  }
 *  ```
 */
@JvmName("continueOn")
@Suppress(
  "UNCHECKED_CAST",
  "USELESS_CAST",
  "EXTENSION_SHADOWED_BY_MEMBER",
  "UNUSED_PARAMETER"
)
suspend fun AsyncSyntax<BIOPartialOf<Nothing>>.continueOn(ctx: CoroutineContext): Unit = arrow.fx.BIO.async().run {
  this@continueOn.continueOn(ctx) as kotlin.Unit
}

/**
 *  Shift evaluation to provided [CoroutineContext].
 *
 *  @receiver [CoroutineContext] to run evaluation on.
 *
 *  ```kotlin:ank:playground
 *  import arrow.fx.*
 * import arrow.fx.extensions.bio.async.*
 * import arrow.core.*
 *
 *
 *  import kotlinx.coroutines.Dispatchers
 *
 *  fun main(args: Array<String>) {
 *   //sampleStart
 *   BIO.async<String>().run {
 *     val result = Dispatchers.Default.shift<String, String>().map {
 *       Thread.currentThread().name
 *     }
 *
 *     println(result)
 *   }
 *   //sampleEnd
 *  }
 *  ```
 */
@JvmName("shift")
@Suppress(
  "UNCHECKED_CAST",
  "USELESS_CAST",
  "EXTENSION_SHADOWED_BY_MEMBER",
  "UNUSED_PARAMETER"
)
fun CoroutineContext.shift(): Kind<BIOPartialOf<Nothing>, Unit> = arrow.fx.BIO.async().run {
  this@shift.shift() as arrow.Kind<BIOPartialOf<Nothing>, kotlin.Unit>
}

/**
 *  Task that never finishes evaluating.
 *
 *  ```kotlin:ank:playground
 *  import arrow.fx.*
 * import arrow.fx.extensions.bio.async.*
 * import arrow.core.*
 *
 *
 *
 *  fun main(args: Array<String>) {
 *   //sampleStart
 *   val i = BIO.async<String>().never<Int>()
 *
 *   println(i)
 *   //sampleEnd
 *  }
 *  ```
 */
@JvmName("never")
@Suppress(
  "UNCHECKED_CAST",
  "USELESS_CAST",
  "EXTENSION_SHADOWED_BY_MEMBER",
  "UNUSED_PARAMETER"
)
fun <A> never(): Kind<BIOPartialOf<Nothing>, A> = arrow.fx.BIO
  .async()
  .never<A>() as arrow.Kind<BIOPartialOf<Nothing>, A>

/**
 *  Helper function that provides an easy way to construct a suspend effect
 *
 *  ```kotlin:ank:playground
 *  import arrow.fx.*
 * import arrow.fx.extensions.bio.async.*
 * import arrow.core.*
 *
 *
 *  import kotlinx.coroutines.Dispatchers
 *
 *  fun main(args: Array<String>) {
 *   //sampleStart
 *   suspend fun logAndIncrease(s: String): Int {
 *      println(s)
 *      return s.toInt() + 1
 *   }
 *
 *   val result = BIO.async<String>().effect(Dispatchers.Default) { Thread.currentThread().name }.effectMap { s: String -> logAndIncrease(s) }
 *   //sampleEnd
 *   println(result)
 *  }
 *  ```
 */
@JvmName("effectMap")
@Suppress(
  "UNCHECKED_CAST",
  "USELESS_CAST",
  "EXTENSION_SHADOWED_BY_MEMBER",
  "UNUSED_PARAMETER"
)
fun <A, B> Kind<BIOPartialOf<Nothing>, A>.effectMap(f: suspend (A) -> B): Kind<BIOPartialOf<Nothing>, B> = arrow.fx.BIO.async().run {
  this@effectMap.effectMap<A, B>(f) as arrow.Kind<BIOPartialOf<Nothing>, B>
}

/**
 *  ank_macro_hierarchy(arrow.fx.typeclasses.Async)
 *
 *  [Async] models how a data type runs an asynchronous computation that may fail.
 *  Defined by the [Proc] signature, which is the consumption of a callback.
 */
@Suppress(
  "UNCHECKED_CAST",
  "NOTHING_TO_INLINE"
)
inline fun Companion.async(): IOAsync = async_singleton
