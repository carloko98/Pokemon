package de.htwg.se.model.BattleLogicComponent

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers
import de.htwg.se.model.BattleLogicComponent.BaseBattleLogicImpl.{WildBattleLogic, TrainerBattleLogic}

class BattleLogicServiceSpec extends AnyWordSpec with Matchers {
  "The BattleLogicService" should {
    "return the WildBattleLogic singleton" in {
      BattleLogicService.getWildLogic() should be(WildBattleLogic)
    }

    "return the TrainerBattleLogic singleton" in {
      BattleLogicService.getTrainerLogic() should be(TrainerBattleLogic)
    }
  }
}