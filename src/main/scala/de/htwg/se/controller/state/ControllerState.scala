package de.htwg.se.controller.state

import de.htwg.se.model.GameState

trait ControllerState {
  val gameState: GameState
  // Wir geben den neuen State zurück (Functional State Pattern)
  def handle(input: String): ControllerState
}