package de.htwg.se.controller.controllerImpl

import com.google.inject.Inject
import de.htwg.se.model.FileIOComponent.IFileIO
import de.htwg.se.util.{Observable, UndoManager, Command}
import de.htwg.se.controller.{IController, ViewState}
import scala.util.{Success, Failure}

// Alias Imports für die ViewStates
import de.htwg.se.controller.{TitleState => VTitle, MenuState => VMenu, PlayerAttackState => VPlayerAtk, EnemyAttackState => VEnemyAtk, NameInputState => VNameInput, SelectProfileState => VSelectProfile}

// Interne States
import de.htwg.se.controller.controllerImpl.state._

// Model Imports
import de.htwg.se.model.PlayerComponent.IPlayer
import de.htwg.se.model.GameStateComponent.GameStateBaseImpl.GameState
import de.htwg.se.model.PokemonComponent.PokemonBaseImpl.PokemonFactory
import de.htwg.se.model.PokemonComponent.IPokemon

class Controller @Inject() (
  initialPlayer: IPlayer,
  initialEnemy: IPlayer,
  val fileIo: IFileIO
) extends IController {

  private var internalState: ControllerState = TitleState(GameState(initialPlayer, initialEnemy))

  private val undoManager = new UndoManager()

  override def viewState: ViewState = internalState match {
    case _: TitleState         => VTitle
    case _: MenuState          => VMenu
    case _: PlayerAttackState  => VPlayerAtk
    case _: EnemyAttackState   => VEnemyAtk
    case _: NameInputState     => VNameInput
    case _: SelectProfileState => VSelectProfile
  }

  def undo(): Unit = {
    undoManager.undoStep()
    notifyObservers()
  }

  def redo(): Unit = {
    undoManager.redoStep()
    notifyObservers()
  }

  def setState(newState: ControllerState): Unit = {
    internalState = newState
    notifyObservers()
  }

  class AttackCommand(input: String) extends Command {
    val oldState = internalState
    val newState = internalState.handle(input)

    override def doStep(): Unit = internalState = newState
    override def undoStep(): Unit = internalState = oldState
    override def redoStep(): Unit = internalState = newState
  }

  def handleInput(input: String): Unit = {
    val oldState = internalState

    input match {
      case "z" | "undo" => undo()
      case "y" | "redo" => redo()
      
      case _ =>
        internalState match {
          case _: PlayerAttackState | _: EnemyAttackState =>
             undoManager.doStep(new AttackCommand(input))

          case _: SelectProfileState =>
             if (input == "b") internalState = TitleState(internalState.gameState)
             else loadGame(input)

          case _ =>
             if (input == "save") saveGame()
             else internalState = internalState.handle(input)
        }
    }

    if (internalState.gameState.battleOver && !internalState.isInstanceOf[MenuState]) {
      internalState = MenuState(internalState.gameState)
    }

    if (wasBattleState(oldState) && internalState.isInstanceOf[MenuState]) {
      println("Kampf beendet - Automatisches Speichern ...")
      saveGame()
    }

    notifyObservers()
  }

  private def wasBattleState(s: ControllerState): Boolean = {
    s.isInstanceOf[PlayerAttackState] || s.isInstanceOf[EnemyAttackState]
  }

  def saveGame(): Unit = {
    val currentPlayer = internalState.gameState.player
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
        internalState = MenuState(newGameState)
        
      case Failure(e) =>
        val errorState = internalState.gameState.copy(msg2 = s"Konnte Profil '$name' nicht laden: ${e.getMessage}")
        internalState = SelectProfileState(errorState)
    }
  }

  def getAvailableSaves: List[String] = fileIo.listSaveGames()
  
  def getPlayer: de.htwg.se.model.PlayerComponent.PlayerBaseImpl.Player = 
    internalState.gameState.player.asInstanceOf[de.htwg.se.model.PlayerComponent.PlayerBaseImpl.Player]
    
  def getEnemy: de.htwg.se.model.PlayerComponent.PlayerBaseImpl.Player = 
    internalState.gameState.enemy.asInstanceOf[de.htwg.se.model.PlayerComponent.PlayerBaseImpl.Player]
    
  def getPlayerPokemon: IPokemon = internalState.gameState.player.activePokemon
  def getEnemyPokemon: IPokemon = internalState.gameState.enemy.activePokemon
  def isBattleOver: Boolean = internalState.gameState.battleOver
  def getMessage: (String, String) = (internalState.gameState.msg1, internalState.gameState.msg2)
}