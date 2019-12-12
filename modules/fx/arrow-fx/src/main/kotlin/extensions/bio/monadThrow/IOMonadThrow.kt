package arrow.fx.extensions.bio.monadThrow

import arrow.Kind
import arrow.fx.BIO.Companion
import arrow.fx.BIOPartialOf
import arrow.fx.extensions.IOMonadThrow
import kotlin.PublishedApi
import kotlin.Suppress
import kotlin.Throwable
import kotlin.jvm.JvmName

/**
 * cached extension
 */
@PublishedApi()
internal val monadThrow_singleton: IOMonadThrow = object : arrow.fx.extensions.IOMonadThrow {}

@JvmName("raiseNonFatal")
@Suppress(
  "UNCHECKED_CAST",
  "USELESS_CAST",
  "EXTENSION_SHADOWED_BY_MEMBER",
  "UNUSED_PARAMETER"
)
fun <A> Throwable.raiseNonFatal(): Kind<BIOPartialOf<Nothing>, A> = arrow.fx.BIO.monadThrow().run {
  this@raiseNonFatal.raiseNonFatal<A>() as arrow.Kind<BIOPartialOf<Nothing>, A>
}

/**
 *  ank_macro_hierarchy(arrow.typeclasses.MonadThrow)
 *
 *  MonadThrow has the error type fixed to Throwable. It provides [fx.monadThrow] for automatically catching throwable
 *  errors in the context of a binding, short-circuiting the complete computation and returning the error raised to the
 *  same computational context (through [raiseError]).
 *
 *  ```kotlin:ank:playground
 *  import arrow.fx.*
 * import arrow.fx.extensions.bio.monadThrow.*
 * import arrow.core.*
 *
 *
 *
 *  fun main(args: Array<String>) {
 *   val result =
 *   //sampleStart
 *   BIO.monadThrow<String>()
 *   //sampleEnd
 *   println(result)
 *  }
 *  ```
 *
 *  ### Example
 *
 *  Oftentimes we find ourselves in situations where we need to sequence some computations that could potentially fail.
 *  [fx.monadThrow] allows us to safely compute those by automatically catching any exceptions thrown during the process.
 *
 *  ```kotlin:ank:playground
 *  import arrow.fx.*
 * import arrow.fx.extensions.bio.monadThrow.*
 * import arrow.core.*
 *
 *
 *  import arrow.Kind
 *  import arrow.typeclasses.MonadThrow
 *
 *  typealias Impacted = Boolean
 *
 *  object Nuke
 *  object Target
 *  class MissedByMeters(private val meters: Int) : Throwable("Missed by $meters meters")
 *
 *  fun <F> MonadThrow<F>.arm(): Kind<F, Nuke> = just(Nuke)
 *  fun <F> MonadThrow<F>.aim(): Kind<F, Target> = just(Target)
 *  fun <F> MonadThrow<F>.launchImpure(target: Target, nuke: Nuke): Impacted {
 *   throw MissedByMeters(5)
 *  }
 *
 *  fun main(args: Array<String>) {
 *    //sampleStart
 *    fun <F> MonadThrow<F>.attack(): Kind<F, Impacted> =
 *      fx.monadThrow {
 *        val nuke = arm().bind()
 *        val target = aim().bind()
 *        val impact = launchImpure(target, nuke) // this throws!
 *        impact
 *      }
 *
 *    val result = BIO.monadThrow<String>().attack()
 *    //sampleEnd
 *    println(result)
 *  }
 *  ```
 */
@Suppress(
  "UNCHECKED_CAST",
  "NOTHING_TO_INLINE"
)
inline fun Companion.monadThrow(): IOMonadThrow = monadThrow_singleton
