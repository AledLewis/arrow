package arrow.fx.kotlinx

import arrow.core.andThen
import arrow.core.right
import arrow.fx.IO
import arrow.fx.IOConnection
import arrow.fx.IOContext
import arrow.fx.IOOf
import arrow.fx.IORunLoop
import arrow.fx.OnCancel
import arrow.fx.extensions.io.bracket.onCancel
import arrow.fx.fix
import arrow.fx.toDisposable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.resumeCancellableWith
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.coroutineContext
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

suspend fun <A> IOOf<A>.suspendedX(): A = suspendCancellableCoroutine { cont ->
  val disp = fix()
    .onCancel(IO.effect {
      println("SuspendedX onCancel got called")
      coroutineContext[Job]?.cancel() ?: Unit
    })
    .unsafeRunAsyncCancellable { either ->
      if (cont.isActive) {
        either.fold({ cont.resumeWithException(it) }, {
          cont.resume(it, {
            println("This handler got called...")
          })
        })
      }
    }

  cont.invokeOnCancellation {
    println("I got cancelled!")
    disp.invoke()
  }
}
