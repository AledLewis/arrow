package arrow.fx.mtl

import arrow.core.Either
import arrow.core.Left
import arrow.core.Right
import arrow.core.fix
import arrow.fx.BIOPartialOf
import arrow.fx.ForIO
import arrow.fx.IO
import arrow.fx.extensions.bio.concurrent.concurrent
import arrow.fx.extensions.fx
import arrow.fx.fix
import arrow.fx.mtl.eithert.concurrent.concurrent
import arrow.fx.typeclasses.Concurrent
import arrow.fx.value
import arrow.mtl.EitherT
import arrow.mtl.EitherTPartialOf
import arrow.mtl.fix

typealias Task<E, A> = EitherT<ForIO, E, A>

fun <E, A> asyncE(
  f: ((Either<E, A>) -> Unit) -> Unit
): EitherT<ForIO, E, A> =
  EitherT(IO.async<Either<E, A>> { cb ->
    f {
      cb(Right(it))
    }
  })

object EitherTExample {

  data class RegisteredUser(val name: String, val email: String)

  sealed class UserErrorDomain { // BIO is not flexible enough to cover 3 cases so we need to combine the error cases in an ADT
    data class UnregisteredUser(val name: String, val email: String? = null) : UserErrorDomain()
    object Anonymous : UserErrorDomain()
  }

  fun concurrent(): Concurrent<EitherTPartialOf<ForIO, UserErrorDomain>> = EitherT.concurrent(IO.concurrent())

  fun network(): Task<UserErrorDomain, RegisteredUser> =
    asyncE { cb ->
      Thread.sleep(750)
      cb(Left(UserErrorDomain.Anonymous))
    }

  // Write as many pure functions as possible!
  fun UserErrorDomain.welcomeMessage(): String =
    when (this) {
      is UserErrorDomain.UnregisteredUser -> "Welcome $name. ${email?.let { "You haven't confirmed your registration on $it" }}"
      UserErrorDomain.Anonymous -> "Welcome, would you like to register?"
    }

  fun program(): EitherT<BIOPartialOf<Nothing>, UserErrorDomain, Unit> =
    concurrent().fx.concurrent { // <-- currently not available in typeclass hiearchy, requires a second new hiearchy or needs to fixed to `E` in this case must be Nothing or doesn't have program signature
      val result = !network()
        .redeemWith(
          { throwable -> raiseError<String>(throwable) }, // Re-raise error
          // { error -> just(error.welcomeMessage()) }, // Resolve left to right to end with `Nothing` left
          { user -> just("Welcome ${user.name}") }
        )

      !effect { println(result) }
    }.fix()
}

suspend fun main(args: Array<String>): Unit =
  EitherTExample.program().value().fix().suspended()
    .value()
    .fold({ println("Left not redeemed") }, { Unit }) // Either error got ignored.
