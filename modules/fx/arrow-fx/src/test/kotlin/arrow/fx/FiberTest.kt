package arrow.fx

import arrow.core.extensions.monoid
import arrow.fx.extensions.applicative
import arrow.fx.extensions.io.applicative.unit
import arrow.fx.extensions.io.concurrent.concurrent
import arrow.fx.extensions.monoid
import arrow.fx.typeclasses.Fiber
import arrow.fx.typeclasses.FiberOf
import arrow.fx.typeclasses.fix
import arrow.test.UnitSpec
import arrow.test.laws.ApplicativeLaws
import arrow.test.laws.MonoidLaws
import arrow.typeclasses.Eq
import io.kotlintest.properties.Gen

class FiberTest : UnitSpec() {

  init {
    fun FIBER_EQ(): Eq<FiberOf<IOPartialOf<Throwable>, Int>> = object : Eq<FiberOf<IOPartialOf<Throwable>, Int>> {
      override fun FiberOf<IOPartialOf<Throwable>, Int>.eqv(b: FiberOf<IOPartialOf<Throwable>, Int>): Boolean = EQ<Int>().run {
        fix().join().eqv(b.fix().join())
      }
    }

    testLaws(
      ApplicativeLaws.laws(Fiber.applicative(IO.concurrent()), FIBER_EQ()),
      MonoidLaws.laws(Fiber.monoid(IO.concurrent(), Int.monoid()), Gen.int().map { i ->
        val io: IO<Throwable, Int> = IO.just(i)
        Fiber(io, IO.unit)
      }, FIBER_EQ())
    )
  }
}
