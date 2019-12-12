package arrow.fx.extensions

import arrow.Kind
import arrow.core.Either
import arrow.core.compose
import arrow.core.identity
import arrow.fx.BIO
import arrow.fx.BIOPartialOf
import arrow.fx.IO
import arrow.fx.IODispatchers
import arrow.fx.IOOf
import arrow.fx.OnCancel
import arrow.fx.RacePair
import arrow.fx.RaceTriple
import arrow.fx.Timer
import arrow.fx.extensions.bio.concurrent.concurrent
import arrow.fx.extensions.bio.dispatchers.dispatchers
import arrow.fx.fix
import arrow.fx.internal.toEither
import arrow.fx.toBIO
import arrow.fx.typeclasses.Async
import arrow.fx.typeclasses.Bracket
import arrow.fx.typeclasses.CancelToken
import arrow.fx.typeclasses.Concurrent
import arrow.fx.typeclasses.ConcurrentEffect
import arrow.fx.typeclasses.ConcurrentSyntax
import arrow.fx.typeclasses.Dispatchers
import arrow.fx.typeclasses.Disposable
import arrow.fx.typeclasses.Effect
import arrow.fx.typeclasses.Environment
import arrow.fx.typeclasses.ExitCase
import arrow.fx.typeclasses.Fiber
import arrow.fx.typeclasses.MonadDefer
import arrow.fx.typeclasses.Proc
import arrow.fx.typeclasses.ProcF
import arrow.fx.typeclasses.UnsafeCancellableRun
import arrow.fx.typeclasses.UnsafeRun
import arrow.fx.value
import arrow.typeclasses.Applicative
import arrow.typeclasses.ApplicativeError
import arrow.typeclasses.Apply
import arrow.typeclasses.Functor
import arrow.typeclasses.Monad
import arrow.typeclasses.MonadError
import arrow.typeclasses.MonadThrow
import arrow.typeclasses.Monoid
import arrow.typeclasses.Semigroup
import arrow.unsafe
import kotlin.coroutines.CoroutineContext
import arrow.fx.handleError as HandleError
import arrow.fx.handleErrorWith as HandleErrorWith
import arrow.fx.redeemWith as RedeemWith
import arrow.fx.redeem as Redeem
import arrow.fx.flatMap as FlatMap
import arrow.fx.ap as Ap
import arrow.fx.attemptIO as Attempt
import arrow.fx.bracket as Bracket
import arrow.fx.bracketCase as BracketCase
import arrow.fx.guarantee as Guarantee
import arrow.fx.guaranteeCase as GuaranteeCase
import arrow.fx.fork as Fork

interface IOFunctor : Functor<BIOPartialOf<Nothing>> {
  override fun <A, B> IOOf<A>.map(f: (A) -> B): IO<B> =
    fix().map(f)
}

interface IOApply : Apply<BIOPartialOf<Nothing>> {
  override fun <A, B> IOOf<A>.map(f: (A) -> B): IO<B> =
    fix().map(f)

  override fun <A, B> IOOf<A>.ap(ff: IOOf<(A) -> B>): IO<B> =
    Ap(ff)
}

interface IOApplicative : Applicative<BIOPartialOf<Nothing>> {
  override fun <A, B> IOOf<A>.map(f: (A) -> B): IO<B> =
    fix().map(f)

  override fun <A> just(a: A): IO<A> =
    IO.just(a)

  override fun <A, B> IOOf<A>.ap(ff: IOOf<(A) -> B>): IO<B> =
    Ap(ff)
}

interface IOMonad : Monad<BIOPartialOf<Nothing>> {
  override fun <A, B> IOOf<A>.flatMap(f: (A) -> IOOf<B>): IO<B> =
    FlatMap(f)

  override fun <A, B> IOOf<A>.map(f: (A) -> B): IO<B> =
    fix().map(f)

  override fun <A, B> tailRecM(a: A, f: (A) -> IOOf<Either<A, B>>): IO<B> =
    IO.tailRecM(a, f)

  override fun <A> just(a: A): IO<A> =
    IO.just(a)
}

interface IOApplicativeError : ApplicativeError<BIOPartialOf<Nothing>, Throwable>, IOApplicative {
  override fun <A> IOOf<A>.attempt(): IO<Either<Throwable, A>> =
    Attempt()

  override fun <A> IOOf<A>.handleErrorWith(f: (Throwable) -> IOOf<A>): IO<A> =
    HandleErrorWith(f)

  override fun <A> IOOf<A>.handleError(f: (Throwable) -> A): IO<A> =
    HandleError(f)

  override fun <A, B> IOOf<A>.redeem(fe: (Throwable) -> B, fb: (A) -> B): IO<B> =
    Redeem(fe, fb)

  override fun <A> raiseError(e: Throwable): IO<A> =
    IO.raiseError(e)
}

interface IOMonadError : MonadError<BIOPartialOf<Nothing>, Throwable>, IOApplicativeError, IOMonad {

  override fun <A> just(a: A): IO<A> = IO.just(a)

  override fun <A, B> IOOf<A>.ap(ff: IOOf<(A) -> B>): IO<B> =
    Ap(ff)

  override fun <A, B> IOOf<A>.map(f: (A) -> B): IO<B> =
    fix().map(f)

  override fun <A> IOOf<A>.attempt(): IO<Either<Throwable, A>> =
    Attempt()

  override fun <A> IOOf<A>.handleErrorWith(f: (Throwable) -> IOOf<A>): IO<A> =
    HandleErrorWith(f)

  override fun <A, B> IOOf<A>.redeemWith(fe: (Throwable) -> IOOf<B>, fb: (A) -> IOOf<B>): IO<B> =
    RedeemWith(fe, fb)

  override fun <A> raiseError(e: Throwable): IO<A> =
    IO.raiseError(e)
}

interface IOMonadThrow : MonadThrow<BIOPartialOf<Nothing>>, IOMonadError

interface IOBracket : Bracket<BIOPartialOf<Nothing>, Throwable>, IOMonadThrow {
  override fun <A, B> IOOf<A>.bracketCase(release: (A, ExitCase<Throwable>) -> IOOf<Unit>, use: (A) -> IOOf<B>): IO<B> =
    BracketCase(release, use)

  override fun <A, B> IOOf<A>.bracket(release: (A) -> IOOf<Unit>, use: (A) -> IOOf<B>): IO<B> =
    Bracket(release, use)

  override fun <A> IOOf<A>.guarantee(finalizer: IOOf<Unit>): IO<A> =
    Guarantee(finalizer)

  override fun <A> IOOf<A>.guaranteeCase(finalizer: (ExitCase<Throwable>) -> IOOf<Unit>): IO<A> =
    GuaranteeCase(finalizer)
}

interface IOMonadDefer : MonadDefer<BIOPartialOf<Nothing>>, IOBracket {
  override fun <A> defer(fa: () -> IOOf<A>): IO<A> =
    IO.defer(fa)

  override fun lazy(): IO<Unit> = IO.lazy
}

interface IOAsync : Async<BIOPartialOf<Nothing>>, IOMonadDefer {
  override fun <A> async(fa: Proc<A>): IO<A> =
    IO.async(fa.toBIO())

  override fun <A> asyncF(k: ProcF<BIOPartialOf<Nothing>, A>): IO<A> =
    IO.asyncF(k.toBIO())

  override fun <A> IOOf<A>.continueOn(ctx: CoroutineContext): IO<A> =
    fix().continueOn(ctx)

  override fun <A> effect(ctx: CoroutineContext, f: suspend () -> A): IO<A> =
    IO.effect(ctx, f)

  override fun <A> effect(f: suspend () -> A): IO<A> =
    IO.effect(f)
}

// FIXME default  are temporarily declared in arrow-effects-io-extensions due to multiplatform needs
interface IOConcurrent : Concurrent<BIOPartialOf<Nothing>>, IOAsync {
  override fun <A> Kind<BIOPartialOf<Nothing>, A>.fork(coroutineContext: CoroutineContext): IO<Fiber<BIOPartialOf<Nothing>, A>> =
    Fork(coroutineContext)

  override fun <A> cancelable(k: ((Either<Throwable, A>) -> Unit) -> CancelToken<BIOPartialOf<Nothing>>): Kind<BIOPartialOf<Nothing>, A> =
    IO.cancelable(k.toBIO())

  override fun <A> cancelableF(k: ((Either<Throwable, A>) -> Unit) -> IOOf<CancelToken<BIOPartialOf<Nothing>>>): IO<A> =
    IO.cancelableF(k.toBIO())

  override fun <A, B> CoroutineContext.racePair(fa: Kind<BIOPartialOf<Nothing>, A>, fb: Kind<BIOPartialOf<Nothing>, B>): IO<RacePair<BIOPartialOf<Nothing>, A, B>> =
    IO.racePair(this, fa, fb)

  override fun <A, B, C> CoroutineContext.raceTriple(fa: Kind<BIOPartialOf<Nothing>, A>, fb: Kind<BIOPartialOf<Nothing>, B>, fc: Kind<BIOPartialOf<Nothing>, C>): IO<RaceTriple<BIOPartialOf<Nothing>, A, B, C>> =
    IO.raceTriple(this, fa, fb, fc)

  override fun <A, B, C> CoroutineContext.parMapN(fa: Kind<BIOPartialOf<Nothing>, A>, fb: Kind<BIOPartialOf<Nothing>, B>, f: (A, B) -> C): Kind<BIOPartialOf<Nothing>, C> =
    IO.parMapN(this@parMapN, fa, fb, f)

  override fun <A, B, C, D> CoroutineContext.parMapN(fa: Kind<BIOPartialOf<Nothing>, A>, fb: Kind<BIOPartialOf<Nothing>, B>, fc: Kind<BIOPartialOf<Nothing>, C>, f: (A, B, C) -> D): Kind<BIOPartialOf<Nothing>, D> =
    IO.parMapN(this@parMapN, fa, fb, fc, f)
}

fun BIO.Companion.concurrent(dispatchers: Dispatchers<BIOPartialOf<Nothing>>): Concurrent<BIOPartialOf<Nothing>> = object : IOConcurrent {
  override fun dispatchers(): Dispatchers<BIOPartialOf<Nothing>> = dispatchers
}

fun BIO.Companion.timer(CF: Concurrent<BIOPartialOf<Nothing>>): Timer<BIOPartialOf<Nothing>> =
  Timer(CF)

interface IOEffect : Effect<BIOPartialOf<Nothing>>, IOAsync {
  override fun <A> IOOf<A>.runAsync(cb: (Either<Throwable, A>) -> IOOf<Unit>): IO<Unit> =
    fix().runAsync(cb.compose { it.toEither() })
}

// FIXME default  are temporarily declared in arrow-effects-io-extensions due to multiplatform needs
interface IOConcurrentEffect : ConcurrentEffect<BIOPartialOf<Nothing>>, IOEffect, IOConcurrent {

  override fun <A> IOOf<A>.runAsyncCancellable(cb: (Either<Throwable, A>) -> IOOf<Unit>): IO<Disposable> =
    fix().runAsyncCancellable(OnCancel.ThrowCancellationException, cb.compose { it.toEither() })
}

fun BIO.Companion.concurrentEffect(dispatchers: Dispatchers<BIOPartialOf<Nothing>>): ConcurrentEffect<BIOPartialOf<Nothing>> = object : IOConcurrentEffect {
  override fun dispatchers(): Dispatchers<BIOPartialOf<Nothing>> = dispatchers
}

interface IOSemigroup<A> : Semigroup<IO<A>> {

  fun SG(): Semigroup<A>

  override fun IO<A>.combine(b: IO<A>): IO<A> =
    FlatMap { a1: A -> b.map { a2: A -> SG().run { a1.combine(a2) } } }
}

interface IOMonoid<A> : Monoid<IO<A>>, IOSemigroup<A> {

  override fun SG(): Semigroup<A> = SM()

  fun SM(): Monoid<A>

  override fun empty(): IO<A> = IO.just(SM().empty())
}

interface IOUnsafeRun : UnsafeRun<BIOPartialOf<Nothing>> {

  override suspend fun <A> unsafe.runBlocking(fa: () -> Kind<BIOPartialOf<Nothing>, A>): A =
    fa().fix().unsafeRunSync().value()

  override suspend fun <A> unsafe.runNonBlocking(fa: () -> Kind<BIOPartialOf<Nothing>, A>, cb: (Either<Throwable, A>) -> Unit) =
    fa().fix().unsafeRunAsync(cb.compose { it.toEither() })
}

interface IOUnsafeCancellableRun : UnsafeCancellableRun<BIOPartialOf<Nothing>> {
  override suspend fun <A> unsafe.runBlocking(fa: () -> Kind<BIOPartialOf<Nothing>, A>): A =
    fa().fix().unsafeRunSync().value()

  override suspend fun <A> unsafe.runNonBlocking(fa: () -> Kind<BIOPartialOf<Nothing>, A>, cb: (Either<Throwable, A>) -> Unit) =
    fa().fix().unsafeRunAsync(cb.compose { it.toEither() })

  override suspend fun <A> unsafe.runNonBlockingCancellable(onCancel: OnCancel, fa: () -> Kind<BIOPartialOf<Nothing>, A>, cb: (Either<Throwable, A>) -> Unit): Disposable =
    fa().fix().unsafeRunAsyncCancellable(onCancel, cb.compose { it.toEither() })
}

interface IODispatchers : Dispatchers<BIOPartialOf<Nothing>> {
  override fun default(): CoroutineContext =
    IODispatchers.CommonPool

  override fun io(): CoroutineContext =
    IODispatchers.IOPool
}

interface IOEnvironment : Environment<BIOPartialOf<Nothing>> {
  override fun dispatchers(): Dispatchers<BIOPartialOf<Nothing>> =
    IO.dispatchers()

  override fun handleAsyncError(e: Throwable): IO<Unit> =
    IO { println("Found uncaught async exception!"); e.printStackTrace() }
}

interface IODefaultConcurrent : Concurrent<BIOPartialOf<Nothing>>, IOConcurrent {

  override fun dispatchers(): Dispatchers<BIOPartialOf<Nothing>> =
    IO.dispatchers()
}

fun BIO.Companion.timer(): Timer<BIOPartialOf<Nothing>> = Timer(IO.concurrent())

interface IODefaultConcurrentEffect : ConcurrentEffect<BIOPartialOf<Nothing>>, IOConcurrentEffect, IODefaultConcurrent

fun <A> BIO.Companion.fx(c: suspend ConcurrentSyntax<BIOPartialOf<Nothing>>.() -> A): IO<A> =
  defer { IO.concurrent().fx.concurrent(c).fix() }

/**
 * converts this Either to an IO. The resulting IO will evaluate to this Eithers
 * Right value or alternatively to the result of applying the specified function to this Left value.
 */
fun <E, A> Either<E, A>.toIO(f: (E) -> Throwable): IO<A> =
  fold({ IO.raiseError(f(it)) }, { IO.just(it) })

/**
 * converts this Either to an IO. The resulting IO will evaluate to this Eithers
 * Right value or Left exception.
 */
fun <A> Either<Throwable, A>.toIO(): IO<A> =
  toIO(::identity)
