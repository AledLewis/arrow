package arrow.fx.extensions.bio.concurrentEffect

import arrow.fx.BIO.Companion
import arrow.fx.extensions.IODefaultConcurrentEffect
import kotlin.PublishedApi
import kotlin.Suppress

/**
 * cached extension
 */
@PublishedApi()
internal val concurrentEffect_singleton: IODefaultConcurrentEffect = object :
  arrow.fx.extensions.IODefaultConcurrentEffect {}

@Suppress(
  "UNCHECKED_CAST",
  "NOTHING_TO_INLINE"
)
inline fun Companion.concurrentEffect(): IODefaultConcurrentEffect = concurrentEffect_singleton
