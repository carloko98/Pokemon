package de.htwg.se.controller

import de.htwg.se.model._
import de.htwg.se.util.Observable
import de.htwg.se.controller.state.{ControllerState, MenuState}
import de.htwg.se.controller.state.PlayerAttackState

class Controller(initialPlayer: Player, initialEnemy: Player) extends Observable {

  // Die EINZIGE Variable für den Zustand + Daten
  var state: ControllerState = MenuState(GameState(initialPlayer, initialEnemy))

  def handleInput(input: String): Unit = {
    state = state.handle(input)
    notifyObservers()
  }

  def getPlayer: Player = state.gameState.player
  def getEnemy: Player = state.gameState.enemy
  // Hilfsgetter für TUI (unverändert)
  def getPlayerPokemon: Pokemon = state.gameState.player.activePokemon
  def getEnemyPokemon: Pokemon = state.gameState.enemy.activePokemon
  def isBattleOver: Boolean = state.gameState.battleOver
  def getMessage: (String, String) = (state.gameState.msg1, state.gameState.msg2)
}