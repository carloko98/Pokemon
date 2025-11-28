package de.htwg.se.model

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers

class BattleLogicSpec extends AnyWordSpec with Matchers {

  "The WildBattleLogic" should {
    "allow fleeing" in {
      WildBattleLogic.isFleeingAllowed should be(true)
    }

    "provide the correct win message" in {
      WildBattleLogic.getWinMessage("Ash") should be("Wildes Pokemon besiegt! Ash gewinnt an Erfahrung.")
    }

    "provide the correct loss message" in {
      WildBattleLogic.getLossMessage("Ash") should be("Ash wurde besiegt! Ab zum Pokemon Center.")
    }
  }

  "The TrainerBattleLogic" should {
    "not allow fleeing" in {
      TrainerBattleLogic.isFleeingAllowed should be(false)
    }

    "provide the correct win message" in {
      TrainerBattleLogic.getWinMessage("Ash") should be("Trainer besiegt! Ash erhält 500 Pokedollar.")
    }

    "provide the correct loss message" in {
      TrainerBattleLogic.getLossMessage("Ash") should be("Ash ist kampfunfähig! Du verlierst Geld.")
    }
  }
}