package arrow.fx.extensions.bio.functor

import arrow.Kind
import arrow.core.Tuple2
import arrow.fx.BIO
import arrow.fx.BIO.Companion
import arrow.fx.BIOPartialOf
import arrow.fx.extensions.IOFunctor
import kotlin.Function1
import kotlin.PublishedApi
import kotlin.Suppress
import kotlin.Unit
import kotlin.jvm.JvmName

/**
 * cached extension
 */
@PublishedApi()
internal val functor_singleton: IOFunctor = object : arrow.fx.extensions.IOFunctor {}

fun <A, B> BIO<Nothing, A>.map(arg1: Function1<A, B>): BIO<Nothing, B> =
  arrow.fx.BIO.functor().run {
    this@map.map<A, B>(arg1) as BIO<Nothing, B>
  }

@JvmName("imap")
@Suppress(
  "UNCHECKED_CAST",
  "USELESS_CAST",
  "EXTENSION_SHADOWED_BY_MEMBER",
  "UNUSED_PARAMETER"
)
fun <A, B> Kind<BIOPartialOf<Nothing>, A>.imap(arg1: Function1<A, B>, arg2: Function1<B, A>): Kind<BIOPartialOf<Nothing>, B> =
  arrow.fx.BIO.functor().run {
    this@imap.imap<A, B>(arg1, arg2) as arrow.Kind<BIOPartialOf<Nothing>, B>
  }

/**
 *  Lifts a function `A -> B` to the [F] structure returning a polymorphic function
 *  that can be applied over all [F] values in the shape of Kind<F, A>
 *
 *  `A -> B -> Kind<F, A> -> Kind<F, B>`
 *
 *  ```kotlin:ank:playground
 *  import arrow.fx.*
 * import arrow.fx.extensions.bio.functor.*
 * import arrow.core.*
 *
 *
 *  import arrow.fx.extensions.bio.applicative.just
 *
 *  fun main(args: Array<String>) {
 *   val result =
 *   //sampleStart
 *   lift<String, String, String>({ s: CharSequence -> "$s World" })("Hello".just<String, String>())
 *   //sampleEnd
 *   println(result)
 *  }
 *  ```
 */
@JvmName("lift")
@Suppress(
  "UNCHECKED_CAST",
  "USELESS_CAST",
  "EXTENSION_SHADOWED_BY_MEMBER",
  "UNUSED_PARAMETER"
)
fun <A, B> lift(arg0: Function1<A, B>): Function1<Kind<BIOPartialOf<Nothing>, A>, Kind<BIOPartialOf<Nothing>, B>> = arrow.fx.BIO
  .functor()
  .lift<A, B>(arg0) as kotlin.Function1<arrow.Kind<BIOPartialOf<Nothing>, A>, arrow.Kind<BIOPartialOf<Nothing>, B>>

/**
 *  Discards the [A] value inside [F] signaling this container may be pointing to a noop
 *  or an effect whose return value is deliberately ignored. The singleton value [Unit] serves as signal.
 *
 *  Kind<F, A> -> Kind<F, Unit>
 *
 *  ```kotlin:ank:playground
 *  import arrow.fx.*
 * import arrow.fx.extensions.bio.functor.*
 * import arrow.core.*
 *
 *
 *  import arrow.fx.extensions.bio.applicative.just
 *
 *  fun main(args: Array<String>) {
 *   val result =
 *   //sampleStart
 *   "Hello World".just<String, String>().unit<String, String>()
 *   //sampleEnd
 *   println(result)
 *  }
 *  ```
 */
@JvmName("unit")
@Suppress(
  "UNCHECKED_CAST",
  "USELESS_CAST",
  "EXTENSION_SHADOWED_BY_MEMBER",
  "UNUSED_PARAMETER"
)
fun <A> Kind<BIOPartialOf<Nothing>, A>.unit(): Kind<BIOPartialOf<Nothing>, Unit> = arrow.fx.BIO.functor().run {
  this@unit.unit<A>() as arrow.Kind<BIOPartialOf<Nothing>, kotlin.Unit>
}

/**
 *  Applies [f] to an [A] inside [F] and returns the [F] structure with a tuple of the [A] value and the
 *  computed [B] value as result of applying [f]
 *
 *  Kind<F, A> -> Kind<F, Tuple2<A, B>>
 *
 *  ```kotlin:ank:playground
 *  import arrow.fx.*
 * import arrow.fx.extensions.bio.functor.*
 * import arrow.core.*
 *
 *
 *  import arrow.fx.extensions.bio.applicative.just
 *
 *  fun main(args: Array<String>) {
 *   val result =
 *   //sampleStart
 *   "Hello".just<String, String>().fproduct<String, String, String>({ "$it World" })
 *   //sampleEnd
 *   println(result)
 *  }
 *  ```
 */
@JvmName("fproduct")
@Suppress(
  "UNCHECKED_CAST",
  "USELESS_CAST",
  "EXTENSION_SHADOWED_BY_MEMBER",
  "UNUSED_PARAMETER"
)
fun <A, B> Kind<BIOPartialOf<Nothing>, A>.fproduct(arg1: Function1<A, B>): Kind<BIOPartialOf<Nothing>, Tuple2<A, B>> =
  arrow.fx.BIO.functor().run {
    this@fproduct.fproduct<A, B>(arg1) as arrow.Kind<BIOPartialOf<Nothing>, arrow.core.Tuple2<A, B>>
  }

/**
 *  Replaces [A] inside [F] with [B] resulting in a Kind<F, B>
 *
 *  Kind<F, A> -> Kind<F, B>
 *
 *  ```kotlin:ank:playground
 *  import arrow.fx.*
 * import arrow.fx.extensions.bio.functor.*
 * import arrow.core.*
 *
 *
 *  import arrow.fx.extensions.bio.applicative.just
 *
 *  fun main(args: Array<String>) {
 *   val result =
 *   //sampleStart
 *   "Hello World".just<String, String>().`as`<String, String, String>("...")
 *   //sampleEnd
 *   println(result)
 *  }
 *  ```
 */
@JvmName("as")
@Suppress(
  "UNCHECKED_CAST",
  "USELESS_CAST",
  "EXTENSION_SHADOWED_BY_MEMBER",
  "UNUSED_PARAMETER"
)
fun <A, B> Kind<BIOPartialOf<Nothing>, A>.`as`(arg1: B): Kind<BIOPartialOf<Nothing>, B> = arrow.fx.BIO.functor().run {
  this@`as`.`as`<A, B>(arg1) as arrow.Kind<BIOPartialOf<Nothing>, B>
}

/**
 *  Pairs [B] with [A] returning a Kind<F, Tuple2<B, A>>
 *
 *  Kind<F, A> -> Kind<F, Tuple2<B, A>>
 *
 *  ```kotlin:ank:playground
 *  import arrow.fx.*
 * import arrow.fx.extensions.bio.functor.*
 * import arrow.core.*
 *
 *
 *  import arrow.fx.extensions.bio.applicative.just
 *
 *  fun main(args: Array<String>) {
 *   val result =
 *   //sampleStart
 *   "Hello".just<String, String>().tupleLeft<String, String, String>("World")
 *   //sampleEnd
 *   println(result)
 *  }
 *  ```
 */
@JvmName("tupleLeft")
@Suppress(
  "UNCHECKED_CAST",
  "USELESS_CAST",
  "EXTENSION_SHADOWED_BY_MEMBER",
  "UNUSED_PARAMETER"
)
fun <A, B> Kind<BIOPartialOf<Nothing>, A>.tupleLeft(arg1: B): Kind<BIOPartialOf<Nothing>, Tuple2<B, A>> = arrow.fx.BIO.functor().run {
  this@tupleLeft.tupleLeft<A, B>(arg1) as arrow.Kind<BIOPartialOf<Nothing>, arrow.core.Tuple2<B, A>>
}

/**
 *  Pairs [A] with [B] returning a Kind<F, Tuple2<A, B>>
 *
 *  Kind<F, A> -> Kind<F, Tuple2<A, B>>
 *
 *  ```kotlin:ank:playground
 *  import arrow.fx.*
 * import arrow.fx.extensions.bio.functor.*
 * import arrow.core.*
 *
 *
 *  import arrow.fx.extensions.bio.applicative.just
 *
 *  fun main(args: Array<String>) {
 *   val result =
 *   //sampleStart
 *   "Hello".just<String, String>().tupleRight<String, String, String>("World")
 *   //sampleEnd
 *   println(result)
 *  }
 *  ```
 */
@JvmName("tupleRight")
@Suppress(
  "UNCHECKED_CAST",
  "USELESS_CAST",
  "EXTENSION_SHADOWED_BY_MEMBER",
  "UNUSED_PARAMETER"
)
fun <A, B> Kind<BIOPartialOf<Nothing>, A>.tupleRight(arg1: B): Kind<BIOPartialOf<Nothing>, Tuple2<A, B>> = arrow.fx.BIO.functor().run {
  this@tupleRight.tupleRight<A, B>(arg1) as arrow.Kind<BIOPartialOf<Nothing>, arrow.core.Tuple2<A, B>>
}

/**
 *  Given [A] is a sub type of [B], re-type this value from Kind<F, A> to Kind<F, B>
 *
 *  Kind<F, A> -> Kind<F, B>
 *
 *  ```kotlin:ank:playground
 *  import arrow.fx.*
 * import arrow.fx.extensions.bio.functor.*
 * import arrow.core.*
 *
 *
 *  import arrow.fx.extensions.bio.applicative.just
 *  import arrow.Kind
 *
 *  fun main(args: Array<String>) {
 *   val result: Kind<*, CharSequence> =
 *   //sampleStart
 *   "Hello".just<String, String>().map<String, String, String>({ "$it World" }).widen<String,
 * String, String>()
 *   //sampleEnd
 *   println(result)
 *  }
 *  ```
 */
@JvmName("widen")
@Suppress(
  "UNCHECKED_CAST",
  "USELESS_CAST",
  "EXTENSION_SHADOWED_BY_MEMBER",
  "UNUSED_PARAMETER"
)
fun <B, A : B> Kind<BIOPartialOf<Nothing>, A>.widen(): Kind<BIOPartialOf<Nothing>, B> = arrow.fx.BIO.functor().run {
  this@widen.widen<B, A>() as arrow.Kind<BIOPartialOf<Nothing>, B>
}

/**
 *  ank_macro_hierarchy(arrow.typeclasses.Functor)
 *
 *  The [Functor] type class abstracts the ability to [map] over the computational context of a type constructor.
 *  Examples of type constructors that can implement instances of the Functor type class include [BIO],
 *  [arrow.core.Option], [arrow.core.NonEmptyList], [List] and many other data types that include a [map] function with the shape
 *  `fun <F, A, B> Kind<F, A>.map(f: (A) -> B): Kind<F, B>` where `F` refers to any type constructor whose contents can be transformed.
 *
 *  ```kotlin:ank:playground
 *  import arrow.fx.*
 * import arrow.fx.extensions.bio.functor.*
 * import arrow.core.*
 *
 *
 *
 *  fun main(args: Array<String>) {
 *   val result =
 *   //sampleStart
 *   BIO.functor<String>()
 *   //sampleEnd
 *   println(result)
 *  }
 *  ```
 *
 *  ### Example
 *
 *  Oftentimes we find ourselves in situations where we need to transform the contents of some data type.
 *  [map] allows us to safely compute over values under the assumption that they'll be there returning the
 *  transformation encapsulated in the same context.
 *
 *  Consider both [arrow.core.Option] and [arrow.core.Try]:
 *
 *  `Option<A>` allows us to model absence and has two possible states, `Some(a: A)` if the value is not absent and `None` to represent an empty case.
 *  In a similar fashion `Try<A>` may have two possible cases `Success(a: A)` for computations that succeed and `Failure(e: Throwable)` if they fail
 *  with an exception.
 *
 *  Both [arrow.core.Try] and [arrow.core.Option] are examples of data types that can be computed over transforming their inner results.
 *
 *  ```kotlin:ank:playground
 *  import arrow.*
 *  import arrow.core.*
 *
 *  fun main(args: Array<String>) {
 *   val result =
 *   //sampleStart
 * 2 }
 *   //sampleEnd
 *   println(result)
 *  }
 *  ```
 *
 *  ```kotlin:ank:playground
 *  import arrow.*
 *  import arrow.core.*
 *
 *  fun main(args: Array<String>) {
 *   val result =
 *   //sampleStart
 * 2 }
 *   //sampleEnd
 *   println(result)
 *  }
 *  ```
 */
@Suppress(
  "UNCHECKED_CAST",
  "NOTHING_TO_INLINE"
)
inline fun Companion.functor(): IOFunctor = functor_singleton
