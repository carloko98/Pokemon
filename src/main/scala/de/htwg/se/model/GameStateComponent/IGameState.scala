package de.htwg.se.model.GameStateComponent

import de.htwg.se.model.PlayerComponent.IPlayer

trait IGameState {
  def player: IPlayer
  def enemy: IPlayer
  def battleOver: Boolean
  def msg1: String
  def msg2: String
}