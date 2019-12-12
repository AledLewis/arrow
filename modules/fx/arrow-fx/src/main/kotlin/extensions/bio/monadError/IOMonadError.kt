package arrow.fx.extensions.bio.monadError

import arrow.Kind
import arrow.core.Either
import arrow.fx.BIO.Companion
import arrow.fx.BIOPartialOf
import arrow.fx.extensions.IOMonadError
import kotlin.Boolean
import kotlin.Function0
import kotlin.Function1
import kotlin.PublishedApi
import kotlin.Suppress
import kotlin.Throwable
import kotlin.jvm.JvmName

/**
 * cached extension
 */
@PublishedApi()
internal val monadError_singleton: IOMonadError = object : arrow.fx.extensions.IOMonadError {}

@JvmName("ensure")
@Suppress(
  "UNCHECKED_CAST",
  "USELESS_CAST",
  "EXTENSION_SHADOWED_BY_MEMBER",
  "UNUSED_PARAMETER"
)
fun <A> Kind<BIOPartialOf<Nothing>, A>.ensure(arg1: Function0<Throwable>, arg2: Function1<A, Boolean>): Kind<BIOPartialOf<Nothing>, A> =
  arrow.fx.BIO.monadError().run {
    this@ensure.ensure<A>(arg1, arg2) as arrow.Kind<BIOPartialOf<Nothing>, A>
  }

@JvmName("rethrow")
@Suppress(
  "UNCHECKED_CAST",
  "USELESS_CAST",
  "EXTENSION_SHADOWED_BY_MEMBER",
  "UNUSED_PARAMETER"
)
fun <A> Kind<BIOPartialOf<Nothing>, Either<Throwable, A>>.rethrow(): Kind<BIOPartialOf<Nothing>, A> = arrow.fx.BIO.monadError().run {
  this@rethrow.rethrow<A>() as arrow.Kind<BIOPartialOf<Nothing>, A>
}

@Suppress(
  "UNCHECKED_CAST",
  "NOTHING_TO_INLINE"
)
inline fun Companion.monadError(): IOMonadError = monadError_singleton
