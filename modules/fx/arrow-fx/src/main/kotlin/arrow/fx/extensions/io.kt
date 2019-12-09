package arrow.fx.extensions

import arrow.Kind
import arrow.core.Either
import arrow.core.identity
import arrow.extension
import arrow.fx.CancelToken
import arrow.fx.IO
import arrow.fx.IODispatchers
import arrow.fx.IOOf
import arrow.fx.IOPartialOf
import arrow.fx.OnCancel
import arrow.fx.RacePair
import arrow.fx.RaceTriple
import arrow.fx.Timer
import arrow.fx.extensions.io.concurrent.concurrent
import arrow.fx.extensions.io.dispatchers.dispatchers
import arrow.fx.fix
import arrow.fx.flatMap
import arrow.fx.runAsyncCancellable
import arrow.fx.typeclasses.Async
import arrow.fx.typeclasses.Bracket
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
import arrow.fx.ap as Ap
import arrow.fx.flatMap as FlatMap
import arrow.fx.redeemWith as RedeemWith
import arrow.fx.bracket as Bracket
import arrow.fx.bracketCase as BracketCase
import arrow.fx.guarantee as Guarantee
import arrow.fx.guaranteeCase as GuaranteeCase
import arrow.fx.fork as Fork

@extension
interface IOFunctor : Functor<IOPartialOf<Throwable>> {
  override fun <A, B> IOOf<Throwable, A>.map(f: (A) -> B): IO<Throwable, B> =
    fix().map(f)
}

@extension
interface IOApply : Apply<IOPartialOf<Throwable>> {
  override fun <A, B> IOOf<Throwable, A>.map(f: (A) -> B): IO<Throwable, B> =
    fix().map(f)

  override fun <A, B> IOOf<Throwable, A>.ap(ff: IOOf<Throwable, (A) -> B>): IO<Throwable, B> =
    Ap(ff)
}

@extension
interface IOApplicative : Applicative<IOPartialOf<Throwable>> {
  override fun <A, B> IOOf<Throwable, A>.map(f: (A) -> B): IO<Throwable, B> =
    fix().map(f)

  override fun <A> just(a: A): IO<Throwable, A> =
    IO.just(a)

  override fun <A, B> IOOf<Throwable, A>.ap(ff: IOOf<Throwable, (A) -> B>): IO<Throwable, B> =
    Ap(ff)
}

@extension
interface IOMonad : Monad<IOPartialOf<Throwable>> {
  override fun <A, B> IOOf<Throwable, A>.flatMap(f: (A) -> IOOf<Throwable, B>): IO<Throwable, B> =
    FlatMap(f)

  override fun <A, B> IOOf<Throwable, A>.map(f: (A) -> B): IO<Throwable, B> =
    fix().map(f)

  override fun <A, B> tailRecM(a: A, f: (A) -> IOOf<Throwable, Either<A, B>>): IO<Throwable, B> =
    IO.tailRecM(a, f)

  override fun <A> just(a: A): IO<Throwable, A> =
    IO.just(a)
}

@extension
interface IOApplicativeError : ApplicativeError<IOPartialOf<Throwable>, Throwable>, IOApplicative {
  override fun <A> IOOf<Throwable, A>.attempt(): IO<Throwable, Either<Throwable, A>> =
    fix().attempt()

  override fun <A> IOOf<Throwable, A>.handleErrorWith(f: (Throwable) -> IOOf<Throwable, A>): IO<Throwable, A> =
    HandleErrorWith(f)

  override fun <A> IOOf<Throwable, A>.handleError(f: (Throwable) -> A): IO<Throwable, A> =
    HandleError(f)

  override fun <A, B> IOOf<Throwable, A>.redeem(fe: (Throwable) -> B, fb: (A) -> B): IO<Throwable, B> =
    fix().redeem(fe, fb)

  override fun <A> raiseError(e: Throwable): IO<Throwable, A> =
    IO.raiseError(e)
}

@extension
interface IOMonadError : MonadError<IOPartialOf<Throwable>, Throwable>, IOApplicativeError, IOMonad {

  override fun <A> just(a: A): IO<Throwable, A> = IO.just(a)

  override fun <A, B> IOOf<Throwable, A>.ap(ff: IOOf<Throwable, (A) -> B>): IO<Throwable, B> =
    Ap(ff)

  override fun <A, B> IOOf<Throwable, A>.map(f: (A) -> B): IO<Throwable, B> =
    fix().map(f)

  override fun <A> IOOf<Throwable, A>.attempt(): IO<Throwable, Either<Throwable, A>> =
    fix().attempt()

  override fun <A> IOOf<Throwable, A>.handleErrorWith(f: (Throwable) -> IOOf<Throwable, A>): IO<Throwable, A> =
    HandleErrorWith(f)

  override fun <A, B> IOOf<Throwable, A>.redeemWith(fe: (Throwable) -> IOOf<Throwable, B>, fb: (A) -> IOOf<Throwable, B>): IO<Throwable, B> =
    RedeemWith(fe, fb)

  override fun <A> raiseError(e: Throwable): IO<Throwable, A> =
    IO.raiseError(e)
}

@extension
interface IOMonadThrow : MonadThrow<IOPartialOf<Throwable>>, IOMonadError

@extension
interface IOBracket : Bracket<IOPartialOf<Throwable>, Throwable>, IOMonadThrow {
  override fun <A, B> IOOf<Throwable, A>.bracketCase(release: (A, ExitCase<Throwable>) -> IOOf<Throwable, Unit>, use: (A) -> IOOf<Throwable, B>): IO<Throwable, B> =
    BracketCase(release, use)

  override fun <A, B> IOOf<Throwable, A>.bracket(release: (A) -> IOOf<Throwable, Unit>, use: (A) -> IOOf<Throwable, B>): IO<Throwable, B> =
    Bracket(release, use)

  override fun <A> IOOf<Throwable, A>.guarantee(finalizer: IOOf<Throwable, Unit>): IO<Throwable, A> =
    Guarantee(finalizer)

  override fun <A> IOOf<Throwable, A>.guaranteeCase(finalizer: (ExitCase<Throwable>) -> IOOf<Throwable, Unit>): IO<Throwable, A> =
    GuaranteeCase(finalizer)
}

@extension
interface IOMonadDefer : MonadDefer<IOPartialOf<Throwable>>, IOBracket {
  override fun <A> defer(fa: () -> IOOf<Throwable, A>): IO<Throwable, A> =
    IO.defer(fa)

  override fun lazy(): IO<Throwable, Unit> = IO.lazy
}

@extension
interface IOAsync : Async<IOPartialOf<Throwable>>, IOMonadDefer {
  override fun <A> async(fa: Proc<A>): IO<Throwable, A> =
    IO.async(fa)

  override fun <A> asyncF(k: ProcF<IOPartialOf<Throwable>, A>): IO<Throwable, A> =
    IO.asyncF(k)

  override fun <A> IOOf<Throwable, A>.continueOn(ctx: CoroutineContext): IO<Throwable, A> =
    fix().continueOn(ctx)

  override fun <A> effect(ctx: CoroutineContext, f: suspend () -> A): IO<Throwable, A> =
    IO.effect(ctx, f)

  override fun <A> effect(f: suspend () -> A): IO<Throwable, A> =
    IO.effect(f)
}

// FIXME default @extension are temporarily declared in arrow-effects-io-extensions due to multiplatform needs
interface IOConcurrent : Concurrent<IOPartialOf<Throwable>>, IOAsync {

  override fun <A> Kind<IOPartialOf<Throwable>, A>.fork(coroutineContext: CoroutineContext): IO<Throwable, Fiber<IOPartialOf<Throwable>, A>> =
    Fork(coroutineContext)

  override fun <A> cancelable(k: ((Either<Throwable, A>) -> Unit) -> CancelToken<IOPartialOf<Throwable>>): Kind<IOPartialOf<Throwable>, A> =
    IO.cancelable(k)

  override fun <A> cancelableF(k: ((Either<Throwable, A>) -> Unit) -> IOOf<Throwable, CancelToken<IOPartialOf<Throwable>>>): IO<Throwable, A> =
    IO.cancelableF(k)

  override fun <A, B> CoroutineContext.racePair(fa: Kind<IOPartialOf<Throwable>, A>, fb: Kind<IOPartialOf<Throwable>, B>): IO<Throwable, RacePair<IOPartialOf<Throwable>, A, B>> =
    IO.racePair(this, fa, fb)

  override fun <A, B, C> CoroutineContext.raceTriple(fa: Kind<IOPartialOf<Throwable>, A>, fb: Kind<IOPartialOf<Throwable>, B>, fc: Kind<IOPartialOf<Throwable>, C>): IO<Throwable, RaceTriple<IOPartialOf<Throwable>, A, B, C>> =
    IO.raceTriple(this, fa, fb, fc)

  override fun <A, B, C> CoroutineContext.parMapN(fa: Kind<IOPartialOf<Throwable>, A>, fb: Kind<IOPartialOf<Throwable>, B>, f: (A, B) -> C): Kind<IOPartialOf<Throwable>, C> =
    IO.parMapN(this@parMapN, fa, fb, f)

  override fun <A, B, C, D> CoroutineContext.parMapN(fa: Kind<IOPartialOf<Throwable>, A>, fb: Kind<IOPartialOf<Throwable>, B>, fc: Kind<IOPartialOf<Throwable>, C>, f: (A, B, C) -> D): Kind<IOPartialOf<Throwable>, D> =
    IO.parMapN(this@parMapN, fa, fb, fc, f)
}

fun IO.Companion.concurrent(dispatchers: Dispatchers<IOPartialOf<Throwable>>): Concurrent<IOPartialOf<Throwable>> = object : IOConcurrent {
  override fun dispatchers(): Dispatchers<IOPartialOf<Throwable>> = dispatchers
}

fun IO.Companion.timer(CF: Concurrent<IOPartialOf<Throwable>>): Timer<IOPartialOf<Throwable>> =
  Timer(CF)

@extension
interface IOEffect : Effect<IOPartialOf<Throwable>>, IOAsync {
  override fun <A> IOOf<Throwable, A>.runAsync(cb: (Either<Throwable, A>) -> IOOf<Throwable, Unit>): IO<Throwable, Unit> =
    fix().runAsync(cb)
}

// FIXME default @extension are temporarily declared in arrow-effects-io-extensions due to multiplatform needs
interface IOConcurrentEffect : ConcurrentEffect<IOPartialOf<Throwable>>, IOEffect, IOConcurrent {

  override fun <A> IOOf<Throwable, A>.runAsyncCancellable(cb: (Either<Throwable, A>) -> IOOf<Throwable, Unit>): IO<Throwable, Disposable> =
    fix().runAsyncCancellable(OnCancel.ThrowCancellationException, cb)
}

fun IO.Companion.concurrentEffect(dispatchers: Dispatchers<IOPartialOf<Throwable>>): ConcurrentEffect<IOPartialOf<Throwable>> = object : IOConcurrentEffect {
  override fun dispatchers(): Dispatchers<IOPartialOf<Throwable>> = dispatchers
}

@extension
interface IOSemigroup<A> : Semigroup<IO<Throwable, A>> {

  fun SG(): Semigroup<A>

  override fun IO<Throwable, A>.combine(b: IO<Throwable, A>): IO<Throwable, A> =
    flatMap { a1: A -> b.map { a2: A -> SG().run { a1.combine(a2) } } }
}

@extension
interface IOMonoid<A> : Monoid<IO<Throwable, A>>, IOSemigroup<A> {

  override fun SG(): Semigroup<A> = SM()

  fun SM(): Monoid<A>

  override fun empty(): IO<Throwable, A> = IO.just(SM().empty())
}

@extension
interface IOUnsafeRun : UnsafeRun<IOPartialOf<Throwable>> {

  override suspend fun <A> unsafe.runBlocking(fa: () -> Kind<IOPartialOf<Throwable>, A>): A = fa().fix().unsafeRunSync()

  override suspend fun <A> unsafe.runNonBlocking(fa: () -> Kind<IOPartialOf<Throwable>, A>, cb: (Either<Throwable, A>) -> Unit) =
    fa().fix().unsafeRunAsync(cb)
}

@extension
interface IOUnsafeCancellableRun : UnsafeCancellableRun<IOPartialOf<Throwable>> {
  override suspend fun <A> unsafe.runBlocking(fa: () -> Kind<IOPartialOf<Throwable>, A>): A = fa().fix().unsafeRunSync()

  override suspend fun <A> unsafe.runNonBlocking(fa: () -> Kind<IOPartialOf<Throwable>, A>, cb: (Either<Throwable, A>) -> Unit) =
    fa().fix().unsafeRunAsync(cb)

  override suspend fun <A> unsafe.runNonBlockingCancellable(onCancel: OnCancel, fa: () -> Kind<IOPartialOf<Throwable>, A>, cb: (Either<Throwable, A>) -> Unit): Disposable =
    fa().fix().unsafeRunAsyncCancellable(onCancel, cb)
}

@extension
interface IODispatchers : Dispatchers<IOPartialOf<Throwable>> {
  override fun default(): CoroutineContext =
    IODispatchers.CommonPool

  override fun io(): CoroutineContext =
    IODispatchers.IOPool
}

@extension
interface IOEnvironment : Environment<IOPartialOf<Throwable>> {
  override fun dispatchers(): Dispatchers<IOPartialOf<Throwable>> =
    IO.dispatchers()

  override fun handleAsyncError(e: Throwable): IO<Throwable, Unit> =
    IO { println("Found uncaught async exception!"); e.printStackTrace() }
}

@extension
interface IODefaultConcurrent : Concurrent<IOPartialOf<Throwable>>, IOConcurrent {

  override fun dispatchers(): Dispatchers<IOPartialOf<Throwable>> =
    IO.dispatchers()
}

fun IO.Companion.timer(): Timer<IOPartialOf<Throwable>> = Timer(IO.concurrent())

@extension
interface IODefaultConcurrentEffect : ConcurrentEffect<IOPartialOf<Throwable>>, IOConcurrentEffect, IODefaultConcurrent

fun <A> IO.Companion.fx(c: suspend ConcurrentSyntax<IOPartialOf<Throwable>>.() -> A): IO<Throwable, A> =
  defer { IO.concurrent().fx.concurrent(c).fix() }

/**
 * converts this Either to an IO. The resulting IO will evaluate to this Eithers
 * Right value or alternatively to the result of applying the specified function to this Left value.
 */
fun <E, A> Either<E, A>.toIO(f: (E) -> Throwable): IO<Throwable, A> =
  fold({ IO.raiseError(f(it)) }, { IO.just(it) })

/**
 * converts this Either to an IO. The resulting IO will evaluate to this Eithers
 * Right value or Left exception.
 */
fun <A> Either<Throwable, A>.toIO(): IO<Throwable, A> =
  toIO(::identity)
