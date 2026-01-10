package de.htwg.se.model.GameStateComponent.GameStateBaseImpl

import de.htwg.se.model.GameStateComponent.IGameState
import de.htwg.se.model.PlayerComponent.IPlayer

case class GameState(
    override val player: IPlayer,
    override val enemy: IPlayer,
    override val battleOver: Boolean = false,
    override val msg1: String = "",
    override val msg2: String = ""
) extends IGameState