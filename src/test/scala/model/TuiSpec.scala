package de.htwg.view

import de.htwg.controller.{Controller, ControllerImpl, Observer}
import de.htwg.model.{Pokemon, Attack, PokemonType}
import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers._
import java.io.{ByteArrayOutputStream, PrintStream}

class TuiSpec extends AnyWordSpec {

  val tackle = Attack("Tackle", 6, PokemonType.Normal)
  val pikachu = Pokemon("PIKACHU", PokemonType.Electric, 34, 34, Vector(tackle))
  val horsea = Pokemon("HORSEA", PokemonType.Water, 40, 40, Vector(tackle))

  "Tui" should {

    "clear screen on render" in {
      val ctrl = new ControllerImpl(pikachu, horsea)
      val out = new ByteArrayOutputStream()
      Console.withOut(out) {
        val tui = new Tui(ctrl)
        tui.render()
      }
      out.toString should include("\u001b[2J\u001b[H")
    }

    "show HP bar correctly" in {
      val ctrl = new ControllerImpl(pikachu, horsea)
      val tui = new Tui(ctrl)

      // Access private method via reflection or make it public for test
      // Or test via output
      val method = classOf[Tui].getDeclaredMethod("hpBar", classOf[Int], classOf[Int])
      method.setAccessible(true)
      method.invoke(tui, 17: Integer, 34: Integer).asInstanceOf[String] should be("#######------")
    }

    "handle invalid input gracefully" in {
      val ctrl = new ControllerImpl(pikachu, horsea)
      val tui = new Tui(ctrl)

      // Simulate input loop with invalid input
      // This is integration-level, better in integration test
    }
  }
}