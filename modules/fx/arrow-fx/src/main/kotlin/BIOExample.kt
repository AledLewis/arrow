import arrow.fx.BIO
import arrow.fx.extensions.fx
import arrow.fx.internal.BIOResult
import arrow.fx.value

// Model domain properly

object BIOExample {

  data class RegisteredUser(val name: String, val email: String)

  sealed class UserErrorDomain { // BIO is not flexible enough to cover 3 cases so we need to combine the error cases in an ADT
    data class UnregisteredUser(val name: String, val email: String? = null) : UserErrorDomain()
    object Anonymous : UserErrorDomain()
  }

  fun network(): BIO<UserErrorDomain, RegisteredUser> =
    BIO.async<UserErrorDomain, RegisteredUser> { callback ->
      Thread.sleep(750)
      callback(BIOResult.Left(UserErrorDomain.Anonymous))
    }

  // Write as many pure functions as possible!
  fun UserErrorDomain.welcomeMessage(): String =
    when (this) {
      is UserErrorDomain.UnregisteredUser -> "Welcome $name. ${email?.let { "You haven't confirmed your registration on $it" }}"
      UserErrorDomain.Anonymous -> "Welcome, would you like to register?"
    }

  fun program(): BIO<Nothing, Unit> =
    BIO.fx { // <-- currently not available in typeclass hiearchy, requires a second new hiearchy or needs to fixed to `E` in this case must be Nothing or doesn't have program signature
      val result = !network()
        .redeemWith(
          { throwable -> BIO.raiseException<String>(throwable) }, // Re-raise error
          { error -> BIO.just(error.welcomeMessage()) }, // Resolve left to right to end with `Nothing` left
          { user -> BIO.just("Welcome ${user.name}") }
        )

      !BIO.effect { println(result) }
    }

  suspend fun main(args: Array<String>): Unit =
    program().suspended()
      .value() // <-- due to BIO impl
}
