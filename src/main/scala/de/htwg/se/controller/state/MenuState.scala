package de.htwg.se.controller.state

import de.htwg.se.model.GameState

case class MenuState(gameState: GameState) extends ControllerState {
  
  override def handle(input: String): ControllerState = input match {
    case "s" | "start" =>
      val newGameState = gameState.copy(
        battleOver = false,
        msg1 = "Kampf gestartet!",
        msg2 = s"${gameState.enemy.name} fordert dich heraus!"
      )
      PlayerAttackState(newGameState)

    case "q" | "quit" =>
      System.exit(0)
      this 

    case _ =>
      val newGameState = gameState.copy(msg1 = "Unbekannter Befehl!", msg2 = "[s]tart oder [q]uit")
      copy(gameState = newGameState)
  }
}