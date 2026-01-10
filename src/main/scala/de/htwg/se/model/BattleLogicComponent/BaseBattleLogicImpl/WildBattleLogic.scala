package de.htwg.se.model.BattleLogicComponent.BaseBattleLogicImpl

import de.htwg.se.model.BattleLogicComponent.IBattleLogic


object WildBattleLogic extends IBattleLogic {
  override def isFleeingAllowed: Boolean = true
  override def getWinMessage(winnerName: String): String = 
    s"Wildes Pokemon besiegt! $winnerName gewinnt an Erfahrung."
  override def getLossMessage(loserName: String): String = 
    s"$loserName wurde besiegt! Ab zum Pokemon Center."
}