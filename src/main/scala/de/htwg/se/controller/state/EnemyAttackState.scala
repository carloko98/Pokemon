package de.htwg.se.controller.state

import de.htwg.se.controller.state.ControllerState
import de.htwg.se.model.{GameState, BattleLogic}
import de.htwg.se.model.{PlayerInterface, PokemonInterface}
import scala.util.Random

private[controller] case class EnemyAttackState(gameState: GameState, logic: BattleLogic) extends ControllerState {

  override def currentPhase: String = "enemy_attack"
  override def prompt: String = gameState.msg1
  override def hint: String = "Gegner greift an... (warte)"
  override def allowedInputs: Set[String] = Set()

  override def handle(input: String): ControllerState = {
    executeEnemyAttack()
  }

  private def executeEnemyAttack(): ControllerState = {
    val currentPlayer: PlayerInterface = gameState.player
    val currentEnemy: PlayerInterface = gameState.enemy

    val activeEnemyPoke: PokemonInterface = currentEnemy.activePokemon
    val activePlayerPoke: PokemonInterface = currentPlayer.activePokemon

    val rnd = new Random()
    val enemyAtk = activeEnemyPoke.attacks(rnd.nextInt(activeEnemyPoke.attacks.size))

    val eff = enemyAtk.attackType.effectivenessAgainst(activePlayerPoke.pType)
    val damage = (enemyAtk.damage * eff).toInt

    val newPlayerPoke: PokemonInterface = activePlayerPoke.withHp(activePlayerPoke.currentHp - damage)
    val newPlayer: PlayerInterface = currentPlayer.updatePokemon(newPlayerPoke)

    val finalGameState = gameState.copy(
      player = newPlayer,
      msg1 = s"${activeEnemyPoke.name} setzt ${enemyAtk.name} ein!",
      msg2 = s"${damage} Schaden!${effMsg(eff)}"
    )

    if (newPlayer.isActiveFainted) {
      val lossMsg = logic.getLossMessage(currentPlayer.name)
      val looseState = finalGameState.copy(
        battleOver = true,
        msg1 = "VERLOREN!",
        msg2 = lossMsg
      )
      MenuState(looseState)
    } else {
      PlayerAttackState(finalGameState, logic)
    }
  }

  private def effMsg(eff: Double): String = eff match {
    case e if e > 1.0 => " (Sehr effektiv!)"
    case e if e < 1.0 && e > 0 => " (Nicht sehr effektiv...)"
    case 0.0 => " (Keine Wirkung!)"
    case _ => ""
  }
}