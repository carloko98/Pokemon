package de.htwg.se.controller.controllerImpl.state

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers
import de.htwg.se.model.GameStateComponent.GameState
import de.htwg.se.model.PokemonComponent.PokemonService

class SelectProfileStateSpec extends AnyWordSpec with Matchers {

  "A SelectProfileState" should {
    val p = PokemonService.createPlayer("Test", Vector("Glurak"))
    val e = PokemonService.createRandomEnemy()
    val gs = GameState(p, e, false, "", "")
    val state = SelectProfileState(gs)

    "transition to TitleState on input 'b'" in {
      state.handle("b") should be(a [TitleState])
    }
    
    "stay in SelectProfileState (or transition) on other input" in {
      noException should be thrownBy state.handle("AnySaveGame")
    }
  }
}