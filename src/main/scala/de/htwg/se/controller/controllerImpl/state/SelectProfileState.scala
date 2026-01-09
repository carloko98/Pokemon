package de.htwg.se.controller.controllerImpl.state


import de.htwg.se.model.{GameState, PokemonFactory}

case class SelectProfileState(gameState: GameState) extends ControllerState {

    override def handle(input: String): ControllerState = {
        this
    }
}