package de.htwg.se.controller

import de.htwg.se.model._
import de.htwg.se.util.{Observable, UndoManager, Command}
import de.htwg.se.controller.state._
import de.htwg.se.model.fileio.XmlFileIO
import scala.util.{Success, Failure}

class Controller(initialPlayer: Player, initialEnemy: Player) extends Observable {

  var state: ControllerState = TitleState(GameState(initialPlayer, initialEnemy))
  val fileIo = new XmlFileIO()
  private val undoManager = new UndoManager()

  def undo(): Unit = {
    undoManager.undoStep()
    notifyObservers()
  }

  def redo(): Unit = {
    undoManager.redoStep()
    notifyObservers()
  }

  def setState(newState: ControllerState): Unit = {
    state = newState
    notifyObservers()
  }

  class AttackCommand(input: String) extends Command {
    val oldState = state
    val newState = state.handle(input)

    override def doStep(): Unit = state = newState
    override def undoStep(): Unit = state = oldState
    override def redoStep(): Unit = state = newState
  }

  def handleInput(input: String): Unit = {
    val oldState = state

    input match {
      case "z" | "undo" => undo()
      case "y" | "redo" => redo()
      
      case _ =>
        state match {
          case _: PlayerAttackState | _: EnemyAttackState =>
             undoManager.doStep(new AttackCommand(input))

          case _: SelectProfileState =>
             if (input == "b") state = TitleState(state.gameState)
             else loadGame(input)

          case _ =>
             if (input == "save") saveGame()
             else state = state.handle(input)
        }
    }

    if (state.gameState.battleOver && !state.isInstanceOf[MenuState]) {
      state = MenuState(state.gameState)
    }

    if (wasBattleState(oldState) && state.isInstanceOf[MenuState]) {
      println("Kampf beendet - Automatisches Speichern ...")
      saveGame()
    }

    notifyObservers()
  }

  private def wasBattleState(s: ControllerState): Boolean = {
    s.isInstanceOf[PlayerAttackState] || s.isInstanceOf[EnemyAttackState]
  }

  def saveGame(): Unit = {
    val currentPlayer = state.gameState.player
    fileIo.save(currentPlayer) match {
      case Success(_) => 
        println(s"Spiel gespeichert: ${currentPlayer.name}")
      case Failure(e) => 
        println(s"Fehler beim Speichern: ${e.getMessage}")
    }
  }

  def loadGame(name: String): Unit = {
    fileIo.load(name) match {
      case Success(loadedPlayer) =>
        val newEnemy = PokemonFactory.createRandomEnemy()
        val newGameState = GameState(
          player = loadedPlayer,
          enemy = newEnemy,
          msg1 = "Spielstand geladen!",
          msg2 = s"Willkommen zurück, ${loadedPlayer.name}."
        )
        state = MenuState(newGameState)
        
      case Failure(e) =>
        val errorState = state.gameState.copy(msg2 = s"Konnte Profil '$name' nicht laden: ${e.getMessage}")
        state = SelectProfileState(errorState)
    }
  }

  def getAvailableSaves: List[String] = fileIo.listSaveGames()
  def getPlayer: Player = state.gameState.player
  def getEnemy: Player = state.gameState.enemy
  def getPlayerPokemon: Pokemon = state.gameState.player.activePokemon
  def getEnemyPokemon: Pokemon = state.gameState.enemy.activePokemon
  def isBattleOver: Boolean = state.gameState.battleOver
  def getMessage: (String, String) = (state.gameState.msg1, state.gameState.msg2)
}