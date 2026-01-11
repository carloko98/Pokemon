package de.htwg.se.controller.controllerImpl.state

import de.htwg.se.model.GameStateComponent.GameState

case class SelectProfileState(gameState: GameState) extends ControllerState {

  override def handle(input: String): ControllerState = input.toLowerCase match {
    case "b" => TitleState(gameState)
    case _ => this
  }
}