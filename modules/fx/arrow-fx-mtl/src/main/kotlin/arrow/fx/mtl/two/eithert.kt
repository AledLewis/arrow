package arrow.fx.mtl.two

import arrow.core.Either
import arrow.core.Left
import arrow.core.None
import arrow.core.Option
import arrow.core.Right
import arrow.core.Some
import arrow.core.extensions.either.monad.flatten
import arrow.mtl.EitherT
import arrow.mtl.EitherTOf
import arrow.mtl.EitherTPartialOf
import arrow.mtl.value
import arrow.fx.Ref
import arrow.fx.typeclasses.Async
import arrow.fx.typeclasses.Bracket
import arrow.fx.typeclasses.ConcurrentEffect
import arrow.fx.typeclasses.Disposable
import arrow.fx.typeclasses.Effect
import arrow.fx.typeclasses.ExitCase
import arrow.fx.typeclasses.MonadDefer
import arrow.fx.typeclasses.Proc
import arrow.fx.typeclasses.ProcF
import arrow.extension
import arrow.fx.RacePair
import arrow.fx.RaceTriple
import arrow.fx.typeclasses.Concurrent
import arrow.fx.typeclasses.Dispatchers
import arrow.fx.typeclasses.Fiber
import arrow.mtl.fix
import arrow.typeclasses.MonadError
import arrow.undocumented
import kotlin.coroutines.CoroutineContext

@extension
@undocumented
interface EitherTMonadError2<F, L> : MonadError<EitherTPartialOf<F, L>, L> {

  fun ME(): MonadError<F, Throwable>

  override fun <A> raiseError(e: L): EitherT<F, L, A> =
    EitherT.left(ME(), e)

  override fun <A> EitherTOf<F, L, A>.handleErrorWith(f: (L) -> EitherTOf<F, L, A>): EitherT<F, L, A> = ME().run {
    fix().transformF(this) {
      when (it) {
        is Either.Left -> f(it.a).value()
        is Either.Right -> just(it)
      }
    }
  }

  override fun <A> just(a: A): EitherT<F, L, A> =
    EitherT.just(ME(), a)

  override fun <A, B> EitherTOf<F, L, A>.flatMap(f: (A) -> EitherTOf<F, L, B>): EitherT<F, L, B> =
    fix().flatMap(ME(), f)

  override fun <A, B> tailRecM(a: A, f: (A) -> EitherTOf<F, L, Either<A, B>>): EitherT<F, L, B> =
    EitherT.tailRecM(ME(), a, f)
}

@extension
@undocumented
interface EitherTBracket<F, L> : Bracket<EitherTPartialOf<F, L>, L>, EitherTMonadError2<F, L> {

  fun MDF(): MonadDefer<F>

  override fun ME(): MonadError<F, Throwable> = MDF()

  override fun <A, B> EitherTOf<F, L, A>.bracketCase(
    release: (A, ExitCase<L>) -> EitherTOf<F, L, Unit>,
    use: (A) -> EitherTOf<F, L, B>
  ): EitherT<F, L, B> = MDF().run {
    EitherT.liftF<F, L, Ref<F, Option<L>>>(this, Ref(None)).flatMap { ref ->
      value().bracketCase(use = { either ->
        when (either) {
          is Either.Right -> use(either.b).value()
          is Either.Left -> just(either)
        }
      }, release = { either, exitCase: ExitCase<Throwable> ->
        when (either) {
          is Either.Right -> when (exitCase) {
            is ExitCase.Completed -> release(either.b, ExitCase.Completed).value().flatMap {
              it.fold({ l ->
                ref.set(Some(l))
              }, {
                just(Unit)
              })
            }
            else -> release(either.b, exitCase).value().unit()
          }
          is Either.Left -> just(Unit)
        }
      })

      TODO()
    }
  }

  //   EitherT.liftF<F, L, Ref<F, Option<L>>>(this, Ref(None)).flatMap { ref ->
  //     EitherT(value().bracketCase(use = { either ->
  //       when (either) {
  //         is Either.Right -> use(either.b).value()
  //         is Either.Left -> just(either)
  //       }
  //     }, release = { either, exitCase ->
  //       when (either) {
  //         is Either.Right -> when (exitCase) {
  //           is ExitCase.Completed -> release(either.b, ExitCase.Completed).value().flatMap {
  //             it.fold({ l ->
  //               ref.set(Some(l))
  //             }, {
  //               just(Unit)
  //             })
  //           }
  //           else -> release(either.b, exitCase).value().unit()
  //         }
  //         is Either.Left -> just(Unit)
  //       }
  //     }).flatMap { either ->
  //       when (either) {
  //         is Either.Right -> ref.get().map {
  //           it.fold({ either }, { left -> Left(left) })
  //         }
  //         is Either.Left -> just(either)
  //       }
  //     })
  //   }
  // }

  // override fun <A, B> EitherTOf<F, L, A>.bracketCase(
  //   release: (A, ExitCase<Throwable>) -> EitherTOf<F, L, Unit>,
  //   use: (A) -> EitherTOf<F, L, B>
  // ): EitherT<F, L, B> = MDF().run {
  //   EitherT.liftF<F, L, Ref<F, Option<L>>>(this, Ref(None)).flatMap { ref ->
  //     EitherT(value().bracketCase(use = { either ->
  //       when (either) {
  //         is Either.Right -> use(either.b).value()
  //         is Either.Left -> just(either)
  //       }
  //     }, release = { either, exitCase ->
  //       when (either) {
  //         is Either.Right -> when (exitCase) {
  //           is ExitCase.Completed -> release(either.b, ExitCase.Completed).value().flatMap {
  //             it.fold({ l ->
  //               ref.set(Some(l))
  //             }, {
  //               just(Unit)
  //             })
  //           }
  //           else -> release(either.b, exitCase).value().unit()
  //         }
  //         is Either.Left -> just(Unit)
  //       }
  //     }).flatMap { either ->
  //       when (either) {
  //         is Either.Right -> ref.get().map {
  //           it.fold({ either }, { left -> Left(left) })
  //         }
  //         is Either.Left -> just(either)
  //       }
  //     })
  //   }
  // }
}

@extension
@undocumented
interface EitherTMonadDefer<F, L> : MonadDefer<EitherTPartialOf<F, L>>, EitherTBracket<F, L> {

  override fun MDF(): MonadDefer<F>

  override fun <A> defer(fa: () -> EitherTOf<F, L, A>): EitherT<F, L, A> =
    EitherT(MDF().defer { fa().value() })
}

@extension
@undocumented
interface EitherTAsync<F, L> : Async<EitherTPartialOf<F, L>>, EitherTMonadDefer<F, L> {

  fun ASF(): Async<F>

  override fun MDF(): MonadDefer<F> = ASF()

  override fun <A> async(fa: Proc<A>): EitherT<F, L, A> = ASF().run {
    EitherT.liftF(this, async(fa))
  }

  override fun <A> asyncF(k: ProcF<EitherTPartialOf<F, L>, A>): EitherT<F, L, A> = ASF().run {
    EitherT.liftF(this, asyncF { cb -> k(cb).value().unit() })
  }

  override fun <A> EitherTOf<F, L, A>.continueOn(ctx: CoroutineContext): EitherT<F, L, A> = ASF().run {
    EitherT(value().continueOn(ctx))
  }
}

@extension
@undocumented
interface EitherTConcurrent<F, L> : Concurrent<EitherTPartialOf<F, L>>, EitherTAsync<F, L> {

  fun CF(): Concurrent<F>

  override fun ASF(): Async<F> = CF()

  override fun dispatchers(): Dispatchers<EitherTPartialOf<F, L>> =
    CF().dispatchers() as Dispatchers<EitherTPartialOf<F, L>>

  override fun <A> EitherTOf<F, L, A>.fork(ctx: CoroutineContext): EitherT<F, L, Fiber<EitherTPartialOf<F, L>, A>> = CF().run {
    EitherT.liftF(this, value().fork(ctx).map(::fiberT))
  }

  override fun <A, B> CoroutineContext.racePair(fa: EitherTOf<F, L, A>, fb: EitherTOf<F, L, B>): EitherT<F, L, RacePair<EitherTPartialOf<F, L>, A, B>> = CF().run {
    EitherT(racePair(fa.value(), fb.value()).flatMap { racePair: RacePair<F, Either<L, A>, Either<L, B>> ->
      when (racePair) {
        is RacePair.First -> when (val winner = racePair.winner) {
          is Either.Left -> racePair.fiberB.cancel().map { Left(winner.a) }
          is Either.Right -> just(Right(RacePair.First(winner.b, fiberT(racePair.fiberB))))
        }
        is RacePair.Second -> when (val winner = racePair.winner) {
          is Either.Left -> racePair.fiberA.cancel().map { Left(winner.a) }
          is Either.Right -> just(Right(RacePair.Second(fiberT(racePair.fiberA), winner.b)))
        }
      }
    })
  }

  override fun <A, B, C> CoroutineContext.raceTriple(
    fa: EitherTOf<F, L, A>,
    fb: EitherTOf<F, L, B>,
    fc: EitherTOf<F, L, C>
  ): EitherT<F, L, RaceTriple<EitherTPartialOf<F, L>, A, B, C>> = CF().run {
    EitherT(raceTriple(fa.value(), fb.value(), fc.value()).flatMap { raceTriple: RaceTriple<F, Either<L, A>, Either<L, B>, Either<L, C>> ->
      when (raceTriple) {
        is RaceTriple.First -> when (val winner = raceTriple.winner) {
          is Either.Left -> tupled(raceTriple.fiberB.cancel(), raceTriple.fiberC.cancel()).map { Left(winner.a) }
          is Either.Right -> just(Right(RaceTriple.First(winner.b, fiberT(raceTriple.fiberB), fiberT(raceTriple.fiberC))))
        }
        is RaceTriple.Second -> when (val winner = raceTriple.winner) {
          is Either.Left -> tupled(raceTriple.fiberA.cancel(), raceTriple.fiberC.cancel()).map { Left(winner.a) }
          is Either.Right -> just(Right(RaceTriple.Second(fiberT(raceTriple.fiberA), winner.b, fiberT(raceTriple.fiberC))))
        }
        is RaceTriple.Third -> when (val winner = raceTriple.winner) {
          is Either.Left -> tupled(raceTriple.fiberA.cancel(), raceTriple.fiberB.cancel()).map { Left(winner.a) }
          is Either.Right -> just(Right(RaceTriple.Third(fiberT(raceTriple.fiberA), fiberT(raceTriple.fiberB), winner.b)))
        }
      }
    })
  }

  fun <A> fiberT(fiber: Fiber<F, Either<L, A>>): Fiber<EitherTPartialOf<F, L>, A> =
    Fiber(EitherT(fiber.join()), EitherT.liftF(ASF(), fiber.cancel()))
}

@extension
@undocumented
interface EitherTEffect<F> : Effect<EitherTPartialOf<F, Throwable>>, EitherTAsync<F, Throwable> {

  fun EFF(): Effect<F>

  override fun ASF(): Async<F> = EFF()

  override fun <A> EitherTOf<F, Throwable, A>.runAsync(cb: (Either<Throwable, A>) -> EitherTOf<F, Throwable, Unit>): EitherT<F, Throwable, Unit> = EFF().run {
    EitherT(value().runAsync { a ->
      cb(a.flatten())
        .value()
        .unit()
    }.attempt())
  }
}

@extension
@undocumented
interface EitherTConcurrentEffect<F> : ConcurrentEffect<EitherTPartialOf<F, Throwable>>, EitherTEffect<F> {

  fun CEFF(): ConcurrentEffect<F>

  override fun EFF(): Effect<F> = CEFF()

  override fun <A> EitherTOf<F, Throwable, A>.runAsyncCancellable(cb: (Either<Throwable, A>) -> EitherTOf<F, Throwable, Unit>): EitherT<F, Throwable, Disposable> = CEFF().run {
    EitherT(value().runAsyncCancellable { a ->
      cb(a.flatten())
        .value()
        .unit()
    }.attempt())
  }
}
