package de.htwg.se.model

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers
import de.htwg.se.model.GameStateComponent.GameState

class GameStateSpec extends AnyWordSpec with Matchers {
  "A GameState" should {
    val p1 = Player("P1")
    val p2 = Player("P2")
    val state = GameState(p1, p2)

    "be initialized correctly with default values" in {
      state.player should be(p1)
      state.enemy should be(p2)
      state.battleOver should be(false)
      state.msg1 should be("")
      state.msg2 should be("")
    }

    "allow copying with modified values" in {
      val newState = state.copy(battleOver = true, msg1 = "Win")
      newState.battleOver should be(true)
      newState.msg1 should be("Win")
      newState.player should be(p1)
    }
  }
}