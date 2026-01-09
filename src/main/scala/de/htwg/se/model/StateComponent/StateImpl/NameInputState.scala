package de.htwg.se.model.StateComponent.StateImpl

import de.htwg.se.controller.ControllerInterface
import de.htwg.se.model.{GameState, PokemonFactory}
import de.htwg.se.model.StateComponent.ControllerState
import de.htwg.se.model.StateComponent.MenuState

case class NameInputState(gameState: GameState) extends ControllerState {

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
