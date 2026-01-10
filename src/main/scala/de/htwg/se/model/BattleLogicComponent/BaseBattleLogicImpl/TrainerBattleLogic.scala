package de.htwg.se.model.BattleLogicComponent.BaseBattleLogicImpl

import de.htwg.se.model.BattleLogicComponent.IBattleLogic


object TrainerBattleLogic extends IBattleLogic {
  override def isFleeingAllowed: Boolean = false
  override def getWinMessage(winnerName: String): String = 
    s"Trainer besiegt! $winnerName erhält 500 Pokedollar."
  override def getLossMessage(loserName: String): String = 
    s"$loserName ist kampfunfähig! Du verlierst Geld."
}
