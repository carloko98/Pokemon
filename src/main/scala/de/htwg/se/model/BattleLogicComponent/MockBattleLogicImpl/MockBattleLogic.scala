package de.htwg.se.model.BattleLogicComponent.MockBattleLogicImpl

import de.htwg.se.model.BattleLogicComponent.IBattleLogic

class MockBattleLogic extends IBattleLogic {

  var _allowFleeing: Boolean = true

  override def isFleeingAllowed: Boolean = _allowFleeing

  override def getWinMessage(winnerName: String): String = {
    s"Mock-Nachricht: $winnerName hat gewonnen!"
  }

  override def getLossMessage(loserName: String): String = {
    s"Mock-Nachricht: $loserName hat verloren!"
  }
}