package de.htwg.se.controller

import de.htwg.se.util.Observable
import de.htwg.se.controller.state.ControllerState
import de.htwg.se.model.{Player, Pokemon}

trait ControllerInterface extends Observable {
  // Aktionen (Methoden, die etwas tun)
  def handleInput(input: String): Unit
  def undo(): Unit
  def redo(): Unit
  def saveGame(): Unit
  def loadGame(name: String): Unit

  // Daten-Zugriff (Getter für die GUI/TUI)
  def state: ControllerState            // Var im Controller implementiert Def im Interface
  def getAvailableSaves: List[String]
  def getPlayer: Player
  def getEnemy: Player
  def getPlayerPokemon: Pokemon
  def getEnemyPokemon: Pokemon
  def isBattleOver: Boolean
  def getMessage: (String, String)
}