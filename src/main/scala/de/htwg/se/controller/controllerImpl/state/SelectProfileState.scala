package de.htwg.se.controller.controllerImpl.state


import de.htwg.se.model.{PokemonFactory}
import de.htwg.se.model.GameStateComponent.GameStateBaseImpl.GameState

case class SelectProfileState(gameState: GameState) extends ControllerState {

    override def handle(input: String): ControllerState = {
        this
    }
}