package arrow.fx.extensions.bio.monadDefer

import arrow.Kind
import arrow.core.Either
import arrow.fx.BIO.Companion
import arrow.fx.BIOPartialOf
import arrow.fx.Ref
import arrow.fx.extensions.IOMonadDefer
import kotlin.Function0
import kotlin.PublishedApi
import kotlin.Suppress
import kotlin.Throwable
import kotlin.jvm.JvmName

/**
 * cached extension
 */
@PublishedApi()
internal val monadDefer_singleton: IOMonadDefer = object : arrow.fx.extensions.IOMonadDefer {}

@JvmName("later")
@Suppress(
  "UNCHECKED_CAST",
  "USELESS_CAST",
  "EXTENSION_SHADOWED_BY_MEMBER",
  "UNUSED_PARAMETER"
)
fun <A> later(f: Function0<A>): Kind<BIOPartialOf<Nothing>, A> = arrow.fx.BIO
  .monadDefer()
  .later<A>(f) as arrow.Kind<BIOPartialOf<Nothing>, A>

@JvmName("later")
@Suppress(
  "UNCHECKED_CAST",
  "USELESS_CAST",
  "EXTENSION_SHADOWED_BY_MEMBER",
  "UNUSED_PARAMETER"
)
fun <A> later(fa: Kind<BIOPartialOf<Nothing>, A>): Kind<BIOPartialOf<Nothing>, A> = arrow.fx.BIO
  .monadDefer()
  .later<A>(fa) as arrow.Kind<BIOPartialOf<Nothing>, A>

@JvmName("laterOrRaise")
@Suppress(
  "UNCHECKED_CAST",
  "USELESS_CAST",
  "EXTENSION_SHADOWED_BY_MEMBER",
  "UNUSED_PARAMETER"
)
fun <A> laterOrRaise(f: Function0<Either<Throwable, A>>): Kind<BIOPartialOf<Nothing>, A> = arrow.fx.BIO
  .monadDefer()
  .laterOrRaise<A>(f) as arrow.Kind<BIOPartialOf<Nothing>, A>

@JvmName("Ref")
@Suppress(
  "UNCHECKED_CAST",
  "USELESS_CAST",
  "EXTENSION_SHADOWED_BY_MEMBER",
  "UNUSED_PARAMETER"
)
fun <A> Ref(a: A): Kind<BIOPartialOf<Nothing>, Ref<BIOPartialOf<Nothing>, A>> = arrow.fx.BIO
  .monadDefer()
  .Ref<A>(a) as arrow.Kind<BIOPartialOf<Nothing>, arrow.fx.Ref<BIOPartialOf<Nothing>, A>>

@Suppress(
  "UNCHECKED_CAST",
  "NOTHING_TO_INLINE"
)
inline fun Companion.monadDefer(): IOMonadDefer = monadDefer_singleton
