package de.htwg.se.model.GameStateComponent

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers
import de.htwg.se.model.PlayerComponent.MockPlayerImpl.MockPlayer

class GameStateSpec extends AnyWordSpec with Matchers {

  "A GameState" should {
    val p = MockPlayer("P1")
    val e = MockPlayer("E1")
    val gs = GameState(p, e, false, "Msg1", "Msg2")

    "have correct initial values" in {
      gs.player should be(p)
      gs.enemy should be(e)
      gs.battleOver should be(false)
      gs.msg1 should be("Msg1")
      gs.msg2 should be("Msg2")
    }

    "support copy mechanism" in {
      val newGs = gs.copy(battleOver = true, msg1 = "NewMsg")
      newGs.battleOver should be(true)
      newGs.msg1 should be("NewMsg")
      newGs.player should be(p)
    }
    
    "be equal to another instance with same values" in {
      val gs2 = GameState(p, e, false, "Msg1", "Msg2")
      gs should be(gs2)
    }
  }
}