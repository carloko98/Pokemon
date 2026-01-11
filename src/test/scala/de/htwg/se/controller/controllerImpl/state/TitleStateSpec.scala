package de.htwg.se.controller.controllerImpl.state

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers
import de.htwg.se.model.GameStateComponent.GameState
import de.htwg.se.model.PokemonComponent.PokemonService

class TitleStateSpec extends AnyWordSpec with Matchers {

  "A TitleState" should {
    val p = PokemonService.createPlayer("Test", Vector("Glurak"))
    val e = PokemonService.createRandomEnemy()
    val gs = GameState(p, e, false, "", "")
    val state = TitleState(gs)

    "transition to NameInputState on input 'n'" in {
      state.handle("n") should be(a [NameInputState])
    }

    "transition to SelectProfileState on input 'l'" in {
      state.handle("l") should be(a [SelectProfileState])
    }

    "stay in TitleState on invalid input" in {
      state.handle("xyz") should be(a [TitleState])
    }
  }
}