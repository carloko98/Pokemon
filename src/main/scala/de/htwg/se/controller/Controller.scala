package de.htwg.se.controller

import de.htwg.se.model.{Player, Attack, GameState, Pokemon}
import de.htwg.se.util.Observable
import scala.util.Random

class Controller(initialPlayer: Player, initialEnemy: Player) extends Observable {

  var gameState: GameState = GameState(initialPlayer, initialEnemy)

  // Getter Tui
  def getPlayer: Player = gameState.player
  def getEnemy: Player = gameState.enemy
  def getPlayerPokemon: Pokemon = gameState.player.activePokemon
  def getEnemyPokemon: Pokemon = gameState.enemy.activePokemon

  def isBattleOver: Boolean = gameState.battleOver
  def getMessage: (String, String) = (gameState.msg1, gameState.msg2)



  def doPlayerAttack(attack: Attack): Unit = {
    if (gameState.battleOver) return

    val currentEnemy = gameState.enemy
    val currentPlayer = gameState.player

    val eff = attack.attackType.effectivenessAgainst(currentEnemy.activePokemon.pType)
    val damage = (attack.damage * eff).toInt
    val newEnemyPokemon = currentEnemy.activePokemon.withHp(currentEnemy.activePokemon.currentHp - damage)
    val newEnemy = currentEnemy.updatePokemon(newEnemyPokemon)

    gameState = gameState.copy(
      enemy = newEnemy,
      msg1 = s"${currentPlayer.name} setzte ${attack.name} ein!",
      msg2 = s"${damage} Schaden an ${currentEnemy.name}!${effMsg(eff)}"
    )
    
    notifyObservers()

    if (newEnemy.isActiveFainted) {
      // spaeter pruefen i=ob noch andere pokemon vorhanden
      endBattle(true)
      return
    }

    Thread.sleep(2000)
    doEnemyTurn(newEnemy, currentPlayer)
  }
    private def doEnemyTurn(currentEnemy: Player, currentPlayer: Player): Unit = {
    val rnd = Random()
    val enemyAtk = currentEnemy.activePokemon.attacks(rnd.nextInt(currentEnemy.activePokemon.attacks.size))
    val eff2 = enemyAtk.attackType.effectivenessAgainst(currentPlayer.activePokemon.pType)
    val damage2 = (enemyAtk.damage * eff2).toInt
    val newPlayerPokemon = currentPlayer.activePokemon.withHp(currentPlayer.activePokemon.currentHp - damage2)
    val newPlayer = currentPlayer.updatePokemon(newPlayerPokemon)


    gameState = gameState.copy(
      player = newPlayer,
      msg1 = s"${currentEnemy.name} setzte ${enemyAtk.name} ein!",
      msg2 = s"${damage2} Schaden an ${currentPlayer.name}!${effMsg(eff2)}"
    )

    notifyObservers()

    // spaeter pruefen i=ob noch andere pokemon vorhanden
    if (newPlayer.isActiveFainted) endBattle(false)
  }

  def doFlee(): Unit = {
    if (!gameState.battleOver) {
      gameState = gameState.copy(
        battleOver = true,
        msg1 = "Du bist geflohen!",
        msg2 = ""
      )
      notifyObservers()
    }
  }

  private def endBattle(won: Boolean): Unit = {
    val winnerName = if(won) gameState.player.name else gameState.enemy.name
    val loserPokemon = if(won) gameState.enemy.activePokemon.name else gameState.player.activePokemon.name
    
    gameState = gameState.copy(
      battleOver = true,
      msg1 = if (won) "Gewonnen!" else "Verloren!",
      msg2 = s"$loserPokemon wurde besiegt! Sieger: $winnerName"
    )
    notifyObservers()
  }

  private def effMsg(eff: Double): String = eff match {
    case e if e > 1.0 => " Sehr effektiv!"
    case e if e < 1.0 && e > 0 => " Nicht sehr effektiv..."
    case 0.0 => " Hat keine Wirkung!"
    case _ => ""
  }
}