package de.htwg.se.model.BattleComponent.BattleImpl

import de.htwg.se.model.BattleComponent.BattleLogic

object TrainerBattleLogic extends BattleLogic {
  override def isFleeingAllowed: Boolean = false
  
  override def getWinMessage(winnerName: String): String =
    s"Trainer besiegt! $winnerName erhält 500 Pokédollar."
    
  override def getLossMessage(loserName: String): String =
    s"$loserName ist kampfunfähig! Du verlierst Geld."
}