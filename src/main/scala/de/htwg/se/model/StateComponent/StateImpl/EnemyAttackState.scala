package de.htwg.se.model.StateComponent


import de.htwg.se.model.GameStateComponent.GameStateImpl.GameState
import de.htwg.se.model.BattleComponent.BattleLogic
import de.htwg.se.model.PlayerComponent.IntPlayer
import de.htwg.se.model.PokemonComponent.IntPokemon
import scala.util.Random



case class EnemyAttackState(gameState: GameState, logic: BattleLogic) extends ControllerState {

  override def handle(input: String): ControllerState = {
    executeEnemyAttack()
  }

  private def executeEnemyAttack(): ControllerState = {
    val currentPlayer: IntPlayer = gameState.player
    val currentEnemy: IntPlayer = gameState.enemy

    val activeEnemyPoke: IntPokemon = currentEnemy.activePokemon
    val activePlayerPoke: IntPokemon = currentPlayer.activePokemon
    val rnd = new Random()
    val enemyAtk = activeEnemyPoke.attacks(rnd.nextInt(activeEnemyPoke.attacks.size))

    val eff = enemyAtk.attackType.effectivenessAgainst(activePlayerPoke.pType)
    val damage = (enemyAtk.damage * eff).toInt

    val newPlayerPoke: IntPokemon = activePlayerPoke.withHp(activePlayerPoke.currentHp - damage)
    val newPlayer: IntPlayer = currentPlayer.updatePokemon(newPlayerPoke)

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