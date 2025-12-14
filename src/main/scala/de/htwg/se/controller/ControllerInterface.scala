package de.htwg.se.controller

import de.htwg.se.util.Observable
import de.htwg.se.controller.state.ControllerState
import de.htwg.se.model.{Player, Pokemon}
import de.htwg.se.model.PlayerInterface
import de.htwg.se.model.PokemonInterface

trait ControllerInterface extends Observable {

  def handleInput(input: String): Unit
  def undo(): Unit
  def redo(): Unit
  def saveGame(): Unit
  def loadGame(name: String): Unit

  // Daten-Zugriff (Getter für die GUI/TUI)
  def state: ControllerState            
  def getAvailableSaves: List[String]
  def getPlayer: PlayerInterface
  def getEnemy: PlayerInterface
  def getPlayerPokemon: PokemonInterface
  def getEnemyPokemon: PokemonInterface
  def isBattleOver: Boolean
  def getMessage: (String, String)
}