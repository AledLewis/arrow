package arrow.fx.extensions.bio.effect

import arrow.fx.BIO.Companion
import arrow.fx.extensions.IOEffect
import kotlin.PublishedApi
import kotlin.Suppress

/**
 * cached extension
 */
@PublishedApi()
internal val effect_singleton: IOEffect = object : arrow.fx.extensions.IOEffect {}

@Suppress(
  "UNCHECKED_CAST",
  "NOTHING_TO_INLINE"
)
inline fun Companion.effect(): IOEffect = effect_singleton
