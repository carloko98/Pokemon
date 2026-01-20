package de.htwg.se.controller.controllerImpl.state

import de.htwg.se.model.PokemonComponent.PokemonService
import de.htwg.se.model.GameStateComponent.GameState

case class NameInputState(gameState: GameState) extends ControllerState {

    override def handle(input: String): ControllerState = {
        if (input.trim.isEmpty) {
            val newGS = gameState.copy(msg2 = "Name darf nicht leer sein!")
            copy(gameState = newGS)
        } else {
            val playerName = input.trim
            val newPlayer = PokemonService.createRandomPlayer(playerName)
            val newEnemy = PokemonService.createRandomEnemy()

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