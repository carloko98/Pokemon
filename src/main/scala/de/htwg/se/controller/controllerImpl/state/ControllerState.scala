package de.htwg.se.controller.controllerImpl.state

import de.htwg.se.model.GameStateComponent.GameStateBaseImpl.GameState

trait ControllerState {
  val gameState: GameState
  def handle(input: String): ControllerState
}