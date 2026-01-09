package de.htwg.se.model.StateComponent.StateImpl


import de.htwg.se.model.{GameState, PokemonFactory}
import de.htwg.se.model.StateComponent.ControllerState
import de.htwg.se.model.StateComponent.NameInputState
import de.htwg.se.model.StateComponent.SelectProfileState


case class TitleState(gameState: GameState) extends ControllerState {

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