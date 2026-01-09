package de.htwg.se.model

import de.htwg.se.model.PlayerInterface

case class GameState(
    player: PlayerInterface,
    enemy: PlayerInterface,
    battleOver: Boolean = false,
    msg1: String = "",
    msg2: String = ""
)

