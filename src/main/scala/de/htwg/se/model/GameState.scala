package de.htwg.se.model


case class GameState(
    player: Player,
    enemy: Player,
    battleOver: Boolean = false,
    msg1: String = "",
    msg2: String = ""
)

