package de.htwg.se.model.BattleLogicComponent

// Nur hier im Service-Objekt ist der Zugriff auf Impl erlaubt!
import de.htwg.se.model.BattleLogicComponent.BaseBattleLogicImpl.{WildBattleLogic, TrainerBattleLogic}

object BattleLogicService {
  
  // Gibt nur das Interface zurück -> Kapselung gewahrt
  def getWildLogic(): IBattleLogic = WildBattleLogic
  
  def getTrainerLogic(): IBattleLogic = TrainerBattleLogic
}