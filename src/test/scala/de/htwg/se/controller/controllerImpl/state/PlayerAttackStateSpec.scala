package de.htwg.se.controller.state

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers
import de.htwg.se.model._
import de.htwg.se.controller.controllerImpl.state.{MenuState, PlayerAttackState}

class PlayerAttackStateSpec extends AnyWordSpec with Matchers {

  "A PlayerAttackState" should {
    // Setup
    val pokemon = PokemonFactory.getPokemon("Glurak")
    val player = Player("Ash", Vector(pokemon))
    val enemy = Player("Gary", Vector(pokemon))
    val gameState = GameState(player, enemy)
    // Wir nehmen einfach WildBattleLogic, da die Logik für den State-Test zweitrangig ist
    val state = PlayerAttackState(gameState, WildBattleLogic) 

    "handle valid attack input (1-4)" in {
      // 1 ist ein gültiger Index (Glurak hat Attacken)
      val nextState = state.handle("1")
      // Wenn es geklappt hat, ändert sich msg1 zu "... setzt ... ein!"
      nextState.gameState.msg1 should include ("setzt")
    }

    "handle invalid attack index (too high/low)" in {
      // Index 99 gibt es nicht -> lift gibt None -> case None greift
      val nextState = state.handle("99")
      
      // WICHTIG: Hier prüfen wir auf die NEUE, einheitliche Fehlermeldung
      nextState shouldBe a [PlayerAttackState]
      nextState.gameState.msg2 should include ("Ungültige Eingabe")
    }

    "handle garbage input (not a number)" in {
      // "blabla" ist keine Zahl -> toIntOption gibt None -> case None greift
      val nextState = state.handle("blabla")
      
      // WICHTIG: Auch hier kommt jetzt die gleiche Fehlermeldung
      nextState shouldBe a [PlayerAttackState]
      nextState.gameState.msg2 should include ("Ungültige Eingabe")
    }

    "handle flee command" in {
      val nextState = state.handle("f")
      nextState shouldBe a [MenuState]
      nextState.gameState.msg1 should include ("geflohen")
    }
  }
}