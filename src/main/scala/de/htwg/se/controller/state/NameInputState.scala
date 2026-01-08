package de.htwg.se.controller.state

import de.htwg.se.controller.state.ControllerState
import de.htwg.se.model.{GameState, PokemonFactory}

private[controller] case class NameInputState(gameState: GameState) extends ControllerState {

    override def currentPhase: String = "name_input"
    override def prompt: String = gameState.msg1
    override def hint: String = gameState.msg2  // "Bitte gib deinen Namen ein:"
    override def allowedInputs: Set[String] = Set()  // Freitext → keine festen Inputs

    override def handle(input: String): ControllerState = {
        if (input.trim.isEmpty) {
            val newGS = gameState.copy(msg2 = "Name darf nicht leer sein!")
            copy(gameState = newGS)
        }else {
            val playerName = input.trim
            val newPlayer = PokemonFactory.createPlayer(playerName, Vector("Glurak", "Bisaflor"))
            val newEnemy = PokemonFactory.createRandomEnemy()

            val newGame = GameState(
                player = newPlayer, 
                enemy = newEnemy, 
                msg1 = s"Hallo $playerName!", 
                msg2 = "Willkommen in der Welt der Pokemon!"
            )

            MenuState(newGame)
        }
    }
}
