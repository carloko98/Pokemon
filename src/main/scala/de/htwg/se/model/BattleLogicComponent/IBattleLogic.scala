package de.htwg.se.model.BattleLogicComponent


trait IBattleLogic {
  def isFleeingAllowed: Boolean
  def getWinMessage(winnerName: String): String
  def getLossMessage(loserName: String): String
}