package arrow.fx.extensions.bio.applicative

import arrow.Kind
import arrow.fx.BIO.Companion
import arrow.fx.BIOPartialOf
import arrow.fx.extensions.IOApplicative
import arrow.typeclasses.Monoid
import kotlin.Int
import kotlin.PublishedApi
import kotlin.Suppress
import kotlin.Unit
import kotlin.collections.List
import kotlin.jvm.JvmName

/**
 * cached extension
 */
@PublishedApi()
internal val applicative_singleton: IOApplicative = object : arrow.fx.extensions.IOApplicative {}

@JvmName("just1")
@Suppress(
  "UNCHECKED_CAST",
  "USELESS_CAST",
  "EXTENSION_SHADOWED_BY_MEMBER",
  "UNUSED_PARAMETER"
)
fun <A> A.just(): Kind<BIOPartialOf<Nothing>, A> = arrow.fx.BIO.applicative().run {
  this@just.just<A>() as arrow.Kind<BIOPartialOf<Nothing>, A>
}

@JvmName("unit")
@Suppress(
  "UNCHECKED_CAST",
  "USELESS_CAST",
  "EXTENSION_SHADOWED_BY_MEMBER",
  "UNUSED_PARAMETER"
)
fun unit(): Kind<BIOPartialOf<Nothing>, Unit> = arrow.fx.BIO
  .applicative()
  .unit() as arrow.Kind<BIOPartialOf<Nothing>, Unit>

@JvmName("replicate")
@Suppress(
  "UNCHECKED_CAST",
  "USELESS_CAST",
  "EXTENSION_SHADOWED_BY_MEMBER",
  "UNUSED_PARAMETER"
)
fun <A> Kind<BIOPartialOf<Nothing>, A>.replicate(arg1: Int): Kind<BIOPartialOf<Nothing>, List<A>> = arrow.fx.BIO.applicative().run {
  this@replicate.replicate<A>(arg1) as Kind<BIOPartialOf<Nothing>, List<A>>
}

@JvmName("replicate")
@Suppress(
  "UNCHECKED_CAST",
  "USELESS_CAST",
  "EXTENSION_SHADOWED_BY_MEMBER",
  "UNUSED_PARAMETER"
)
fun <A> Kind<BIOPartialOf<Nothing>, A>.replicate(arg1: Int, arg2: Monoid<A>): Kind<BIOPartialOf<Nothing>, A> =
  arrow.fx.BIO.applicative().run {
    this@replicate.replicate<A>(arg1, arg2) as arrow.Kind<BIOPartialOf<Nothing>, A>
  }

@Suppress(
  "UNCHECKED_CAST",
  "NOTHING_TO_INLINE"
)
inline fun Companion.applicative(): IOApplicative = applicative_singleton
