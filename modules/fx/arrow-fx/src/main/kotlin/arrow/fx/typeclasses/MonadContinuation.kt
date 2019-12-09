package arrow.fx.typeclasses

import arrow.typeclasses.MonadContinuation
import arrow.typeclasses.MonadSyntax
import arrow.typeclasses.MonadThrowFx
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext
import kotlin.coroutines.RestrictsSuspension

@RestrictsSuspension
interface MonadDeferSyntax<F> : MonadSyntax<F>, MonadDefer<F>

@Suppress("DELEGATED_MEMBER_HIDES_SUPERTYPE_OVERRIDE")
open class MonadDeferContinuation<F, A>(ME: MonadDefer<F>, override val context: CoroutineContext = EmptyCoroutineContext) :
  MonadContinuation<F, A>(ME), MonadDefer<F> by ME, MonadDeferSyntax<F> {

  override val fx: MonadThrowFx<F> = ME.fx

  @Suppress("UNCHECKED_CAST")
  override fun resumeWithException(exception: Throwable) {
    returnedMonad = raiseError(exception)
  }
}
