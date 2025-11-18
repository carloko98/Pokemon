package de.htwg.se.controller

import de.htwg.se.model.{Pokemon, Attack, PokemonType}
import scala.util.Random


case class Controller(
  // --- Alle Zustände sind 'val' im Konstruktor ---
  val player: Pokemon,
  val enemy: Pokemon,
  val battleOver: Boolean = false,
  val msg1: String = "",
  val msg2: String = ""
) {


  def isBattleOver: Boolean = battleOver
  def getMessage: (String, String) = (msg1, msg2)

  /**
   * Führt einen Angriff aus.
   * Gibt eine *neue* Controller-Instanz mit dem Ergebnis der Runde zurück.
   */
  def doPlayerAttack(attack: Attack): Controller = {
    if (battleOver) return this // Nichts tun, alten Zustand zurückgeben

    // --- Spieler-Angriff ---
    val eff = attack.attackType.effectivenessAgainst(enemy.pType)
    val damage = (attack.damage * eff).toInt
    val enemyAfterAttack = enemy.withHp(enemy.currentHp - damage) // Immutable Model

    // --- Neuer Zustand nach dem Spieler-Angriff ---
    val stateAfterPlayerAttack = this.copy(
      enemy = enemyAfterAttack,
      msg1 = s"${player.name} setzte ${attack.name} ein!",
      msg2 = s"${damage} Schaden an ${enemy.name}!${effMsg(eff)}"
    )
    

    // --- 2. Gegner besiegt? ---
    if (enemyAfterAttack.isFainted) {
      return stateAfterPlayerAttack.endBattle(true) // endBattle gibt finalen Zustand zurück
    }

    // --- 3. GEGNER-ANGRIFF ---
    val rnd = Random()
    val enemyAtk = enemyAfterAttack.attacks(rnd.nextInt(enemyAfterAttack.attacks.size))
    val eff2 = enemyAtk.attackType.effectivenessAgainst(player.pType)
    val damage2 = (enemyAtk.damage * eff2).toInt
    val playerAfterAttack = player.withHp(player.currentHp - damage2) // Immutable Model

    // --- Neuer Zustand nach dem Gegner-Angriff ---
    val stateAfterEnemyAttack = stateAfterPlayerAttack.copy(
      player = playerAfterAttack,
      msg1 = s"${enemyAfterAttack.name} setzte ${enemyAtk.name} ein!",
      msg2 = s"${damage2} Schaden an ${player.name}!${effMsg(eff2)}"
    )



    // Spieler besiegt? 
    if (playerAfterAttack.isFainted) {
      return stateAfterEnemyAttack.endBattle(false)
    }

    // Kampf geht weiter 
    stateAfterEnemyAttack 
  }

  
  def doFlee(): Controller = {
    if (battleOver) return this

    this.copy(
      msg1 = "Du bist geflohen!",
      msg2 = "",
      battleOver = true
    )
  }


  private def endBattle(won: Boolean): Controller = {
    this.copy(
      battleOver = true,
      msg1 = if (won) "Du hast gewonnen!" else "Du hast verloren!",
      msg2 = if (won) s"${enemy.name} ist besiegt!" else s"${player.name} ist besiegt!"
    )
  }
  
  private def effMsg(eff: Double): String = eff match {
    case e if e > 1.0  => " Sehr effektiv!"
    case e if e < 1.0 && e > 0 => " Nicht sehr effektiv..."
    case 0.0           => " Hat keine Wirkung!"
    case _             => ""
  }
}