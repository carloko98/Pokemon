package de.htwg.se.controller

import de.htwg.se.model.{Pokemon, Attack, GameState}
import de.htwg.se.util.Observable
import scala.util.Random

class Controller(player: Pokemon, enemy: Pokemon) extends Observable {

  var gameState: GameState = GameState(player, enemy)

  def getPlayer: Pokemon = gameState.player
  def getEnemy: Pokemon = gameState.enemy
  def isBattleOver: Boolean = gameState.battleOver
  def getMessage: (String, String) = (gameState.msg1, gameState.msg2)



  def doPlayerAttack(attack: Attack): Unit = {
    if (gameState.battleOver) return

    val currentEnemy = gameState.enemy
    val currentPlayer = gameState.player

    // 2. Berechnung
    val eff = attack.attackType.effectivenessAgainst(currentEnemy.pType)
    val damage = (attack.damage * eff).toInt
    val newEnemy = currentEnemy.withHp(currentEnemy.currentHp - damage)

    // 3. STATE UPDATE (mit copy!)
    // Wir erstellen einen neuen State basierend auf dem alten
    gameState = gameState.copy(
      enemy = newEnemy,
      msg1 = s"${currentPlayer.name} setzte ${attack.name} ein!",
      msg2 = s"${damage} Schaden an ${currentEnemy.name}!${effMsg(eff)}"
    )
    
    // 4. Bescheid sagen
    notifyObservers()

    // Check ob Gegner besiegt
    if (newEnemy.isFainted) {
      endBattle(true)
      return
    }

    // --- Gegner Zug ---
    val rnd = Random()
    val enemyAtk = newEnemy.attacks(rnd.nextInt(newEnemy.attacks.size))
    val eff2 = enemyAtk.attackType.effectivenessAgainst(currentPlayer.pType)
    val damage2 = (enemyAtk.damage * eff2).toInt
    val newPlayer = currentPlayer.withHp(currentPlayer.currentHp - damage2)

    // STATE UPDATE 2 (Gegner-Angriff)
    gameState = gameState.copy(
      player = newPlayer,
      msg1 = s"${newEnemy.name} setzte ${enemyAtk.name} ein!",
      msg2 = s"${damage2} Schaden an ${currentPlayer.name}!${effMsg(eff2)}"
    )

    notifyObservers()

    if (newPlayer.isFainted) endBattle(false)
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
    val e = gameState.enemy
    val p = gameState.player
    
    gameState = gameState.copy(
      battleOver = true,
      msg1 = if (won) "Du hast gewonnen!" else "Du hast verloren!",
      msg2 = if (won) s"${e.name} ist besiegt!" else s"${p.name} ist besiegt!"
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