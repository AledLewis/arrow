package arrow.fx.extensions.bio.unsafeRun

import arrow.Kind
import arrow.core.Either
import arrow.fx.BIO.Companion
import arrow.fx.BIOPartialOf
import arrow.fx.extensions.IOUnsafeRun
import arrow.unsafe
import kotlin.Function0
import kotlin.Function1
import kotlin.PublishedApi
import kotlin.Suppress
import kotlin.Throwable
import kotlin.Unit
import kotlin.jvm.JvmName

/**
 * cached extension
 */
@PublishedApi()
internal val unsafeRun_singleton: IOUnsafeRun = object : arrow.fx.extensions.IOUnsafeRun {}

@JvmName("runBlocking")
@Suppress(
  "UNCHECKED_CAST",
  "USELESS_CAST",
  "EXTENSION_SHADOWED_BY_MEMBER",
  "UNUSED_PARAMETER"
)
suspend fun <A> unsafe.runBlocking(fa: Function0<Kind<BIOPartialOf<Nothing>, A>>): A = arrow.fx.BIO.unsafeRun().run {
  this@runBlocking.runBlocking<A>(fa) as A
}

@JvmName("runNonBlocking")
@Suppress(
  "UNCHECKED_CAST",
  "USELESS_CAST",
  "EXTENSION_SHADOWED_BY_MEMBER",
  "UNUSED_PARAMETER"
)
suspend fun <A> unsafe.runNonBlocking(
  fa: Function0<Kind<BIOPartialOf<Nothing>, A>>,
  cb: Function1<Either<Throwable,
    A>, Unit>
): Unit = arrow.fx.BIO.unsafeRun().run {
  this@runNonBlocking.runNonBlocking<A>(fa, cb) as kotlin.Unit
}

@Suppress(
  "UNCHECKED_CAST",
  "NOTHING_TO_INLINE"
)
inline fun Companion.unsafeRun(): IOUnsafeRun = unsafeRun_singleton
