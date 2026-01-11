package de.htwg.se.controller.controllerImpl.state

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers
import de.htwg.se.model.GameStateComponent.GameState
import de.htwg.se.model.PokemonComponent.PokemonService

class MenuStateSpec extends AnyWordSpec with Matchers {

  "A MenuState" should {
    val p = PokemonService.createPlayer("Test", Vector("Glurak"))
    val e = PokemonService.createRandomEnemy()
    val gs = GameState(p, e, false, "", "")
    val state = MenuState(gs)

    "transition to PlayerAttackState on input 's'" in {
      val result = state.handle("s")
      result should be(a [PlayerAttackState])
      result.gameState.msg1 should include ("Wilder Kampf")
    }

    "transition to PlayerAttackState on input 't'" in {
      val result = state.handle("t")
      result should be(a [PlayerAttackState])
      result.gameState.msg1 should include ("Trainerkampf")
    }

    "stay in MenuState on invalid input" in {
      state.handle("invalid") should be(a [MenuState])
    }
  }
}