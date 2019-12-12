package arrow.fx.extensions.bio.dispatchers

import arrow.fx.BIO.Companion
import arrow.fx.extensions.IODispatchers
import kotlin.PublishedApi
import kotlin.Suppress
import kotlin.coroutines.CoroutineContext
import kotlin.jvm.JvmName

/**
 * cached extension
 */
@PublishedApi()
internal val dispatchers_singleton: IODispatchers = object : arrow.fx.extensions.IODispatchers {}

@JvmName("io")
@Suppress(
  "UNCHECKED_CAST",
  "USELESS_CAST",
  "EXTENSION_SHADOWED_BY_MEMBER",
  "UNUSED_PARAMETER"
)
fun io(): CoroutineContext = arrow.fx.BIO
  .dispatchers()
  .io() as kotlin.coroutines.CoroutineContext

@Suppress(
  "UNCHECKED_CAST",
  "NOTHING_TO_INLINE"
)
inline fun Companion.dispatchers(): IODispatchers = dispatchers_singleton
