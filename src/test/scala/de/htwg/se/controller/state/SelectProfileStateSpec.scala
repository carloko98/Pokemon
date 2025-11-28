package de.htwg.se.controller.state

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers
import de.htwg.se.model.{GameState, Player, Pokemon, PokemonType, Attack}

class SelectProfileStateSpec extends AnyWordSpec with Matchers {

  "The SelectProfileState" should {
    val pokemon = Pokemon("TestMon", PokemonType.Fire, 100, 100, Vector(Attack("Test", 10, PokemonType.Normal)))
    val player = Player("TestPlayer", Vector(pokemon))
    val enemy = Player("TestEnemy", Vector(pokemon))
    val gameState = GameState(player, enemy)
    
    val state = SelectProfileState(gameState)

    "return itself when handle is called (logic is in Controller)" in {
    
      val nextState = state.handle("irgendein input")
      
      nextState should be(state)
    }
  }
}