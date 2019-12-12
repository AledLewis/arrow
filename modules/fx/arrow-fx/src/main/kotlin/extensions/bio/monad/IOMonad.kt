package arrow.fx.extensions.bio.monad

import arrow.Kind
import arrow.core.Either
import arrow.core.Eval
import arrow.core.Tuple2
import arrow.fx.BIO
import arrow.fx.BIO.Companion
import arrow.fx.BIOOf
import arrow.fx.BIOPartialOf
import arrow.fx.extensions.IOMonad
import arrow.fx.fix
import kotlin.Boolean
import kotlin.Function0
import kotlin.Function1
import kotlin.PublishedApi
import kotlin.Suppress
import kotlin.jvm.JvmName

fun <A, B> BIOOf<Nothing, A>.map(f: (A) -> B): BIO<Nothing, B> =
  fix().map(f)

/**
 * cached extension
 */
@PublishedApi()
internal val monad_singleton: IOMonad = object : arrow.fx.extensions.IOMonad {}

/**
 *  @see [Apply.ap]
 */
@JvmName("ap")
@Suppress(
  "UNCHECKED_CAST",
  "USELESS_CAST",
  "EXTENSION_SHADOWED_BY_MEMBER",
  "UNUSED_PARAMETER"
)
fun <A, B> Kind<BIOPartialOf<Nothing>, A>.ap(arg1: Kind<BIOPartialOf<Nothing>, Function1<A, B>>): Kind<BIOPartialOf<Nothing>, B> = arrow.fx.BIO.monad().run {
  this@ap.ap<A, B>(arg1) as arrow.Kind<BIOPartialOf<Nothing>, B>
}

@JvmName("flatten")
@Suppress(
  "UNCHECKED_CAST",
  "USELESS_CAST",
  "EXTENSION_SHADOWED_BY_MEMBER",
  "UNUSED_PARAMETER"
)
fun <A> Kind<BIOPartialOf<Nothing>, Kind<BIOPartialOf<Nothing>, A>>.flatten(): Kind<BIOPartialOf<Nothing>, A> = arrow.fx.BIO.monad().run {
  this@flatten.flatten<A>() as arrow.Kind<BIOPartialOf<Nothing>, A>
}

@JvmName("followedBy")
@Suppress(
  "UNCHECKED_CAST",
  "USELESS_CAST",
  "EXTENSION_SHADOWED_BY_MEMBER",
  "UNUSED_PARAMETER"
)
fun <A, B> Kind<BIOPartialOf<Nothing>, A>.followedBy(arg1: Kind<BIOPartialOf<Nothing>, B>): Kind<BIOPartialOf<Nothing>, B> = arrow.fx.BIO.monad().run {
  this@followedBy.followedBy<A, B>(arg1) as arrow.Kind<BIOPartialOf<Nothing>, B>
}

@JvmName("followedByEval")
@Suppress(
  "UNCHECKED_CAST",
  "USELESS_CAST",
  "EXTENSION_SHADOWED_BY_MEMBER",
  "UNUSED_PARAMETER"
)
fun <A, B> Kind<BIOPartialOf<Nothing>, A>.followedByEval(arg1: Eval<Kind<BIOPartialOf<Nothing>, B>>): Kind<BIOPartialOf<Nothing>, B> =
  arrow.fx.BIO.monad().run {
    this@followedByEval.followedByEval<A, B>(arg1) as arrow.Kind<BIOPartialOf<Nothing>, B>
  }

@JvmName("effectM")
@Suppress(
  "UNCHECKED_CAST",
  "USELESS_CAST",
  "EXTENSION_SHADOWED_BY_MEMBER",
  "UNUSED_PARAMETER"
)
fun <A, B> Kind<BIOPartialOf<Nothing>, A>.effectM(arg1: Function1<A, Kind<BIOPartialOf<Nothing>, B>>): Kind<BIOPartialOf<Nothing>, A> =
  arrow.fx.BIO.monad().run {
    this@effectM.effectM<A, B>(arg1) as arrow.Kind<BIOPartialOf<Nothing>, A>
  }

@JvmName("flatTap")
@Suppress(
  "UNCHECKED_CAST",
  "USELESS_CAST",
  "EXTENSION_SHADOWED_BY_MEMBER",
  "UNUSED_PARAMETER"
)
fun <A, B> Kind<BIOPartialOf<Nothing>, A>.flatTap(arg1: Function1<A, Kind<BIOPartialOf<Nothing>, B>>): Kind<BIOPartialOf<Nothing>, A> =
  arrow.fx.BIO.monad().run {
    this@flatTap.flatTap<A, B>(arg1) as arrow.Kind<BIOPartialOf<Nothing>, A>
  }

@JvmName("productL")
@Suppress(
  "UNCHECKED_CAST",
  "USELESS_CAST",
  "EXTENSION_SHADOWED_BY_MEMBER",
  "UNUSED_PARAMETER"
)
fun <A, B> Kind<BIOPartialOf<Nothing>, A>.productL(arg1: Kind<BIOPartialOf<Nothing>, B>): Kind<BIOPartialOf<Nothing>, A> = arrow.fx.BIO.monad().run {
  this@productL.productL<A, B>(arg1) as arrow.Kind<BIOPartialOf<Nothing>, A>
}

@JvmName("forEffect")
@Suppress(
  "UNCHECKED_CAST",
  "USELESS_CAST",
  "EXTENSION_SHADOWED_BY_MEMBER",
  "UNUSED_PARAMETER"
)
fun <A, B> Kind<BIOPartialOf<Nothing>, A>.forEffect(arg1: Kind<BIOPartialOf<Nothing>, B>): Kind<BIOPartialOf<Nothing>, A> = arrow.fx.BIO.monad().run {
  this@forEffect.forEffect<A, B>(arg1) as arrow.Kind<BIOPartialOf<Nothing>, A>
}

@JvmName("productLEval")
@Suppress(
  "UNCHECKED_CAST",
  "USELESS_CAST",
  "EXTENSION_SHADOWED_BY_MEMBER",
  "UNUSED_PARAMETER"
)
fun <A, B> Kind<BIOPartialOf<Nothing>, A>.productLEval(arg1: Eval<Kind<BIOPartialOf<Nothing>, B>>): Kind<BIOPartialOf<Nothing>, A> =
  arrow.fx.BIO.monad().run {
    this@productLEval.productLEval<A, B>(arg1) as arrow.Kind<BIOPartialOf<Nothing>, A>
  }

@JvmName("forEffectEval")
@Suppress(
  "UNCHECKED_CAST",
  "USELESS_CAST",
  "EXTENSION_SHADOWED_BY_MEMBER",
  "UNUSED_PARAMETER"
)
fun <A, B> Kind<BIOPartialOf<Nothing>, A>.forEffectEval(arg1: Eval<Kind<BIOPartialOf<Nothing>, B>>): Kind<BIOPartialOf<Nothing>, A> =
  arrow.fx.BIO.monad().run {
    this@forEffectEval.forEffectEval<A, B>(arg1) as arrow.Kind<BIOPartialOf<Nothing>, A>
  }

@JvmName("mproduct")
@Suppress(
  "UNCHECKED_CAST",
  "USELESS_CAST",
  "EXTENSION_SHADOWED_BY_MEMBER",
  "UNUSED_PARAMETER"
)
fun <A, B> Kind<BIOPartialOf<Nothing>, A>.mproduct(arg1: Function1<A, Kind<BIOPartialOf<Nothing>, B>>): Kind<BIOPartialOf<Nothing>, Tuple2<A, B>> =
  arrow.fx.BIO.monad().run {
    this@mproduct.mproduct<A, B>(arg1) as arrow.Kind<BIOPartialOf<Nothing>, arrow.core.Tuple2<A, B>>
  }

@JvmName("ifM")
@Suppress(
  "UNCHECKED_CAST",
  "USELESS_CAST",
  "EXTENSION_SHADOWED_BY_MEMBER",
  "UNUSED_PARAMETER"
)
fun <B> Kind<BIOPartialOf<Nothing>, Boolean>.ifM(arg1: Function0<Kind<BIOPartialOf<Nothing>, B>>, arg2: Function0<Kind<BIOPartialOf<Nothing>, B>>):
  Kind<BIOPartialOf<Nothing>, B> = arrow.fx.BIO.monad().run {
  this@ifM.ifM<B>(arg1, arg2) as arrow.Kind<BIOPartialOf<Nothing>, B>
}

@JvmName("selectM")
@Suppress(
  "UNCHECKED_CAST",
  "USELESS_CAST",
  "EXTENSION_SHADOWED_BY_MEMBER",
  "UNUSED_PARAMETER"
)
fun <A, B> Kind<BIOPartialOf<Nothing>, Either<A, B>>.selectM(arg1: Kind<BIOPartialOf<Nothing>, Function1<A, B>>): Kind<BIOPartialOf<Nothing>, B> =
  arrow.fx.BIO.monad().run {
    this@selectM.selectM<A, B>(arg1) as arrow.Kind<BIOPartialOf<Nothing>, B>
  }

@JvmName("select")
@Suppress(
  "UNCHECKED_CAST",
  "USELESS_CAST",
  "EXTENSION_SHADOWED_BY_MEMBER",
  "UNUSED_PARAMETER"
)
fun <A, B> Kind<BIOPartialOf<Nothing>, Either<A, B>>.select(arg1: Kind<BIOPartialOf<Nothing>, Function1<A, B>>): Kind<BIOPartialOf<Nothing>, B> =
  arrow.fx.BIO.monad().run {
    this@select.select<A, B>(arg1) as arrow.Kind<BIOPartialOf<Nothing>, B>
  }

/**
 *  ank_macro_hierarchy(arrow.typeclasses.Monad)
 *
 *  [Monad] abstract over the ability to declare sequential computations that are dependent in the order or
 *  the results of previous computations.
 *
 *  Given a type constructor [F] with a value of [A] we can compose multiple operations of type
 *  `Kind<F, ?>` where `?` denotes a value being transformed.
 *
 *  This is true for all type constructors that can support the [Monad] type class including and not limited to
 *  [IO], [ObservableK], [Option], [Either], [List], [Try] ...
 *
 *  [The Monad Tutorial](https://arrow-kt.io/docs/patterns/monads/)
 */
@Suppress(
  "UNCHECKED_CAST",
  "NOTHING_TO_INLINE"
)
inline fun Companion.monad(): IOMonad = monad_singleton
