package de.htwg.se.controller.state

import de.htwg.se.controller.state.ControllerState
import de.htwg.se.model.{GameState, PokemonFactory}

private[controller] case class SelectProfileState(gameState: GameState) extends ControllerState {

    override def currentPhase: String = "select_profile"
    override def prompt: String = gameState.msg1
    override def hint: String = "Gib Profilnamen ein oder [b] für zurück"
    override def allowedInputs: Set[String] = Set("b")

    override def handle(input: String): ControllerState = {
        this
    }
}