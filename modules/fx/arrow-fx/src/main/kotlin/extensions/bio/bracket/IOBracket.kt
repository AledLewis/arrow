package arrow.fx.extensions.bio.bracket

import arrow.Kind
import arrow.fx.BIO.Companion
import arrow.fx.BIOPartialOf
import arrow.fx.extensions.IOBracket
import kotlin.PublishedApi
import kotlin.Suppress
import kotlin.jvm.JvmName

/**
 * cached extension
 */
@PublishedApi()
internal val bracket_singleton: IOBracket = object : arrow.fx.extensions.IOBracket {}

/**
 *  Meant for ensuring a given task continues execution even when interrupted.
 */
@JvmName("uncancelable")
@Suppress(
  "UNCHECKED_CAST",
  "USELESS_CAST",
  "EXTENSION_SHADOWED_BY_MEMBER",
  "UNUSED_PARAMETER"
)
fun <A> Kind<BIOPartialOf<Nothing>, A>.uncancelable(): Kind<BIOPartialOf<Nothing>, A> = arrow.fx.BIO.bracket().run {
  this@uncancelable.uncancelable<A>() as arrow.Kind<BIOPartialOf<Nothing>, A>
}

/**
 *  ank_macro_hierarchy(arrow.fx.typeclasses.Bracket)
 *
 *  Extension of MonadError exposing the [bracket] operation, a generalized abstracted pattern of safe resource
 *  acquisition and release in the face of errors or interruption.
 *
 *  @define The functions receiver here (Kind<F, A>) would stand for the "acquireParam", and stands for an action that
 *  "acquires" some expensive resource, that needs to be used and then discarded.
 *
 *  @define use is the action that uses the newly allocated resource and that will provide the final result.
 */
@Suppress(
  "UNCHECKED_CAST",
  "NOTHING_TO_INLINE"
)
inline fun Companion.bracket(): IOBracket = bracket_singleton
