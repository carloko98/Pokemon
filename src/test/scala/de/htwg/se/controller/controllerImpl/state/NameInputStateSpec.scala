package de.htwg.se.controller.controllerImpl.state

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers
import de.htwg.se.model.GameStateComponent.GameState
import de.htwg.se.model.PokemonComponent.PokemonService

class NameInputStateSpec extends AnyWordSpec with Matchers {

  "A NameInputState" should {
    val p = PokemonService.createPlayer("Old", Vector("Glurak"))
    val e = PokemonService.createRandomEnemy()
    val gs = GameState(p, e, false, "", "")
    val state = NameInputState(gs)

    "stay in NameInputState on empty input" in {
      val result = state.handle("   ")
      result should be(a [NameInputState])
    }

    "transition to MenuState on valid input" in {
      val result = state.handle("Ash")
      result should be(a [MenuState])
      result.gameState.player.name should be("Ash")
    }
  }
}