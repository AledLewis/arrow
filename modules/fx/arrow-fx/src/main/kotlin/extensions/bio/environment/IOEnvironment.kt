package arrow.fx.extensions.bio.environment

import arrow.fx.BIO.Companion
import arrow.fx.BIOPartialOf
import arrow.fx.extensions.IOEnvironment
import arrow.fx.typeclasses.Dispatchers
import kotlin.PublishedApi
import kotlin.Suppress
import kotlin.jvm.JvmName

/**
 * cached extension
 */
@PublishedApi()
internal val environment_singleton: IOEnvironment = object : arrow.fx.extensions.IOEnvironment {}

@JvmName("dispatchers")
@Suppress(
  "UNCHECKED_CAST",
  "USELESS_CAST",
  "EXTENSION_SHADOWED_BY_MEMBER",
  "UNUSED_PARAMETER"
)
fun dispatchers(): Dispatchers<BIOPartialOf<Nothing>> = arrow.fx.BIO
  .environment()
  .dispatchers() as arrow.fx.typeclasses.Dispatchers<BIOPartialOf<Nothing>>

@Suppress(
  "UNCHECKED_CAST",
  "NOTHING_TO_INLINE"
)
inline fun Companion.environment(): IOEnvironment = environment_singleton
