package arrow.fx.internal

import arrow.fx.BIO
import arrow.fx.BIOPartialOf
import arrow.fx.IOConnection
import arrow.fx.typeclasses.Fiber

internal fun <E, A> BIOFiber(promise: UnsafePromise<E, A>, conn: IOConnection): Fiber<BIOPartialOf<E>, A> {
  val join: BIO<E, A> = BIO.Async { conn2, cb ->
    conn2.push(BIO { promise.remove(cb) })

    promise.get { a ->
      cb(a)
      conn2.pop()
      conn.pop()
    }
  }

  return Fiber(join, conn.cancel())
}
