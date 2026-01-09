package de.htwg.se.model.StateComponent

import de.htwg.se.controller.controllerImpl.state
import de.htwg.se.model.{GameState, WildBattleLogic, TrainerBattleLogic}
import de.htwg.se.model.StateComponent.ControllerState
import PlayerAttackState

case class MenuState(gameState: GameState) extends ControllerState {
  
  override def handle(input: String): ControllerState = input match {
    case "s" | "start" =>
      val newGameState = gameState.copy(
        battleOver = false,
        msg1 = "Wilder Kampf gestartet!",
        msg2 = s"Ein wildes ${gameState.enemy.name} taucht auf!"
      )
      PlayerAttackState(newGameState, WildBattleLogic)

    case "t" | "trainer" =>
      val newGameState = gameState.copy(
        battleOver = false,
        msg1 = "Trainerkampf gestartet!",
        msg2 = s"${gameState.enemy.name} fordert dich heraus!"
      )
      PlayerAttackState(newGameState, TrainerBattleLogic)

    case "q" | "quit" =>
      System.exit(0)
      this 

    case _ =>
      val newGameState = gameState.copy(msg1 = "Unbekannter Befehl!", msg2 = "[s]tart (Wild), [t]rainer oder [q]uit")
      copy(gameState = newGameState)
  }
}