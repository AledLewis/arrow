package arrow.fx.typeclasses

import arrow.Kind
import arrow.Kind2
import arrow.fx.BiCancelToken
import arrow.fx.CancelToken
import arrow.higherkind

/**
 * [Fiber] represents the pure result of an [Async] data type
 * being started concurrently and that can be either joined or canceled.
 *
 * You can think of fibers as being lightweight threads, a Fiber being a
 * concurrency primitive for doing cooperative multi-tasking.
 */
@higherkind interface Fiber<F, out A> : FiberOf<F, A> {

  /**
   * Returns a new task that will await for the completion of the
   * underlying [Fiber], (asynchronously) blocking the current run-loop
   * until that result is available.
   */
  fun join(): Kind<F, A>

  /**
   * Triggers the cancellation of the [Fiber].
   *
   * @returns a task that trigger the cancellation upon evaluation.
   */
  fun cancel(): CancelToken<F>

  operator fun component1(): Kind<F, A> = join()
  operator fun component2(): CancelToken<F> = cancel()

  companion object {

    /**
     * [Fiber] constructor.
     *
     * @param join task that will trigger the cancellation.
     * @param cancel task that will await for the completion of the underlying Fiber.
     */
    operator fun <F, A> invoke(join: Kind<F, A>, cancel: CancelToken<F>): Fiber<F, A> = object : Fiber<F, A> {
      override fun join(): Kind<F, A> = join
      override fun cancel(): CancelToken<F> = cancel
      override fun toString(): String = "Fiber(join= ${join()}, cancel= ${cancel()})"
    }
  }
}

@higherkind interface BiFiber<F, out E, out A> : BiFiberOf<F, E, A> {

  /**
   * Returns a new task that will await for the completion of the
   * underlying [Fiber], (asynchronously) blocking the current run-loop
   * until that result is available.
   */
  fun join(): Kind2<F, E, A>

  /**
   * Triggers the cancellation of the [Fiber].
   *
   * @returns a task that trigger the cancellation upon evaluation.
   */
  fun cancel(): BiCancelToken<F>

  operator fun component1(): Kind2<F, E, A> = join()
  operator fun component2(): BiCancelToken<F> = cancel()

  companion object {

    /**
     * [Fiber] constructor.
     *
     * @param join task that will trigger the cancellation.
     * @param cancel task that will await for the completion of the underlying Fiber.
     */
    operator fun <F, E, A> invoke(join: Kind2<F, E, A>, cancel: BiCancelToken<F>): BiFiber<F, E, A> = object : BiFiber<F, E, A> {
      override fun join(): Kind2<F, E, A> = join
      override fun cancel(): BiCancelToken<F> = cancel
      override fun toString(): String = "Fiber(join= ${join()}, cancel= ${cancel()})"
    }
  }
}
