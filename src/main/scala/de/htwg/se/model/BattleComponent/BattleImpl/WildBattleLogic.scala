package de.htwg.se.model.BattleComponent.BattleImpl

import de.htwg.se.model.BattleComponent.BattleLogic

object WildBattleLogic extends BattleLogic {
  override def isFleeingAllowed: Boolean = true
  
  override def getWinMessage(winnerName: String): String =
    s"Wildes Pokémon besiegt! $winnerName gewinnt an Erfahrung."
    
  override def getLossMessage(loserName: String): String =
    s"$loserName wurde besiegt! Ab zum Pokémon-Center."
}