package de.htwg.se.model.BattleLogicComponent

import de.htwg.se.model.BattleLogicComponent.BaseBattleLogicImpl.{WildBattleLogic, TrainerBattleLogic}

object BattleLogicService {

  def getWildLogic(): IBattleLogic = WildBattleLogic
  
  def getTrainerLogic(): IBattleLogic = TrainerBattleLogic
}