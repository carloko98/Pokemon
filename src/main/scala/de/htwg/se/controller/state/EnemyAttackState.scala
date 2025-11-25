package de.htwg.se.controller.state

import de.htwg.se.model.GameState
import scala.util.Random

case class EnemyTurnState(gameState: GameState) extends ControllerState {

  override def handle(input: String): ControllerState = {
    executeEnemyAttack()
  }

  private def executeEnemyAttack(): ControllerState = {
    val currentPlayer = gameState.player
    val currentEnemy = gameState.enemy
    val activeEnemyPoke = currentEnemy.activePokemon
    val activePlayerPoke = currentPlayer.activePokemon

    // --- SIMPLE KI ---
    val rnd = new Random()
    val enemyAtk = activeEnemyPoke.attacks(rnd.nextInt(activeEnemyPoke.attacks.size))
    val eff = enemyAtk.attackType.effectivenessAgainst(activePlayerPoke.pType)
    val damage = (enemyAtk.damage * eff).toInt
    val newPlayerPoke = activePlayerPoke.withHp(activePlayerPoke.currentHp - damage)
    val newPlayer = currentPlayer.updatePokemon(newPlayerPoke)

    val finalGameState = gameState.copy(
      player = newPlayer,
      msg1 = s"${activeEnemyPoke.name} setzt ${enemyAtk.name} ein!",
      msg2 = s"${damage} Schaden!${effMsg(eff)}"
    )

    if (newPlayer.isActiveFainted) {
       val looseState = finalGameState.copy(
        battleOver = true,
        msg1 = "Verloren!",
        msg2 = s"${newPlayer.activePokemon.name} besiegt! Zurück im Menü."
      )
        MenuState(looseState)
    } else {
      PlayerAttackState(finalGameState)
    }
  }

  private def effMsg(eff: Double): String = eff match {
    case e if e > 1.0 => " (Sehr effektiv!)"
    case e if e < 1.0 && e > 0 => " (Nicht sehr effektiv...)"
    case 0.0 => " (Keine Wirkung!)"
    case _ => ""
  }
}