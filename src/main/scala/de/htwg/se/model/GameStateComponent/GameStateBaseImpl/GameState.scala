package de.htwg.se.model.GameStateComponent.GameStateBaseImpl

import de.htwg.se.model.PlayerComponent.IPlayer

case class GameState(
    player: IPlayer,
    enemy: IPlayer,
    battleOver: Boolean = false,
    msg1: String = "",
    msg2: String = ""
)

