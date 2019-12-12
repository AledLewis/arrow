package arrow.fx.extensions.bio.apply

import arrow.Kind
import arrow.core.Eval
import arrow.core.Tuple10
import arrow.core.Tuple2
import arrow.core.Tuple3
import arrow.core.Tuple4
import arrow.core.Tuple5
import arrow.core.Tuple6
import arrow.core.Tuple7
import arrow.core.Tuple8
import arrow.core.Tuple9
import arrow.fx.BIO.Companion
import arrow.fx.BIOPartialOf
import arrow.fx.extensions.IOApply
import kotlin.Function1
import kotlin.PublishedApi
import kotlin.Suppress
import kotlin.jvm.JvmName

/**
 * cached extension
 */
@PublishedApi()
internal val apply_singleton: IOApply = object : arrow.fx.extensions.IOApply {}

@JvmName("map")
@Suppress(
  "UNCHECKED_CAST",
  "USELESS_CAST",
  "EXTENSION_SHADOWED_BY_MEMBER",
  "UNUSED_PARAMETER"
)
fun <A, B, Z> map(
  arg0: Kind<BIOPartialOf<Nothing>, A>,
  arg1: Kind<BIOPartialOf<Nothing>, B>,
  arg2: Function1<Tuple2<A, B>, Z>
): Kind<BIOPartialOf<Nothing>, Z> = arrow.fx.BIO
  .apply()
  .map<A, B, Z>(arg0, arg1, arg2) as arrow.Kind<BIOPartialOf<Nothing>, Z>

@JvmName("map")
@Suppress(
  "UNCHECKED_CAST",
  "USELESS_CAST",
  "EXTENSION_SHADOWED_BY_MEMBER",
  "UNUSED_PARAMETER"
)
fun <A, B, C, Z> map(
  arg0: Kind<BIOPartialOf<Nothing>, A>,
  arg1: Kind<BIOPartialOf<Nothing>, B>,
  arg2: Kind<BIOPartialOf<Nothing>, C>,
  arg3: Function1<Tuple3<A, B, C>, Z>
): Kind<BIOPartialOf<Nothing>, Z> = arrow.fx.BIO
  .apply()
  .map<A, B, C, Z>(arg0, arg1, arg2, arg3) as arrow.Kind<BIOPartialOf<Nothing>, Z>

@JvmName("map")
@Suppress(
  "UNCHECKED_CAST",
  "USELESS_CAST",
  "EXTENSION_SHADOWED_BY_MEMBER",
  "UNUSED_PARAMETER"
)
fun <A, B, C, D, Z> map(
  arg0: Kind<BIOPartialOf<Nothing>, A>,
  arg1: Kind<BIOPartialOf<Nothing>, B>,
  arg2: Kind<BIOPartialOf<Nothing>, C>,
  arg3: Kind<BIOPartialOf<Nothing>, D>,
  arg4: Function1<Tuple4<A, B, C, D>, Z>
): Kind<BIOPartialOf<Nothing>, Z> = arrow.fx.BIO
  .apply()
  .map<A, B, C, D, Z>(arg0, arg1, arg2, arg3, arg4) as arrow.Kind<BIOPartialOf<Nothing>, Z>

@JvmName("map")
@Suppress(
  "UNCHECKED_CAST",
  "USELESS_CAST",
  "EXTENSION_SHADOWED_BY_MEMBER",
  "UNUSED_PARAMETER"
)
fun <A, B, C, D, E, Z> map(
  arg0: Kind<BIOPartialOf<Nothing>, A>,
  arg1: Kind<BIOPartialOf<Nothing>, B>,
  arg2: Kind<BIOPartialOf<Nothing>, C>,
  arg3: Kind<BIOPartialOf<Nothing>, D>,
  arg4: Kind<BIOPartialOf<Nothing>, E>,
  arg5: Function1<Tuple5<A, B, C, D, E>, Z>
): Kind<BIOPartialOf<Nothing>, Z> = arrow.fx.BIO
  .apply()
  .map<A, B, C, D, E, Z>(arg0, arg1, arg2, arg3, arg4, arg5) as arrow.Kind<BIOPartialOf<Nothing>, Z>

@JvmName("map")
@Suppress(
  "UNCHECKED_CAST",
  "USELESS_CAST",
  "EXTENSION_SHADOWED_BY_MEMBER",
  "UNUSED_PARAMETER"
)
fun <A, B, C, D, E, FF, Z> map(
  arg0: Kind<BIOPartialOf<Nothing>, A>,
  arg1: Kind<BIOPartialOf<Nothing>, B>,
  arg2: Kind<BIOPartialOf<Nothing>, C>,
  arg3: Kind<BIOPartialOf<Nothing>, D>,
  arg4: Kind<BIOPartialOf<Nothing>, E>,
  arg5: Kind<BIOPartialOf<Nothing>, FF>,
  arg6: Function1<Tuple6<A, B, C, D, E, FF>, Z>
): Kind<BIOPartialOf<Nothing>, Z> = arrow.fx.BIO
  .apply()
  .map<A, B, C, D, E, FF, Z>(arg0, arg1, arg2, arg3, arg4, arg5, arg6) as arrow.Kind<BIOPartialOf<Nothing>, Z>

@JvmName("map")
@Suppress(
  "UNCHECKED_CAST",
  "USELESS_CAST",
  "EXTENSION_SHADOWED_BY_MEMBER",
  "UNUSED_PARAMETER"
)
fun <A, B, C, D, E, FF, G, Z> map(
  arg0: Kind<BIOPartialOf<Nothing>, A>,
  arg1: Kind<BIOPartialOf<Nothing>, B>,
  arg2: Kind<BIOPartialOf<Nothing>, C>,
  arg3: Kind<BIOPartialOf<Nothing>, D>,
  arg4: Kind<BIOPartialOf<Nothing>, E>,
  arg5: Kind<BIOPartialOf<Nothing>, FF>,
  arg6: Kind<BIOPartialOf<Nothing>, G>,
  arg7: Function1<Tuple7<A, B, C, D, E, FF, G>, Z>
): Kind<BIOPartialOf<Nothing>, Z> = arrow.fx.BIO
  .apply()
  .map<A, B, C, D, E, FF, G, Z>(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7) as arrow.Kind<BIOPartialOf<Nothing>, Z>

@JvmName("map")
@Suppress(
  "UNCHECKED_CAST",
  "USELESS_CAST",
  "EXTENSION_SHADOWED_BY_MEMBER",
  "UNUSED_PARAMETER"
)
fun <A, B, C, D, E, FF, G, H, Z> map(
  arg0: Kind<BIOPartialOf<Nothing>, A>,
  arg1: Kind<BIOPartialOf<Nothing>, B>,
  arg2: Kind<BIOPartialOf<Nothing>, C>,
  arg3: Kind<BIOPartialOf<Nothing>, D>,
  arg4: Kind<BIOPartialOf<Nothing>, E>,
  arg5: Kind<BIOPartialOf<Nothing>, FF>,
  arg6: Kind<BIOPartialOf<Nothing>, G>,
  arg7: Kind<BIOPartialOf<Nothing>, H>,
  arg8: Function1<Tuple8<A, B, C, D, E, FF, G, H>, Z>
): Kind<BIOPartialOf<Nothing>, Z> = arrow.fx.BIO
  .apply()
  .map<A, B, C, D, E, FF, G, H, Z>(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8) as
  arrow.Kind<BIOPartialOf<Nothing>, Z>

@JvmName("map")
@Suppress(
  "UNCHECKED_CAST",
  "USELESS_CAST",
  "EXTENSION_SHADOWED_BY_MEMBER",
  "UNUSED_PARAMETER"
)
fun <A, B, C, D, E, FF, G, H, I, Z> map(
  arg0: Kind<BIOPartialOf<Nothing>, A>,
  arg1: Kind<BIOPartialOf<Nothing>, B>,
  arg2: Kind<BIOPartialOf<Nothing>, C>,
  arg3: Kind<BIOPartialOf<Nothing>, D>,
  arg4: Kind<BIOPartialOf<Nothing>, E>,
  arg5: Kind<BIOPartialOf<Nothing>, FF>,
  arg6: Kind<BIOPartialOf<Nothing>, G>,
  arg7: Kind<BIOPartialOf<Nothing>, H>,
  arg8: Kind<BIOPartialOf<Nothing>, I>,
  arg9: Function1<Tuple9<A, B, C, D, E, FF, G, H, I>, Z>
): Kind<BIOPartialOf<Nothing>, Z> = arrow.fx.BIO
  .apply()
  .map<A, B, C, D, E, FF, G, H, I, Z>(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9)
  as arrow.Kind<BIOPartialOf<Nothing>, Z>

@JvmName("map")
@Suppress(
  "UNCHECKED_CAST",
  "USELESS_CAST",
  "EXTENSION_SHADOWED_BY_MEMBER",
  "UNUSED_PARAMETER"
)
fun <A, B, C, D, E, FF, G, H, I, J, Z> map(
  arg0: Kind<BIOPartialOf<Nothing>, A>,
  arg1: Kind<BIOPartialOf<Nothing>, B>,
  arg2: Kind<BIOPartialOf<Nothing>, C>,
  arg3: Kind<BIOPartialOf<Nothing>, D>,
  arg4: Kind<BIOPartialOf<Nothing>, E>,
  arg5: Kind<BIOPartialOf<Nothing>, FF>,
  arg6: Kind<BIOPartialOf<Nothing>, G>,
  arg7: Kind<BIOPartialOf<Nothing>, H>,
  arg8: Kind<BIOPartialOf<Nothing>, I>,
  arg9: Kind<BIOPartialOf<Nothing>, J>,
  arg10: Function1<Tuple10<A, B, C, D, E, FF, G, H, I, J>, Z>
): Kind<BIOPartialOf<Nothing>, Z> = arrow.fx.BIO
  .apply()
  .map<A, B, C, D, E, FF, G, H, I, J,
    Z>(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10) as arrow.Kind<BIOPartialOf<Nothing>, Z>

@JvmName("map2")
@Suppress(
  "UNCHECKED_CAST",
  "USELESS_CAST",
  "EXTENSION_SHADOWED_BY_MEMBER",
  "UNUSED_PARAMETER"
)
fun <A, B, Z> Kind<BIOPartialOf<Nothing>, A>.map2(arg1: Kind<BIOPartialOf<Nothing>, B>, arg2: Function1<Tuple2<A, B>, Z>): Kind<BIOPartialOf<Nothing>, Z> =
  arrow.fx.BIO.apply().run {
    this@map2.map2<A, B, Z>(arg1, arg2) as arrow.Kind<BIOPartialOf<Nothing>, Z>
  }

@JvmName("map2Eval")
@Suppress(
  "UNCHECKED_CAST",
  "USELESS_CAST",
  "EXTENSION_SHADOWED_BY_MEMBER",
  "UNUSED_PARAMETER"
)
fun <A, B, Z> Kind<BIOPartialOf<Nothing>, A>.map2Eval(arg1: Eval<Kind<BIOPartialOf<Nothing>, B>>, arg2: Function1<Tuple2<A, B>, Z>):
  Eval<Kind<BIOPartialOf<Nothing>, Z>> = arrow.fx.BIO.apply().run {
  this@map2Eval.map2Eval<A, B, Z>(arg1, arg2) as arrow.core.Eval<arrow.Kind<BIOPartialOf<Nothing>, Z>>
}

@JvmName("product")
@Suppress(
  "UNCHECKED_CAST",
  "USELESS_CAST",
  "EXTENSION_SHADOWED_BY_MEMBER",
  "UNUSED_PARAMETER"
)
fun <A, B> Kind<BIOPartialOf<Nothing>, A>.product(arg1: Kind<BIOPartialOf<Nothing>, B>): Kind<BIOPartialOf<Nothing>, Tuple2<A, B>> =
  arrow.fx.BIO.apply().run {
    this@product.product<A, B>(arg1) as arrow.Kind<BIOPartialOf<Nothing>, arrow.core.Tuple2<A, B>>
  }

@JvmName("product1")
@Suppress(
  "UNCHECKED_CAST",
  "USELESS_CAST",
  "EXTENSION_SHADOWED_BY_MEMBER",
  "UNUSED_PARAMETER"
)
fun <A, B, Z> Kind<BIOPartialOf<Nothing>, Tuple2<A, B>>.product(arg1: Kind<BIOPartialOf<Nothing>, Z>): Kind<BIOPartialOf<Nothing>, Tuple3<A, B, Z>> =
  arrow.fx.BIO.apply().run {
    this@product.product<A, B, Z>(arg1) as arrow.Kind<BIOPartialOf<Nothing>, arrow.core.Tuple3<A, B, Z>>
  }

@JvmName("product2")
@Suppress(
  "UNCHECKED_CAST",
  "USELESS_CAST",
  "EXTENSION_SHADOWED_BY_MEMBER",
  "UNUSED_PARAMETER"
)
fun <A, B, C, Z> Kind<BIOPartialOf<Nothing>, Tuple3<A, B, C>>.product(arg1: Kind<BIOPartialOf<Nothing>, Z>): Kind<BIOPartialOf<Nothing>, Tuple4<A, B, C, Z>> = arrow.fx.BIO.apply().run {
  this@product.product<A, B, C, Z>(arg1) as arrow.Kind<BIOPartialOf<Nothing>, arrow.core.Tuple4<A, B, C, Z>>
}

@JvmName("product3")
@Suppress(
  "UNCHECKED_CAST",
  "USELESS_CAST",
  "EXTENSION_SHADOWED_BY_MEMBER",
  "UNUSED_PARAMETER"
)
fun <A, B, C, D, Z> Kind<BIOPartialOf<Nothing>, Tuple4<A, B, C, D>>.product(arg1: Kind<BIOPartialOf<Nothing>, Z>): Kind<BIOPartialOf<Nothing>, Tuple5<A, B,
  C, D, Z>> = arrow.fx.BIO.apply().run {
  this@product.product<A, B, C, D, Z>(arg1) as arrow.Kind<BIOPartialOf<Nothing>, arrow.core.Tuple5<A, B, C, D, Z>>
}

@JvmName("product4")
@Suppress(
  "UNCHECKED_CAST",
  "USELESS_CAST",
  "EXTENSION_SHADOWED_BY_MEMBER",
  "UNUSED_PARAMETER"
)
fun <A, B, C, D, E, Z> Kind<BIOPartialOf<Nothing>, Tuple5<A, B, C, D, E>>.product(arg1: Kind<BIOPartialOf<Nothing>, Z>):
  Kind<BIOPartialOf<Nothing>, Tuple6<A, B, C, D, E, Z>> = arrow.fx.BIO.apply().run {
  this@product.product<A, B, C, D, E, Z>(arg1) as arrow.Kind<BIOPartialOf<Nothing>, arrow.core.Tuple6<A, B, C, D, E, Z>>
}

@JvmName("product5")
@Suppress(
  "UNCHECKED_CAST",
  "USELESS_CAST",
  "EXTENSION_SHADOWED_BY_MEMBER",
  "UNUSED_PARAMETER"
)
fun <A, B, C, D, E, FF, Z> Kind<BIOPartialOf<Nothing>, Tuple6<A, B, C, D, E, FF>>.product(arg1: Kind<BIOPartialOf<Nothing>, Z>):
  Kind<BIOPartialOf<Nothing>, Tuple7<A, B, C, D, E, FF, Z>> = arrow.fx.BIO.apply().run {
  this@product.product<A, B, C, D, E, FF, Z>(arg1) as arrow.Kind<BIOPartialOf<Nothing>, arrow.core.Tuple7<A, B, C, D, E,
    FF, Z>>
}

@JvmName("product6")
@Suppress(
  "UNCHECKED_CAST",
  "USELESS_CAST",
  "EXTENSION_SHADOWED_BY_MEMBER",
  "UNUSED_PARAMETER"
)
fun <A, B, C, D, E, FF, G, Z> Kind<BIOPartialOf<Nothing>, Tuple7<A, B, C, D, E, FF, G>>.product(arg1: Kind<BIOPartialOf<Nothing>, Z>):
  Kind<BIOPartialOf<Nothing>, Tuple8<A, B, C, D, E, FF, G, Z>> = arrow.fx.BIO.apply().run {
  this@product.product<A, B, C, D, E, FF, G, Z>(arg1) as arrow.Kind<BIOPartialOf<Nothing>, arrow.core.Tuple8<A, B, C, D, E,
    FF, G, Z>>
}

@JvmName("product7")
@Suppress(
  "UNCHECKED_CAST",
  "USELESS_CAST",
  "EXTENSION_SHADOWED_BY_MEMBER",
  "UNUSED_PARAMETER"
)
fun <A, B, C, D, E, FF, G, H, Z> Kind<BIOPartialOf<Nothing>, Tuple8<A, B, C, D, E, FF, G,
  H>>.product(arg1: Kind<BIOPartialOf<Nothing>, Z>): Kind<BIOPartialOf<Nothing>, Tuple9<A, B, C, D, E, FF, G, H, Z>> =
  arrow.fx.BIO.apply().run {
    this@product.product<A, B, C, D, E, FF, G, H, Z>(arg1) as arrow.Kind<BIOPartialOf<Nothing>, arrow.core.Tuple9<A, B, C, D,
      E, FF, G, H, Z>>
  }

@JvmName("product8")
@Suppress(
  "UNCHECKED_CAST",
  "USELESS_CAST",
  "EXTENSION_SHADOWED_BY_MEMBER",
  "UNUSED_PARAMETER"
)
fun <A, B, C, D, E, FF, G, H, I, Z> Kind<BIOPartialOf<Nothing>, Tuple9<A, B, C, D, E, FF, G, H,
  I>>.product(arg1: Kind<BIOPartialOf<Nothing>, Z>): Kind<BIOPartialOf<Nothing>, Tuple10<A, B, C, D, E, FF, G, H, I, Z>> =
  arrow.fx.BIO.apply().run {
    this@product.product<A, B, C, D, E, FF, G, H, I, Z>(arg1) as arrow.Kind<BIOPartialOf<Nothing>, arrow.core.Tuple10<A, B,
      C, D, E, FF, G, H, I, Z>>
  }

@JvmName("tupled")
@Suppress(
  "UNCHECKED_CAST",
  "USELESS_CAST",
  "EXTENSION_SHADOWED_BY_MEMBER",
  "UNUSED_PARAMETER"
)
fun <A, B> tupled(arg0: Kind<BIOPartialOf<Nothing>, A>, arg1: Kind<BIOPartialOf<Nothing>, B>): Kind<BIOPartialOf<Nothing>, Tuple2<A, B>> = arrow.fx.BIO
  .apply()
  .tupled<A, B>(arg0, arg1) as arrow.Kind<BIOPartialOf<Nothing>, arrow.core.Tuple2<A, B>>

@JvmName("tupled")
@Suppress(
  "UNCHECKED_CAST",
  "USELESS_CAST",
  "EXTENSION_SHADOWED_BY_MEMBER",
  "UNUSED_PARAMETER"
)
fun <A, B, C> tupled(
  arg0: Kind<BIOPartialOf<Nothing>, A>,
  arg1: Kind<BIOPartialOf<Nothing>, B>,
  arg2: Kind<BIOPartialOf<Nothing>, C>
): Kind<BIOPartialOf<Nothing>, Tuple3<A, B, C>> = arrow.fx.BIO
  .apply()
  .tupled<A, B, C>(arg0, arg1, arg2) as arrow.Kind<BIOPartialOf<Nothing>, arrow.core.Tuple3<A, B, C>>

@JvmName("tupled")
@Suppress(
  "UNCHECKED_CAST",
  "USELESS_CAST",
  "EXTENSION_SHADOWED_BY_MEMBER",
  "UNUSED_PARAMETER"
)
fun <A, B, C, D> tupled(
  arg0: Kind<BIOPartialOf<Nothing>, A>,
  arg1: Kind<BIOPartialOf<Nothing>, B>,
  arg2: Kind<BIOPartialOf<Nothing>, C>,
  arg3: Kind<BIOPartialOf<Nothing>, D>
): Kind<BIOPartialOf<Nothing>, Tuple4<A, B, C, D>> = arrow.fx.BIO
  .apply()
  .tupled<A, B, C, D>(arg0, arg1, arg2, arg3) as Kind<BIOPartialOf<Nothing>, Tuple4<A, B, C, D>>

@JvmName("tupled")
@Suppress(
  "UNCHECKED_CAST",
  "USELESS_CAST",
  "EXTENSION_SHADOWED_BY_MEMBER",
  "UNUSED_PARAMETER"
)
fun <A, B, C, D, E> tupled(
  arg0: Kind<BIOPartialOf<Nothing>, A>,
  arg1: Kind<BIOPartialOf<Nothing>, B>,
  arg2: Kind<BIOPartialOf<Nothing>, C>,
  arg3: Kind<BIOPartialOf<Nothing>, D>,
  arg4: Kind<BIOPartialOf<Nothing>, E>
): Kind<BIOPartialOf<Nothing>, Tuple5<A, B, C, D, E>> = arrow.fx.BIO
  .apply()
  .tupled<A, B, C, D, E>(arg0, arg1, arg2, arg3, arg4) as Kind<BIOPartialOf<Nothing>, Tuple5<A, B, C, D,
  E>>

@JvmName("tupled")
@Suppress(
  "UNCHECKED_CAST",
  "USELESS_CAST",
  "EXTENSION_SHADOWED_BY_MEMBER",
  "UNUSED_PARAMETER"
)
fun <A, B, C, D, E, FF> tupled(
  arg0: Kind<BIOPartialOf<Nothing>, A>,
  arg1: Kind<BIOPartialOf<Nothing>, B>,
  arg2: Kind<BIOPartialOf<Nothing>, C>,
  arg3: Kind<BIOPartialOf<Nothing>, D>,
  arg4: Kind<BIOPartialOf<Nothing>, E>,
  arg5: Kind<BIOPartialOf<Nothing>, FF>
): Kind<BIOPartialOf<Nothing>, Tuple6<A, B, C, D, E, FF>> = arrow.fx.BIO
  .apply()
  .tupled<A, B, C, D, E, FF>(arg0, arg1, arg2, arg3, arg4, arg5) as Kind<BIOPartialOf<Nothing>, Tuple6<A,
  B, C, D, E, FF>>

@JvmName("tupled")
@Suppress(
  "UNCHECKED_CAST",
  "USELESS_CAST",
  "EXTENSION_SHADOWED_BY_MEMBER",
  "UNUSED_PARAMETER"
)
fun <A, B, C, D, E, FF, G> tupled(
  arg0: Kind<BIOPartialOf<Nothing>, A>,
  arg1: Kind<BIOPartialOf<Nothing>, B>,
  arg2: Kind<BIOPartialOf<Nothing>, C>,
  arg3: Kind<BIOPartialOf<Nothing>, D>,
  arg4: Kind<BIOPartialOf<Nothing>, E>,
  arg5: Kind<BIOPartialOf<Nothing>, FF>,
  arg6: Kind<BIOPartialOf<Nothing>, G>
): Kind<BIOPartialOf<Nothing>, Tuple7<A, B, C, D, E, FF, G>> = arrow.fx.BIO
  .apply()
  .tupled<A, B, C, D, E, FF, G>(arg0, arg1, arg2, arg3, arg4, arg5, arg6) as
  Kind<BIOPartialOf<Nothing>, Tuple7<A, B, C, D, E, FF, G>>

@JvmName("tupled")
@Suppress(
  "UNCHECKED_CAST",
  "USELESS_CAST",
  "EXTENSION_SHADOWED_BY_MEMBER",
  "UNUSED_PARAMETER"
)
fun <A, B, C, D, E, FF, G, H> tupled(
  arg0: Kind<BIOPartialOf<Nothing>, A>,
  arg1: Kind<BIOPartialOf<Nothing>, B>,
  arg2: Kind<BIOPartialOf<Nothing>, C>,
  arg3: Kind<BIOPartialOf<Nothing>, D>,
  arg4: Kind<BIOPartialOf<Nothing>, E>,
  arg5: Kind<BIOPartialOf<Nothing>, FF>,
  arg6: Kind<BIOPartialOf<Nothing>, G>,
  arg7: Kind<BIOPartialOf<Nothing>, H>
): Kind<BIOPartialOf<Nothing>, Tuple8<A, B, C, D, E, FF, G, H>> = arrow.fx.BIO
  .apply()
  .tupled<A, B, C, D, E, FF, G, H>(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7) as
  Kind<BIOPartialOf<Nothing>, Tuple8<A, B, C, D, E, FF, G, H>>

@JvmName("tupled")
@Suppress(
  "UNCHECKED_CAST",
  "USELESS_CAST",
  "EXTENSION_SHADOWED_BY_MEMBER",
  "UNUSED_PARAMETER"
)
fun <A, B, C, D, E, FF, G, H, I> tupled(
  arg0: Kind<BIOPartialOf<Nothing>, A>,
  arg1: Kind<BIOPartialOf<Nothing>, B>,
  arg2: Kind<BIOPartialOf<Nothing>, C>,
  arg3: Kind<BIOPartialOf<Nothing>, D>,
  arg4: Kind<BIOPartialOf<Nothing>, E>,
  arg5: Kind<BIOPartialOf<Nothing>, FF>,
  arg6: Kind<BIOPartialOf<Nothing>, G>,
  arg7: Kind<BIOPartialOf<Nothing>, H>,
  arg8: Kind<BIOPartialOf<Nothing>, I>
): Kind<BIOPartialOf<Nothing>, Tuple9<A, B, C, D, E, FF, G, H, I>> = arrow.fx.BIO
  .apply()
  .tupled<A, B, C, D, E, FF, G, H, I>(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8) as
  Kind<BIOPartialOf<Nothing>, Tuple9<A, B, C, D, E, FF, G, H, I>>

@JvmName("tupled")
@Suppress(
  "UNCHECKED_CAST",
  "USELESS_CAST",
  "EXTENSION_SHADOWED_BY_MEMBER",
  "UNUSED_PARAMETER"
)
fun <A, B, C, D, E, FF, G, H, I, J> tupled(
  arg0: Kind<BIOPartialOf<Nothing>, A>,
  arg1: Kind<BIOPartialOf<Nothing>, B>,
  arg2: Kind<BIOPartialOf<Nothing>, C>,
  arg3: Kind<BIOPartialOf<Nothing>, D>,
  arg4: Kind<BIOPartialOf<Nothing>, E>,
  arg5: Kind<BIOPartialOf<Nothing>, FF>,
  arg6: Kind<BIOPartialOf<Nothing>, G>,
  arg7: Kind<BIOPartialOf<Nothing>, H>,
  arg8: Kind<BIOPartialOf<Nothing>, I>,
  arg9: Kind<BIOPartialOf<Nothing>, J>
): Kind<BIOPartialOf<Nothing>, Tuple10<A, B, C, D, E, FF, G, H, I, J>> = arrow.fx.BIO
  .apply()
  .tupled<A, B, C, D, E, FF, G, H, I,
    J>(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9) as
  Kind<BIOPartialOf<Nothing>, Tuple10<A, B, C, D, E, FF, G, H, I, J>>

@Suppress(
  "UNCHECKED_CAST",
  "NOTHING_TO_INLINE"
)
inline fun Companion.apply(): IOApply = apply_singleton
