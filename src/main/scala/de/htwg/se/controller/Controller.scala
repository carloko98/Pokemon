package de.htwg.se.controller

import de.htwg.se.model.{Pokemon, Attack}
import de.htwg.se.util.Observable
import scala.util.Random

class Controller(var player: Pokemon, var enemy: Pokemon) extends Observable {


  var battleOver = false
  var msg1 = ""
  var msg2 = ""

  def getPlayer: Pokemon = player
  def getEnemy: Pokemon = enemy
  def getMessage: (String, String) = (msg1, msg2)

  def doPlayerAttack(attack: Attack): Unit = {
    if (battleOver) return

    val eff = attack.attackType.effectivenessAgainst(enemy.pType)
    val damage = (attack.damage * eff).toInt
    
    enemy = enemy.withHp(enemy.currentHp - damage)
    msg1 = s"${player.name} setzte ${attack.name} ein!"
    msg2 = s"${damage} Schaden an ${enemy.name}!${effMsg(eff)}"
    
    notifyObservers() 

    if (enemy.isFainted) {
      endBattle(true)
      return
    }

    // Gegner greift zurück
    Thread.sleep(2000)
    val rnd = Random()
    val enemyAtk = enemy.attacks(rnd.nextInt(enemy.attacks.size))
    val eff2 = enemyAtk.attackType.effectivenessAgainst(player.pType)
    val damage2 = (enemyAtk.damage * eff2).toInt
    
   
    player = player.withHp(player.currentHp - damage2)
    msg1 = s"${enemy.name} setzte ${enemyAtk.name} ein!"
    msg2 = s"${damage2} Schaden an ${player.name}!${effMsg(eff2)}"
    
    notifyObservers() 

    if (player.isFainted) endBattle(false)
  }

  def doFlee(): Unit = {
    if (!battleOver) {
      msg1 = "Du bist geflohen!"
      msg2 = ""
      battleOver = true
      notifyObservers()
    }
  }

  private def endBattle(won: Boolean): Unit = {
    battleOver = true
    msg1 = if (won) "Du hast gewonnen!" else "Du hast verloren!"
    msg2 = if (won) s"${enemy.name} ist besiegt!" else s"${player.name} ist besiegt!"
    notifyObservers()
  }

  private def effMsg(eff: Double): String = eff match {
    case e if e > 1.0  => " Sehr effektiv!"
    case e if e < 1.0 && e > 0 => " Nicht sehr effektiv..."
    case 0.0           => " Hat keine Wirkung!"
    case _             => ""
  }
}