package arrow.fx.extensions.bio.unsafeCancellableRun

import arrow.Kind
import arrow.core.Either
import arrow.fx.BIO.Companion
import arrow.fx.BIOPartialOf
import arrow.fx.OnCancel
import arrow.fx.extensions.IOUnsafeCancellableRun
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
internal val unsafeCancellableRun_singleton: IOUnsafeCancellableRun = object :
  arrow.fx.extensions.IOUnsafeCancellableRun {}

@JvmName("runNonBlockingCancellable")
@Suppress(
  "UNCHECKED_CAST",
  "USELESS_CAST",
  "EXTENSION_SHADOWED_BY_MEMBER",
  "UNUSED_PARAMETER"
)
suspend fun <A> unsafe.runNonBlockingCancellable(
  onCancel: OnCancel,
  fa: Function0<Kind<BIOPartialOf<Nothing>, A>>,
  cb: Function1<Either<Throwable, A>, Unit>
): Function0<Unit> = arrow.fx.BIO.unsafeCancellableRun().run {
  this@runNonBlockingCancellable.runNonBlockingCancellable<A>(onCancel, fa, cb) as
    kotlin.Function0<kotlin.Unit>
}

@Suppress(
  "UNCHECKED_CAST",
  "NOTHING_TO_INLINE"
)
inline fun Companion.unsafeCancellableRun(): IOUnsafeCancellableRun = unsafeCancellableRun_singleton
