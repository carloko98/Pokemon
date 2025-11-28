package de.htwg.se.controller

import de.htwg.se.model._
import de.htwg.se.util.Observable
import de.htwg.se.controller.state._
import de.htwg.se.model.fileio.XmlFileIO

class Controller(initialPlayer: Player, initialEnemy: Player) extends Observable {

  var state: ControllerState = TitleState(GameState(initialPlayer, initialEnemy))
  val fileIo = new XmlFileIO()

  def handleInput(input: String): Unit = {
    val oldState = state

    state match {
      case _: SelectProfileState =>
        if(input == "b") state = TitleState(state.gameState)
        else loadGame(input)

      case _ =>
        if (input == "save") saveGame()
        else state = state.handle(input)
    }

    // Prüfen, ob Battle vorbei ist und direkt in MenuState wechseln
    if (state.gameState.battleOver && !state.isInstanceOf[MenuState]) {
      state = MenuState(state.gameState)
    }

    // Auto-Save nach Battle
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
    fileIo.save(currentPlayer)
    println(s"Spiel gespeichert: ${currentPlayer.name}")
  }

  def loadGame(name: String): Unit = {
    try {
      val loadedPlayer = fileIo.load(name)
      val newEnemy = PokemonFactory.createRandomEnemy()
      val newGameState = GameState(
        player = loadedPlayer,
        enemy = newEnemy,
        msg1 = "Spielstand geladen!",
        msg2 = s"Willkommen zurück, ${loadedPlayer.name}."
      )
      state = MenuState(newGameState)
    } catch {
      case e: Exception =>
        val errorState = state.gameState.copy(msg2 = s"Profil '$name' nicht gefunden!")
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
