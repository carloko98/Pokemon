package de.htwg.se.model


case class GameState(
    player: Pokemon,
    enemy: Pokemon,
    battleOver: Boolean = false,
    msg1: String = "",
    msg2: String = ""
)

