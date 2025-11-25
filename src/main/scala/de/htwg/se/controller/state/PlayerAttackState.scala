package de.htwg.se.controller.state

import de.htwg.se.controller.state.ControllerState
import de.htwg.se.model.{GameState, Attack, BattleLogic}

// Nimmt jetzt 'logic' im Konstruktor entgegen
case class PlayerAttackState(gameState: GameState, logic: BattleLogic) extends ControllerState {

  override def handle(input: String): ControllerState = input match {
    case "f" | "fliehen" =>
      if (logic.isFleeingAllowed) {
        val newGameState = gameState.copy(
          battleOver = true,
          msg1 = "Du bist geflohen!",
          msg2 = "Zurück im Menü."
        )
        MenuState(newGameState)
      } else {
        val newGameState = gameState.copy(msg2 = "Flucht unmöglich! (Trainer Kampf)")
        copy(gameState = newGameState)
      }

    case s if s.forall(_.isDigit) && s.nonEmpty =>
      val index = s.toInt - 1
      val attacks = gameState.player.activePokemon.attacks
      if (index >= 0 && index < attacks.length) {
        executePlayerAttack(attacks(index))
      } else {
        val newGameState = gameState.copy(msg2 = "Ungueltiger Angriff Index!")
        copy(gameState = newGameState)
      }

    case _ =>
      val newGameState = gameState.copy(msg2 = "Wähle Attacke (1-4) oder [f]liehen")
      copy(gameState = newGameState)
  }

  private def executePlayerAttack(attack: Attack): ControllerState = {
    val currentPlayer = gameState.player
    val currentEnemy = gameState.enemy
    val activePlayerPoke = currentPlayer.activePokemon
    val activeEnemyPoke = currentEnemy.activePokemon


    val eff = attack.attackType.effectivenessAgainst(activeEnemyPoke.pType)
    val damage = (attack.damage * eff).toInt
    val newEnemyPoke = activeEnemyPoke.withHp(activeEnemyPoke.currentHp - damage)
    val newEnemy = currentEnemy.updatePokemon(newEnemyPoke)

    val intermediateGameState = gameState.copy(
      enemy = newEnemy,
      msg1 = s"${activePlayerPoke.name} setzt ${attack.name} ein!",
      msg2 = s"${damage} Schaden!${effMsg(eff)}"
    )

    if (newEnemy.isActiveFainted) {
      val winMsg = logic.getWinMessage(currentPlayer.name)
      
      val winState = intermediateGameState.copy(
        battleOver = true,
        msg1 = "GEWONNEN!",
        msg2 = winMsg
      )
      MenuState(winState)
    } else {
      EnemyAttackState(intermediateGameState, logic)
    }
  }
  
  private def effMsg(eff: Double): String = eff match {
    case e if e > 1.0 => " (Sehr effektiv!)"
    case e if e < 1.0 && e > 0 => " (Nicht sehr effektiv...)"
    case 0.0 => " (Keine Wirkung!)"
    case _ => ""
  }
}