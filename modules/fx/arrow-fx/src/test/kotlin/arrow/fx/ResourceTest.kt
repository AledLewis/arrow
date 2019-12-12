package arrow.fx

import arrow.Kind
import arrow.core.Right
import arrow.core.Some
import arrow.core.extensions.monoid
import arrow.core.extensions.list.traverse.traverse
import arrow.fx.extensions.bio.applicative.applicative
import arrow.fx.extensions.bio.bracket.bracket
import arrow.fx.extensions.resource.applicative.applicative
import arrow.fx.extensions.resource.monad.monad
import arrow.fx.extensions.resource.monoid.monoid
import arrow.fx.typeclasses.seconds
import arrow.test.UnitSpec
import arrow.test.laws.MonadLaws
import arrow.test.laws.MonoidLaws
import arrow.test.laws.forFew
import arrow.typeclasses.Eq
import io.kotlintest.properties.Gen

class ResourceTest : UnitSpec() {
  init {

    val EQ = Eq<Kind<ResourcePartialOf<ForIO, Throwable>, Int>> { a, b ->
      val tested: IO<Int> = a.fix().invoke { IO.just(1) }.fix()
      val expected = b.fix().invoke { IO.just(1) }.fix()
      val compare = IO.applicative().map(tested, expected) { (t, e) -> t == e }.fix()
      compare.unsafeRunTimed(5.seconds) == Some(Right(true))
    }

    testLaws(
      MonadLaws.laws(Resource.monad(IO.bracket()), EQ),
      MonoidLaws.laws(Resource.monoid(Int.monoid(), IO.bracket()), Gen.int().map { Resource.just(it, IO.bracket()) }, EQ)
    )

    "Resource releases resources in reverse order of acquisition" {
      forFew(5, Gen.list(Gen.string())) { l ->
        val released = mutableListOf<String>()
        l.traverse(Resource.applicative(IO.bracket())) {
          Resource({ IO { it } }, { r -> IO { released.add(r); Unit } }, IO.bracket())
        }.fix().invoke { IO.unit }.fix().unsafeRunSync()

        // This looks confusing but is correct, traverse is a rightFold => l is already "reversed"
        l == released
      }
    }
  }
}
