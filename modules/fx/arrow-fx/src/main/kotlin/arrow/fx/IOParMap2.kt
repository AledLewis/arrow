package arrow.fx

import arrow.core.Either
import arrow.core.Left
import arrow.core.Right
import arrow.core.internal.AtomicRefW
import arrow.core.nonFatalOrThrow
import arrow.fx.internal.BIOForkedStart
import arrow.fx.internal.BIOResult
import arrow.fx.internal.Platform
import kotlin.coroutines.CoroutineContext

/** Mix-in to enable `parMapN` 2-arity on IO's companion directly. */
interface IOParMap2 {

  fun <E, A, B, C> parMapN(ctx: CoroutineContext, fa: BIOOf<E, A>, fb: BIOOf<E, B>, f: (A, B) -> C): BIO<E, C> = BIO.Async { conn, cb ->
    // Used to store Throwable, Either<A, B> or empty (null). (No sealed class used for a slightly better performing ParMap2)
    val state = AtomicRefW<Any?>(null)

    val connA = IOConnection()
    val connB = IOConnection()

    conn.pushPair(connA, connB)

    fun complete(a: A, b: B) {
      conn.pop()
      cb(try {
        BIOResult.Right(f(a, b))
      } catch (e: Throwable) {
        BIOResult.Error(e.nonFatalOrThrow())
      })
    }

    fun sendException(other: IOConnection, e: Throwable) = when (state.getAndSet(e)) {
      is Throwable -> Unit // Do nothing we already finished TODO replace with active field
      else -> other.cancel().fix().unsafeRunAsync { r ->
        conn.pop()
        // TODO if `r` is an exception send it to the asyncErrorHandler
        cb(BIOResult.Error(r.fold({ e2 -> Platform.composeErrors(e, e2) }, { e }, { e })))
      }
    }

    fun sendError(other: IOConnection, e: E) = when (state.getAndSet(e)) {
      is Throwable -> Unit
      else -> other.cancel().fix().unsafeRunAsync { r ->
        conn.pop()
        cb(BIOResult.Left(e))
        // TODO if `r` is an exception send it to the asyncErrorHandler
      }
    }

    IORunLoop.startCancelable(BIOForkedStart(fa, ctx), connA) { resultA ->
      resultA.fold({ e ->
        sendException(connB, e)
      }, { e ->
        sendError(connB, e)
      }, { a ->
        when (val oldState = state.getAndSet(Left(a))) {
          null -> Unit // Wait for B
          is Throwable -> Unit // ParMapN already failed and A was cancelled.
          is Either.Left<*> -> Unit // Already state.getAndSet
          is Either.Right<*> -> complete(a, (oldState as Either.Right<B>).b)
        }
      })
    }

    IORunLoop.startCancelable(BIOForkedStart(fb, ctx), connB) { resultB ->
      resultB.fold({ e ->
        sendException(connA, e)
      }, { e ->
        sendError(connB, e)
      }, { b ->
        when (val oldState = state.getAndSet(Right(b))) {
          null -> Unit // Wait for A
          is Throwable -> Unit // ParMapN already failed and B was cancelled.
          is Either.Right<*> -> Unit // IO cannot finish twice
          is Either.Left<*> -> complete((oldState as Either.Left<A>).a, b)
        }
      })
    }
  }
}
