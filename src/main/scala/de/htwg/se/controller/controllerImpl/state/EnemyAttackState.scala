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
    // Sicherheits-Check falls moves.json leer war, sonst Absturz bei nextInt(0)
    val attacks = if (activeEnemyPoke.attacks.nonEmpty) activeEnemyPoke.attacks else Vector(de.htwg.se.model.PokemonComponent.Attack("Verzweifler", 10, de.htwg.se.model.PokemonComponent.PokemonType.Normal))
    val enemyAtk = attacks(rnd.nextInt(attacks.size))

    val eff1 = enemyAtk.attackType.effectivenessAgainst(activePlayerPoke.pType)
    val eff2 = activePlayerPoke.secondaryType match {
      case Some(t2) => enemyAtk.attackType.effectivenessAgainst(t2)
      case None => 1.0
    }
    val eff = eff1 * eff2
    val damage = (enemyAtk.damage * eff).toInt

    val newPlayerPoke: IPokemon = activePlayerPoke.withHp(activePlayerPoke.currentHp - damage)
    val newPlayer: IPlayer = currentPlayer.updatePokemon(newPlayerPoke)

    // Zwischenstand mit Schaden (noch kein Game Over Status)
    val damageState = gameState.copy(
      player = newPlayer,
      msg1 = s"${activeEnemyPoke.name} setzt ${enemyAtk.name} ein!",
      msg2 = s"${damage} Schaden!${effMsg(eff)}"
    )

    // HIER IST DIE WICHTIGE ÄNDERUNG:
    if (newPlayer.isActiveFainted) {
      
      // Prüfen: Hast du noch ein anderes Pokemon?
      newPlayer.nextAlivePokemonIndex match {
        
        case Some(idx) =>
          // JA: Wir wechseln automatisch dein Pokemon
          val nextPlayer = newPlayer.switchActivePokemon(idx)
          val nextPokeName = nextPlayer.activePokemon.name
          
          val switchState = damageState.copy(
            player = nextPlayer,
            msg1 = s"${activePlayerPoke.name} wurde besiegt!",
            msg2 = s"Du schickst $nextPokeName in den Kampf!"
          )
          
          // Du bist wieder dran (mit dem neuen Pokemon)
          PlayerAttackState(switchState, logic)

        case None =>
          // NEIN: Keine Pokemon mehr -> JETZT ist verloren
          val lossMsg = logic.getLossMessage(currentPlayer.name)
          val looseState = damageState.copy(
            battleOver = true,
            msg1 = "VERLOREN!",
            msg2 = lossMsg
          )
          MenuState(looseState)
      }
      
    } else {
      // Pokemon lebt noch -> Du bist dran
      PlayerAttackState(damageState, logic)
    }
  }

  private def effMsg(eff: Double): String = eff match {
    case e if e > 1.0 => " (Sehr effektiv!)"
    case e if e < 1.0 && e > 0 => " (Nicht sehr effektiv...)"
    case 0.0 => " (Keine Wirkung!)"
    case _ => ""
  }
}