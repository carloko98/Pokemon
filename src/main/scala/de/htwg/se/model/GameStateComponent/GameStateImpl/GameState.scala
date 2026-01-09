package de.htwg.se.model.GameStateComponent.GameStateImpl

import de.htwg.se.model.PlayerComponent.IntPlayer

case class GameState(
    player: IntPlayer,
    enemy: IntPlayer,
    battleOver: Boolean = false,
    msg1: String = "",
    msg2: String = ""
)

