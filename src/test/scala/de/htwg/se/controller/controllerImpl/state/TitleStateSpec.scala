package de.htwg.se.controller.state

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers
import de.htwg.se.model.{GameState, Player, Pokemon, PokemonType, Attack}
import de.htwg.se.controller.controllerImpl.state.{NameInputState, SelectProfileState, TitleState}

class TitleStateSpec extends AnyWordSpec with Matchers {

  "The TitleState" should {
    val pokemon = Pokemon("TestMon", PokemonType.Fire, 100, 100, Vector(Attack("Test", 10, PokemonType.Normal)))
    val player = Player("Ash", Vector(pokemon))
    val enemy = Player("Gary", Vector(pokemon))
    val gameState = GameState(player, enemy)
    
    val titleState = TitleState(gameState)

    "transition to NameInputState on 'n' or 'neu'" in {
      val nextState = titleState.handle("n")
      nextState shouldBe a [NameInputState]
      
      val nextState2 = titleState.handle("neu")
      nextState2 shouldBe a [NameInputState]
    }

    "transition to SelectProfileState on 'l' or 'laden'" in {
      val nextState = titleState.handle("l")
      nextState shouldBe a [SelectProfileState]
      
      val nextState2 = titleState.handle("laden")
      nextState2 shouldBe a [SelectProfileState]
    }

    
    "remain in TitleState on unknown input" in {
      val nextState = titleState.handle("xyz")
      nextState shouldBe a [TitleState]
      nextState.gameState.msg2 should include ("[n]eues Spiel")
    }
  }
}