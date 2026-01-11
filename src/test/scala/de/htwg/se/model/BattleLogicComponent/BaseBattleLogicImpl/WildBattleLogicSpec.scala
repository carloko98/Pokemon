package de.htwg.se.model.BattleLogicComponent.BaseBattleLogicImpl

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers

class WildBattleLogicSpec extends AnyWordSpec with Matchers {
  "The WildBattleLogic" should {
    "allow fleeing" in {
      WildBattleLogic.isFleeingAllowed should be(true)
    }

    "return the correct win message" in {
      val msg = WildBattleLogic.getWinMessage("Ash")
      msg should include("Wildes Pokemon besiegt!")
      msg should include("Ash gewinnt an Erfahrung")
    }

    "return the correct loss message" in {
      val msg = WildBattleLogic.getLossMessage("Ash")
      msg should include("Ash wurde besiegt!")
      msg should include("Ab zum Pokemon Center")
    }
  }
}