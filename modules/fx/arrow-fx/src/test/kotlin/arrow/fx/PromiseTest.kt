package arrow.fx

import arrow.core.Left
import arrow.core.None
import arrow.core.Some
import arrow.core.Tuple2
import arrow.fx.extensions.bio.apply.product
import arrow.fx.extensions.bio.async.async
import arrow.fx.extensions.bio.concurrent.concurrent
import arrow.fx.extensions.bio.functor.tupleLeft
import arrow.fx.extensions.bio.monadDefer.monadDefer
import arrow.test.UnitSpec
import arrow.test.generators.throwable
import io.kotlintest.properties.Gen
import io.kotlintest.properties.forAll
import io.kotlintest.shouldBe
import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

class PromiseTest : UnitSpec() {

  init {

    fun tests(
      label: String,
      ctx: CoroutineContext = Dispatchers.Default,
      promise: IO<Promise<ForIO, Int>>
    ) {

      "$label - complete" {
        forAll(Gen.int()) { a ->
          promise.flatMap { p ->
            p.complete(a).flatMap {
              p.get()
            }
          }.unsafeRunSync().value() == a
        }
      }

      "$label - complete twice should result in Promise.AlreadyFulfilled" {
        forAll(Gen.int(), Gen.int()) { a, b ->
          promise.flatMap { p ->
            p.complete(a).flatMap {
              p.complete(b)
                .attemptIO()
                .product(p.get())
            }
          }.unsafeRunSync().value() == Tuple2(Left(Promise.AlreadyFulfilled), a)
        }
      }

      "$label - tryComplete" {
        forAll(Gen.int()) { a ->
          promise.flatMap { p ->
            p.tryComplete(a).flatMap { didComplete ->
              p.get().tupleLeft(didComplete)
            }
          }.unsafeRunSync().value() == Tuple2(true, a)
        }
      }

      "$label - tryComplete twice returns false" {
        forAll(Gen.int(), Gen.int()) { a, b ->
          promise.flatMap { p ->
            p.tryComplete(a).flatMap {
              p.tryComplete(b).flatMap { didComplete ->
                p.get().tupleLeft(didComplete)
              }
            }
          }.unsafeRunSync().value() == Tuple2(false, a)
        }
      }

      "$label - error" {
        forAll(Gen.throwable()) { error ->
          promise.flatMap { p ->
            p.error(error).flatMap {
              p.get().attemptIO()
            }
          }.unsafeRunSync().value() == Left(error)
        }
      }

      "$label - error twice should result in Promise.AlreadyFulfilled" {
        forAll(Gen.throwable()) { error ->
          promise.flatMap { p ->
            p.error(error).flatMap {
              p.error(RuntimeException("Boom!")).attemptIO()
                .product(p.get().attemptIO())
            }
          }.unsafeRunSync().value() == Tuple2(Left(Promise.AlreadyFulfilled), Left(error))
        }
      }

      "$label - tryError" {
        forAll(Gen.throwable()) { error ->
          promise.flatMap { p ->
            p.tryError(error).flatMap { didError ->
              p.get().attemptIO()
                .tupleLeft(didError)
            }
          }.unsafeRunSync().value() == Tuple2(true, Left(error))
        }
      }

      "$label - tryError twice returns false" {
        forAll(Gen.throwable()) { error ->
          promise.flatMap { p ->
            p.tryError(error).flatMap {
              p.tryError(RuntimeException("Boom!")).flatMap { didComplete ->
                p.get().attemptIO()
                  .tupleLeft(didComplete)
              }
            }
          }.unsafeRunSync().value() == Tuple2(false, Left(error))
        }
      }

      "$label - get blocks until set" {
        Ref(IO.monadDefer(), 0).flatMap { state ->
          promise.flatMap { modifyGate ->
            promise.flatMap { readGate ->
              modifyGate.get().flatMap { state.update { i -> i * 2 }.flatMap { readGate.complete(0) } }.fork(ctx).flatMap {
                state.set(1).flatMap { modifyGate.complete(0) }.fork(ctx).flatMap {
                  readGate.get().flatMap {
                    state.get()
                  }
                }
              }
            }
          }
        }.unsafeRunSync().value() shouldBe 2
      }

      "$label - tryGet returns None for empty Promise" {
        promise.flatMap { p -> p.tryGet() }.unsafeRunSync().value() shouldBe None
      }

      "$label - tryGet returns Some for completed promise" {
        forAll(Gen.int()) { a ->
          promise.flatMap { p ->
            p.complete(a).flatMap {
              p.tryGet()
            }
          }.unsafeRunSync().value() == Some(a)
        }
      }
    }

    tests("CancelablePromise", promise = Promise<ForIO, Int>(IO.concurrent()).fix())
    tests("UncancelablePromise", promise = Promise.uncancelable<ForIO, Int>(IO.async()).fix())
  }
}
