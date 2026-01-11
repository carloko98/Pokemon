package de.htwg.se.controller.controllerImpl.state

import de.htwg.se.model.GameStateComponent.GameState
import de.htwg.se.model.BattleLogicComponent.IBattleLogic
import de.htwg.se.model.PokemonComponent.IPokemon
import de.htwg.se.model.PlayerComponent.IPlayer
import scala.util.Random

case class EnemyAttackState(gameState: GameState, logic: IBattleLogic) extends ControllerState {

  override def handle(input: String): ControllerState = {
    executeEnemyAttack()
  }

  private def executeEnemyAttack(): ControllerState = {
    val currentPlayer: IPlayer = gameState.player
    val currentEnemy: IPlayer = gameState.enemy

    val activeEnemyPoke: IPokemon = currentEnemy.activePokemon
    val activePlayerPoke: IPokemon = currentPlayer.activePokemon

    val rnd = new Random()
    val enemyAtk = activeEnemyPoke.attacks(rnd.nextInt(activeEnemyPoke.attacks.size))

    val eff = enemyAtk.attackType.effectivenessAgainst(activePlayerPoke.pType)
    val damage = (enemyAtk.damage * eff).toInt

    val newPlayerPoke: IPokemon = activePlayerPoke.withHp(activePlayerPoke.currentHp - damage)
    val newPlayer: IPlayer = currentPlayer.updatePokemon(newPlayerPoke)

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