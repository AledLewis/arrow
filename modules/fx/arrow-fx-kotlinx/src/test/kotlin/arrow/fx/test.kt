package arrow.fx

import arrow.core.Either
import arrow.core.andThen
import arrow.core.right
import arrow.fx.extensions.fx
import arrow.fx.extensions.io.bracket.onCancel
import arrow.fx.extensions.io.dispatchers.dispatchers
import arrow.fx.kotlinx.suspendedX
import arrow.fx.typeclasses.Disposable
import arrow.fx.typeclasses.ExitCase
import arrow.fx.typeclasses.milliseconds
import arrow.test.UnitSpec
import io.kotlintest.shouldBe
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.async
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.coroutineContext
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class KotlinXSpec : UnitSpec() {
  init {
    "test" {
      val changed = true
      changed shouldBe false
    }

    // "Cancelling coroutine execution" {
    //   suspend fun longRunning() = launch {
    //     repeat(1000) {
    //       delay(500L)
    //     }
    //   }
    //
    //   IO.fx {
    //     val latch = !Promise<ExitCase<Throwable>>()
    //
    //     val (_, cancel) = !IO.unit.bracketCase(
    //       use = {
    //         IO.effect { longRunning() }
    //           .onCancel(IO { coroutineContext[Job]?.cancel() ?: Unit })
    //       },
    //       release = { _, ec -> latch.complete(ec) }
    //     ).fork()
    //
    //     !sleep(100.milliseconds)
    //     !cancel
    //     val result = !latch.get()
    //     !effect { result shouldBe ExitCase.Canceled }
    //   }.suspendedX()
    // }

    // "Cancelling coroutine execution" {
    //   suspend fun longRunning() = launch(IO.dispatchers().default()) {
    //     repeat(1000) {
    //       delay(500L)
    //       println("Hello")
    //     }
    //   }
    //
    //   val disp =
    //     IO.effect { longRunning() }
    //       .onCancel(IO { coroutineContext[Job]?.cancel() ?: Unit })
    //       .unsafeRunAsyncCancellableX {
    //
    //       }
    //
    //   delay(700)
    //   println("Going to cancel KotlinX SHIT")
    //   disp.invoke()
    // }

    // "Cancelling coroutine execution" {
    //   suspend fun longRunning() {
    //     launch(Dispatchers.Default) {
    //       repeat(100) {
    //         delay(250L)
    //       }
    //     }
    //   }
    //
    //   IO.shift(IO.dispatchers().default())
    //     .followedBy(IO.effect { longRunning() })
    //     .onCancel(IO.effect {
    //       println("Hello Bitch!")
    //       coroutineContext.cancel()
    //     })
    //     .unsafeRunAsyncCancellable {}
    //     .invoke()
    // }

    "Cancelling IO from KotlnX" {
      fun infiniteLoop(): IO<Unit> {
        fun loop(iterations: Int): IO<Unit> =
          IO.just(iterations).flatMap { i ->
            IO.sleep(1.milliseconds)
              .followedBy(loop(i + 1))
          }

        return loop(0)
      }

      val completable = CompletableDeferred<Unit>()

      val infinite = async(IO.dispatchers().default()) {
        infiniteLoop()
          .onCancel(IO { completable.complete(Unit); Unit })
          .suspendedX2()
      }

      delay(2000)
      infinite.cancel()
      delay(2000)
      completable.isCompleted shouldBe true
    }
  }
}

suspend fun <A> IOOf<A>.suspendedX2(): A = suspendCancellableCoroutine { cont ->
  val conn = cont.context[IOContext]?.connection ?: IOConnection()

  IORunLoop.startCancelable(this, conn) { eith ->
    eith.fold(cont::resumeWithException) { cont.resume(it) }
  }

  cont.invokeOnCancellation { conn.cancel().fix().unsafeRunSync() }
}

fun <A> IOOf<A>.runAsyncCancellableX(onCancel: OnCancel = OnCancel.Silent, cb: (Either<Throwable, A>) -> IOOf<Unit>): IO<Disposable> =
  IO.async { ccb ->
    val conn = IOConnection()
    conn.push(IO.effect { println("I got cancelled bitch") })
    val onCancelCb =
      when (onCancel) {
        OnCancel.ThrowCancellationException ->
          cb andThen { it.fix().unsafeRunAsync { } }
        OnCancel.Silent ->
          { either -> either.fold({ if (!conn.isCanceled() || it != OnCancel.CancellationException) cb(either) }, { cb(either) }) }
      }
    ccb(conn.toDisposable().right())
    IORunLoop.startCancelable(fix()
      .onCancel(IO.effect {
        val job = coroutineContext[Job]
        println("Job is $job")
        job?.cancel() ?: Unit
      }
      ), conn, onCancelCb)
  }

fun <A> IOOf<A>.unsafeRunAsyncCancellableX(onCancel: OnCancel = OnCancel.Silent, cb: (Either<Throwable, A>) -> Unit): Disposable =
  runAsyncCancellableX(onCancel, cb andThen { IO.unit }).unsafeRunSync()
