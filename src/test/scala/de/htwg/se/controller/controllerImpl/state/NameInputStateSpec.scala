package de.htwg.se.controller.state

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers
import de.htwg.se.model.{GameState, Player, Pokemon, PokemonType, Attack}
import de.htwg.se.controller.controllerImpl.state.{NameInputState}
import de.htwg.se.model.StateComponent.MenuState

class NameInputStateSpec extends AnyWordSpec with Matchers {

  "The NameInputState" should {
    val attack = Attack("Test", 10, PokemonType.Normal)
    val pokemon = Pokemon("TestMon", PokemonType.Fire, 100, 100, Vector(attack))
    val dummyPlayer = Player("Dummy", Vector(pokemon))
    
    val initialState = NameInputState(GameState(dummyPlayer, dummyPlayer))

    "handle empty input correctly" in {
      val nextState = initialState.handle("   ")

      nextState shouldBe a [NameInputState]
      
      nextState.gameState.msg2 should be("Name darf nicht leer sein!")
    }

    "handle valid name input correctly" in {
      val inputName = "Gary"
      val nextState = initialState.handle(inputName)
      
      nextState shouldBe a [MenuState]
      
      val nextGS = nextState.gameState
      
      nextGS.player.name should be("Gary")
      
      nextGS.player.team should not be empty
      nextGS.player.team.head.name should be("Glurak")
      
      nextGS.enemy.team should not be empty
      
      nextGS.msg1 should be(s"Hallo $inputName!")
      nextGS.msg2 should be("Willkommen in der Welt der Pokemon!")
    }
  }
}