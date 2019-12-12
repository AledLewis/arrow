package arrow.fx.extensions.bio.applicativeError

import arrow.Kind
import arrow.core.Either
import arrow.core.ForOption
import arrow.core.ForTry
import arrow.fx.BIO.Companion
import arrow.fx.BIOPartialOf
import arrow.fx.extensions.IOApplicativeError
import arrow.typeclasses.ApplicativeError
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
internal val applicativeError_singleton: IOApplicativeError = object :
  arrow.fx.extensions.IOApplicativeError {}

@JvmName("raiseError1")
@Suppress(
  "UNCHECKED_CAST",
  "USELESS_CAST",
  "EXTENSION_SHADOWED_BY_MEMBER",
  "UNUSED_PARAMETER"
)
fun <A> Throwable.raiseError(): Kind<BIOPartialOf<Nothing>, A> = arrow.fx.BIO.applicativeError().run {
  this@raiseError.raiseError<A>() as Kind<BIOPartialOf<Nothing>, A>
}

@JvmName("fromOption")
@Suppress(
  "UNCHECKED_CAST",
  "USELESS_CAST",
  "EXTENSION_SHADOWED_BY_MEMBER",
  "UNUSED_PARAMETER"
)
fun <A> Kind<ForOption, A>.fromOption(arg1: Function0<Throwable>): Kind<BIOPartialOf<Nothing>, A> =
  arrow.fx.BIO.applicativeError().run {
    this@fromOption.fromOption<A>(arg1) as Kind<BIOPartialOf<Nothing>, A>
  }

@JvmName("fromEither")
@Suppress(
  "UNCHECKED_CAST",
  "USELESS_CAST",
  "EXTENSION_SHADOWED_BY_MEMBER",
  "UNUSED_PARAMETER"
)
fun <A, EE> Either<EE, A>.fromEither(arg1: Function1<EE, Throwable>): Kind<BIOPartialOf<Nothing>, A> =
  arrow.fx.BIO.applicativeError().run {
    this@fromEither.fromEither<A, EE>(arg1) as Kind<BIOPartialOf<Nothing>, A>
  }

@JvmName("fromTry")
@Suppress(
  "UNCHECKED_CAST",
  "USELESS_CAST",
  "EXTENSION_SHADOWED_BY_MEMBER",
  "UNUSED_PARAMETER"
)
fun <A> Kind<ForTry, A>.fromTry(arg1: Function1<Throwable, Throwable>): Kind<BIOPartialOf<Nothing>, A> =
  arrow.fx.BIO.applicativeError().run {
    this@fromTry.fromTry<A>(arg1) as arrow.Kind<BIOPartialOf<Nothing>, A>
  }

@JvmName("catch")
@Suppress(
  "UNCHECKED_CAST",
  "USELESS_CAST",
  "EXTENSION_SHADOWED_BY_MEMBER",
  "UNUSED_PARAMETER"
)
fun <A> catch(arg0: Function1<Throwable, Throwable>, arg1: Function0<A>): Kind<BIOPartialOf<Nothing>, A> = arrow.fx.BIO
  .applicativeError()
  .catch<A>(arg0, arg1) as arrow.Kind<BIOPartialOf<Nothing>, A>

@JvmName("catch")
@Suppress(
  "UNCHECKED_CAST",
  "USELESS_CAST",
  "EXTENSION_SHADOWED_BY_MEMBER",
  "UNUSED_PARAMETER"
)
fun <A> ApplicativeError<BIOPartialOf<Nothing>, Throwable>.catch(arg1: Function0<A>): Kind<BIOPartialOf<Nothing>, A> =
  arrow.fx.BIO.applicativeError().run {
    this@catch.catch<A>(arg1) as arrow.Kind<BIOPartialOf<Nothing>, A>
  }

@JvmName("effectCatch")
@Suppress(
  "UNCHECKED_CAST",
  "USELESS_CAST",
  "EXTENSION_SHADOWED_BY_MEMBER",
  "UNUSED_PARAMETER"
)
suspend fun <A> effectCatch(arg0: Function1<Throwable, Throwable>, arg1: suspend () -> A): Kind<BIOPartialOf<Nothing>, A> =
  arrow.fx.BIO
    .applicativeError()
    .effectCatch<A>(arg0, arg1) as arrow.Kind<BIOPartialOf<Nothing>, A>

@JvmName("effectCatch")
@Suppress(
  "UNCHECKED_CAST",
  "USELESS_CAST",
  "EXTENSION_SHADOWED_BY_MEMBER",
  "UNUSED_PARAMETER"
)
suspend fun <F, A> ApplicativeError<F, Throwable>.effectCatch(arg1: suspend () -> A): Kind<F, A> =
  arrow.fx.BIO.applicativeError().run {
    this@effectCatch.effectCatch<F, A>(arg1) as arrow.Kind<F, A>
  }

@Suppress(
  "UNCHECKED_CAST",
  "NOTHING_TO_INLINE"
)
inline fun Companion.applicativeError(): IOApplicativeError = applicativeError_singleton
