// src/main/scala/de/htwg/se/model/BattleEngine.scala
package de.htwg.se.model
import de.htwg.se.controller.Controller

import scala.util.Random

class BattleEngine(controller: Controller) {

  def performAttack(attack: Attack): Unit = {
    if (controller.isBattleOver) return

    val player = controller.getPlayer
    val enemy = controller.getEnemy

    val eff = attack.attackType.effectivenessAgainst(enemy.pType)
    val damage = (attack.damage * eff).toInt
    val newEnemy = enemy.withHp(enemy.currentHp - damage)

    controller.updateState(
      enemy = newEnemy,
      msg1 = s"${player.name} setzte ${attack.name} ein!",
      msg2 = s"${damage} Schaden an ${enemy.name}!${effMsg(eff)}"
    )

    if (newEnemy.isFainted) {
      controller.endBattle(won = true)
      return
    }

    // --- Gegner Zug ---
    Thread.sleep(2000)
    val rnd = Random()
    val enemyAtk = newEnemy.attacks(rnd.nextInt(newEnemy.attacks.size))
    val eff2 = enemyAtk.attackType.effectivenessAgainst(player.pType)
    val damage2 = (enemyAtk.damage * eff2).toInt
    val newPlayer = player.withHp(player.currentHp - damage2)

    controller.updateState(
      player = newPlayer,
      msg1 = s"${newEnemy.name} setzte ${enemyAtk.name} ein!",
      msg2 = s"${damage2} Schaden an ${player.name}!${effMsg(eff2)}"
    )

    if (newPlayer.isFainted) {
      controller.endBattle(won = false)
    }
  }

  def performFlee(): Unit = {
    if (!controller.isBattleOver) {
      controller.updateState(
        battleOver = true,
        msg1 = "Du bist geflohen!",
        msg2 = ""
      )
    }
  }

  private def effMsg(eff: Double): String = eff match {
    case e if e > 1.0 => " Sehr effektiv!"
    case e if e < 1.0 && e > 0 => " Nicht sehr effektiv..."
    case 0.0 => " Hat keine Wirkung!"
    case _ => ""
  }
}