package de.htwg.se.model.BattleLogicComponent.BaseBattleLogicImpl

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers

class TrainerBattleLogicSpec extends AnyWordSpec with Matchers {
  "The TrainerBattleLogic" should {
    "not allow fleeing" in {
      TrainerBattleLogic.isFleeingAllowed should be(false)
    }

    "return the correct win message" in {
      val msg = TrainerBattleLogic.getWinMessage("Ash")
      msg should include("Trainer besiegt!")
      msg should include("Ash erhält 500 Pokedollar")
    }

    "return the correct loss message" in {
      val msg = TrainerBattleLogic.getLossMessage("Ash")
      msg should include("Ash ist kampfunfähig!")
      msg should include("Du verlierst Geld")
    }
  }
}