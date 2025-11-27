package de.htwg.se.model

trait BattleLogic {
  def isFleeingAllowed: Boolean
  def getWinMessage(winnerName: String): String
  def getLossMessage(loserName: String): String
}

object WildBattleLogic extends BattleLogic {
  override def isFleeingAllowed: Boolean = true
  override def getWinMessage(winnerName: String): String = 
    s"Wildes Pokemon besiegt! $winnerName gewinnt an Erfahrung."
  override def getLossMessage(loserName: String): String = 
    s"$loserName wurde besiegt! Ab zum Pokemon Center."
}

object TrainerBattleLogic extends BattleLogic {
  override def isFleeingAllowed: Boolean = false
  override def getWinMessage(winnerName: String): String = 
    s"Trainer besiegt! $winnerName erhält 500 Pokedollar."
  override def getLossMessage(loserName: String): String = 
    s"$loserName ist kampfunfähig! Du verlierst Geld."
}