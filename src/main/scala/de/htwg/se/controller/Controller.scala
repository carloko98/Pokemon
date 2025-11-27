// src/main/scala/de/htwg/se/controller/Controller.scala
package de.htwg.se.controller

import de.htwg.se.model.Command._
import de.htwg.se.model.{Pokemon, GameState, Command, BattleEngine}
import de.htwg.se.util.Observable

class Controller(player: Pokemon, enemy: Pokemon) extends Observable {

  private var gameState: GameState = GameState(player, enemy)
  private val engine = new BattleEngine(this)  // Receiver!

  // --- Getter ---
  def getPlayer: Pokemon = gameState.player
  def getEnemy: Pokemon = gameState.enemy
  def isBattleOver: Boolean = gameState.battleOver
  def getMessage: (String, String) = (gameState.msg1, gameState.msg2)

  // --- Command Pattern Entry Point ---
  def execute(command: Command): Unit = command match {
    case AttackCommand(attack) => engine.performAttack(attack)
    case FleeCommand           => engine.performFlee()
  }

  // --- Hilfsmethode für den Receiver ---
  def updateState(
    player: Pokemon = gameState.player,
    enemy: Pokemon = gameState.enemy,
    battleOver: Boolean = gameState.battleOver,
    msg1: String = "",
    msg2: String = ""
  ): Unit = {
    gameState = gameState.copy(
      player = player,
      enemy = enemy,
      battleOver = battleOver,
      msg1 = msg1,
      msg2 = msg2
    )
    notifyObservers()
  }

  def endBattle(won: Boolean): Unit = {
    val e = gameState.enemy
    val p = gameState.player
    
    updateState(
      battleOver = true,
      msg1 = if (won) "Du hast gewonnen!" else "Du hast verloren!",
      msg2 = if (won) s"${e.name} ist besiegt!" else s"${p.name} ist besiegt!"
    )
  }
}