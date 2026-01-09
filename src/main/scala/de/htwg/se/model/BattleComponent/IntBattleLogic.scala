package de.htwg.se.model.BattleComponent

trait BattleLogic {
  def isFleeingAllowed: Boolean
  def getWinMessage(winnerName: String): String
  def getLossMessage(loserName: String): String
}

