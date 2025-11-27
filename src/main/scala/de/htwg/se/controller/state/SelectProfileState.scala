package de.htwg.se.controller.state

import de.htwg.se.controller.state.ControllerState
import de.htwg.se.model.{GameState, PokemonFactory}

case class SelectProfileState(gameState: GameState) extends ControllerState {

    override def handle(input: String): ControllerState = {
        this
    }
}