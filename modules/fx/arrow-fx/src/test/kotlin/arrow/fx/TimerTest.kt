package arrow.fx

import arrow.fx.extensions.bio.async.async
import arrow.fx.extensions.bio.concurrent.concurrent
import arrow.test.UnitSpec
import arrow.test.laws.TimerLaws

class TimerTest : UnitSpec() {
  init {
    testLaws(TimerLaws.laws(IO.async(), Timer.invoke(IO.concurrent()), EQ()))
  }
}
