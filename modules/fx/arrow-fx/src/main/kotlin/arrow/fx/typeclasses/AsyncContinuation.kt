package arrow.fx.typeclasses

import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext
import kotlin.coroutines.RestrictsSuspension

typealias Disposable = () -> Unit

@RestrictsSuspension
interface AsyncSyntax<F> : MonadDeferSyntax<F>, Async<F>

@Suppress("DELEGATED_MEMBER_HIDES_SUPERTYPE_OVERRIDE")
open class AsyncContinuation<F, A>(val SC: Async<F>, override val context: CoroutineContext = EmptyCoroutineContext) :
  MonadDeferContinuation<F, A>(SC), Async<F> by SC, AsyncSyntax<F> {
  override val fx: AsyncFx<F> = SC.fx
}
