package de.htwg.se.model.StateComponent

import de.htwg.se.model.GameStateComponent.GameState

trait ControllerState {
  val gameState: GameState
  def handle(input: String): ControllerState
}