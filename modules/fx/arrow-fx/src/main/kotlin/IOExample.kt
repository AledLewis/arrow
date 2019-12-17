import arrow.core.Right
import arrow.fx.IO
import arrow.fx.extensions.fx
import arrow.fx.value

object IOExample {

  // Model domain properly
  sealed class UserDomain {
    data class RegisteredUser(val name: String, val email: String) : UserDomain()
    data class UnregisteredUser(val name: String, val email: String? = null) : UserDomain()
    object Anonymous : UserDomain()
  }

  fun network(): IO<UserDomain> =
    IO.async<UserDomain> { callback ->
      Thread.sleep(750)
      callback(Right(UserDomain.UnregisteredUser("Simon")))
    }

  // Write as many pure functions as possible!
  fun UserDomain.welcomeMessage(): String =
    when (this) {
      is UserDomain.RegisteredUser -> "Welcome $name"
      is UserDomain.UnregisteredUser -> "Welcome $name. ${email?.let { "You haven't confirmed your registration on $it" }}"
      UserDomain.Anonymous -> "Welcome, would you like to register?"
    }

  fun program(): IO<Unit> =
    IO.fx {
      val result = !network()
      !IO.effect { println(result.welcomeMessage()) }
    }

  suspend fun main(args: Array<String>): Unit =
    program().suspended()
      .value() // <-- due to BIO impl
}
