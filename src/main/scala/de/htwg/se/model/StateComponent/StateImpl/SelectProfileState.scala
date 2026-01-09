package de.htwg.se.model.StateComponent.StateImpl


import de.htwg.se.model.{GameState, PokemonFactory}
import de.htwg.se.model.StateComponent.ControllerState

case class SelectProfileState(gameState: GameState) extends ControllerState {

    override def handle(input: String): ControllerState = {
        this
    }
}