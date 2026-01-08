package de.htwg.se.controller

import de.htwg.se.model._
import de.htwg.se.util.{Observable, UndoManager, Command}
import de.htwg.se.controller.state._
import de.htwg.se.model.fileio.{FileIOInterface, XmlFileIO}
import scala.util.{Success, Failure}

class Controller(initialPlayer: PlayerInterface, initialEnemy: PlayerInterface) extends ControllerInterface {

  private var currentState: ControllerState = TitleState(GameState(initialPlayer, initialEnemy))

  def currentPhase: String = currentState.currentPhase
  def prompt: String = currentState.prompt
  def hint: String = currentState.hint
  def allowedInputs: Set[String] = currentState.allowedInputs

  val fileIo: FileIOInterface = new XmlFileIO()
  private val undoManager = new UndoManager()

  def undo(): Unit = {
    undoManager.undoStep()
    notifyObservers()
  }

  def redo(): Unit = {
    undoManager.redoStep()
    notifyObservers()
  }

  

  class AttackCommand(input: String) extends Command {
    private val oldState = currentState
    private val newState = oldState.handle(input)
    override def doStep(): Unit = currentState = newState
    override def undoStep(): Unit = currentState = oldState
    override def redoStep(): Unit = currentState = newState
  }

  def handleInput(input: String): Unit = {
  val normalizedInput = input.trim.toLowerCase

  normalizedInput match {
    case "z" | "undo" => undo()
    case "y" | "redo" => redo()
    case "save" => saveGame()
    case _ =>
      if (currentPhase == "select_profile") {
        if (normalizedInput == "b") {
          currentState = TitleState(currentState.gameState.copy(
            msg1 = "Pokemon Scala Edition",
            msg2 = "[n]eues Spiel, [l]aden oder [q]uit"
          ))
        } else {
          // Hier kommt der Fix: Profilname → loadGame direkt aufrufen!
          loadGame(normalizedInput)
          return  // Wichtig: Nicht weiter delegieren!
        }
      } else if (currentPhase == "player_attack" || currentPhase == "enemy_attack") {
        undoManager.doStep(new AttackCommand(normalizedInput))
      } else {
        currentState = currentState.handle(normalizedInput)
      }
  }

  // Automatischer Übergang nach Kampfende
  if (currentState.gameState.battleOver && currentPhase != "menu") {
    currentState = MenuState(currentState.gameState)
    saveGame()
  }

  notifyObservers()
}

  private def wasBattleState(s: ControllerState): Boolean = {
    s.isInstanceOf[PlayerAttackState] || s.isInstanceOf[EnemyAttackState]
  }

  def saveGame(): Unit = {
    val currentPlayerInterface = currentState.gameState.player
    currentPlayerInterface match {
      case concretePlayer: Player =>
        fileIo.save(concretePlayer) match {
          case Success(_) => 
            println(s"Spiel gespeichert: ${concretePlayer.name}")
          case Failure(e) => 
            println(s"Fehler beim Speichern: ${e.getMessage}")
        }
      case _ =>
        println("Fehler: Kann nur konkrete Player-Objekte speichern.")
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
        currentState = MenuState(newGameState)
        notifyObservers()
        
      case Failure(e) =>
        val errorState = currentState.gameState.copy(msg2 = s"Konnte Profil '$name' nicht laden: ${e.getMessage}")
        currentState = SelectProfileState(errorState)
        notifyObservers()
    }
  }

  def getAvailableSaves: List[String] = fileIo.listSaveGames()
  def getPlayer: PlayerInterface = currentState.gameState.player
  def getEnemy: PlayerInterface = currentState.gameState.enemy
  def getPlayerPokemon: PokemonInterface = currentState.gameState.player.activePokemon
  def getEnemyPokemon: PokemonInterface = currentState.gameState.enemy.activePokemon
  def isBattleOver: Boolean = currentState.gameState.battleOver
  def getMessage: (String, String) = (currentState.gameState.msg1, currentState.gameState.msg2)
}