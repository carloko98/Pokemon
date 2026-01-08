package de.htwg.se.controller.state

import de.htwg.se.controller.state.ControllerState
import de.htwg.se.model.{GameState, PokemonFactory}


private[controller] case class TitleState(gameState: GameState) extends ControllerState {

    override def currentPhase: String = "title"
    override def prompt: String = gameState.msg1
    override def hint: String = "[n]eues Spiel, [l]aden oder [q]uit"
    override def allowedInputs: Set[String] = Set("n", "neu", "l", "laden", "q", "quit")
    override def handle(input: String): ControllerState = input.toLowerCase match {
        
        case "n" | "neu" =>
            val entryState = gameState.copy(msg1 = "Neues Spiel", msg2 = "Bitte gib deinen Namen ein:")
            NameInputState(entryState)
        
        case "l" | "laden" =>
            val loadState = gameState.copy(msg1 = "Spiel laden", msg2 = "Gib den Namen des Profils ein:")
            SelectProfileState(loadState)
        
        case "q" | "quit" =>
            System.exit(0)
            this

        case _ =>
            val newGame = gameState.copy(msg2 = "[n]eues Spiel, [l]aden oder [q]uit")
            copy(gameState = newGame)
    }
}