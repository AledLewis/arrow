package arrow.fx

import arrow.core.internal.AtomicBooleanW
import arrow.fx.internal.BIOFiber
import arrow.fx.internal.BIOForkedStart
import arrow.fx.internal.BIOResult
import arrow.fx.internal.Platform
import arrow.fx.internal.UnsafePromise
import arrow.fx.typeclasses.Fiber
import kotlin.coroutines.CoroutineContext

/** Mix-in to enable `parMapN` 2-arity on IO's companion directly. */
interface IORaceTriple {

  /**
   * Race three tasks concurrently within a new [IO].
   * Race results in a winner and the others, yet to finish task running in a [Fiber].
   *
   * ```kotlin:ank:playground
   * import arrow.fx.*
   * import arrow.fx.extensions.fx
   * import kotlinx.coroutines.Dispatchers
   *
   * fun main(args: Array<String>) {
   *   //sampleStart
   *   val result = IO.fx {
   *     val raceResult = !IO.raceTriple(Dispatchers.Default, never<Int>(), just("Hello World!"), never<Double>())
   *     raceResult.fold(
   *       { _, _, _ -> "never cannot win before complete" },
   *       { _, winner, _ -> winner },
   *       { _, _, _ -> "never cannot win before complete" }
   *     )
   *   }
   *   //sampleEnd
   *
   *   val r = result.unsafeRunSync()
   *   println("Race winner result is: $r")
   * }
   * ```
   *
   * @param ctx [CoroutineContext] to execute the source [IO] on.
   * @param ioA task to participate in the race
   * @param ioB task to participate in the race
   * @param ioC task to participate in the race
   * @return [RaceTriple]
   *
   * @see [arrow.fx.typeclasses.Concurrent.raceN] for a simpler version that cancels losers.
   */
  fun <E, A, B, C> raceTriple(ctx: CoroutineContext, ioA: BIOOf<E, A>, ioB: BIOOf<E, B>, ioC: BIOOf<E, C>): BIO<E, RaceTriple<BIOPartialOf<E>, A, B, C>> =
    BIO.Async { conn, cb ->
      val active = AtomicBooleanW(true)

      val upstreamCancelToken = IO.defer { if (conn.isCanceled()) IO.unit else conn.cancel() }

      val connA = IOConnection()
      connA.push(upstreamCancelToken)
      val promiseA = UnsafePromise<E, A>()

      val connB = IOConnection()
      connB.push(upstreamCancelToken)
      val promiseB = UnsafePromise<E, B>()

      val connC = IOConnection()
      connC.push(upstreamCancelToken)
      val promiseC = UnsafePromise<E, C>()

      conn.push(connA.cancel(), connB.cancel(), connC.cancel())

      IORunLoop.startCancelable(BIOForkedStart(ioA, ctx), connA) { either: BIOResult<E, A> ->
        either.fold({ error ->
          if (active.getAndSet(false)) { // if an error finishes first, stop the race.
            connB.cancel().fix().unsafeRunAsync { r2 ->
              connC.cancel().fix().unsafeRunAsync { r3 ->
                conn.pop()
                val errorResult = r2.fold({ e2 ->
                  r3.fold({ e3 -> Platform.composeErrors(error, e2, e3) }, { Platform.composeErrors(error, e2) }, { error })
                }, {
                  r3.fold({ e3 -> Platform.composeErrors(error, e3) }, { error }, { error })
                }, { error })
                cb(BIOResult.Error(errorResult))
              }
            }
          } else {
            promiseA.complete(BIOResult.Error(error))
          }
        }, { e ->
          if (active.getAndSet(false)) { // if an error finishes first, stop the race.
            connB.cancel().fix().unsafeRunAsync { r2 ->
              connC.cancel().fix().unsafeRunAsync { r3 ->
                conn.pop()
                cb(BIOResult.Left(e))
              }
            }
          } else {
            promiseA.complete(BIOResult.Left(e))
          }
        }, { a ->
          if (active.getAndSet(false)) {
            conn.pop()
            cb(BIOResult.Right(RaceTriple.First(a, BIOFiber(promiseB, connB), BIOFiber(promiseC, connC))))
          } else {
            promiseA.complete(BIOResult.Right(a))
          }
        })
      }

      IORunLoop.startCancelable(BIOForkedStart(ioB, ctx), connB) { either: BIOResult<E, B> ->
        either.fold({ error ->
          if (active.getAndSet(false)) { // if an error finishes first, stop the race.
            connA.cancel().fix().unsafeRunAsync { r2 ->
              connC.cancel().fix().unsafeRunAsync { r3 ->
                conn.pop()
                val errorResult = r2.fold({ e2 ->
                  r3.fold({ e3 -> Platform.composeErrors(error, e2, e3) }, { Platform.composeErrors(error, e2) }, { error })
                }, {
                  r3.fold({ e3 -> Platform.composeErrors(error, e3) }, { error }, { error })
                }, { error })
                cb(BIOResult.Error(errorResult))
              }
            }
          } else {
            promiseB.complete(BIOResult.Error(error))
          }
        }, { e ->
          if (active.getAndSet(false)) { // if an error finishes first, stop the race.
            connA.cancel().fix().unsafeRunAsync { r2 ->
              connC.cancel().fix().unsafeRunAsync { r3 ->
                conn.pop()
                cb(BIOResult.Left(e))
              }
            }
          } else {
            promiseB.complete(BIOResult.Left(e))
          }
        }, { b ->
          if (active.getAndSet(false)) {
            conn.pop()
            cb(BIOResult.Right(RaceTriple.Second(BIOFiber(promiseA, connA), b, BIOFiber(promiseC, connC))))
          } else {
            promiseB.complete(BIOResult.Right(b))
          }
        })
      }

      IORunLoop.startCancelable(BIOForkedStart(ioC, ctx), connC) { either: BIOResult<E, C> ->
        either.fold({ error ->
          if (active.getAndSet(false)) { // if an error finishes first, stop the race.
            connA.cancel().fix().unsafeRunAsync { r2 ->
              connB.cancel().fix().unsafeRunAsync { r3 ->
                conn.pop()
                val errorResult = r2.fold({ e2 ->
                  r3.fold({ e3 -> Platform.composeErrors(error, e2, e3) }, { Platform.composeErrors(error, e2) }, { error })
                }, {
                  r3.fold({ e3 -> Platform.composeErrors(error, e3) }, { error }, { error })
                }, { error })
                cb(BIOResult.Error(errorResult))
              }
            }
          } else {
            promiseC.complete(BIOResult.Error(error))
          }
        }, { e ->
          if (active.getAndSet(false)) { // if an error finishes first, stop the race.
            connA.cancel().fix().unsafeRunAsync { r2 ->
              connB.cancel().fix().unsafeRunAsync { r3 ->
                conn.pop()
                //
                cb(BIOResult.Left(e))
              }
            }
          } else {
            promiseC.complete(BIOResult.Left(e))
          }
        }, { c ->
          if (active.getAndSet(false)) {
            conn.pop()
            cb(BIOResult.Right(RaceTriple.Third(BIOFiber(promiseA, connA), BIOFiber(promiseB, connB), c)))
          } else {
            promiseC.complete(BIOResult.Right(c))
          }
        })
      }
    }
}
