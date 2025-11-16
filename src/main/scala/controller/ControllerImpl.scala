package de.htwg.controller

import de.htwg.model.{Pokemon, Attack, PokemonType}
import scala.util.Random

/**
 * Controller-Implementierung (MVC-Schicht: Controller)
 * 
 * Verantwortlich für:
 * - Spiel-Logik (Angriff, Flucht, Sieg/Niederlage)
 * - Zustandsverwaltung (Spieler, Gegner, Nachrichten)
 * - Observer-Benachrichtigung (View wird aktualisiert)
 * 
 * WICHTIG: **Keine UI-Logik!** → Nur Zustandsänderung + Model-N (Typ-Effektivität)
 */
class ControllerImpl(var player: Pokemon, var enemy: Pokemon) extends Controller {

  // ==================== ZUSTAND (privat, mutable nur hier!) ====================
  private var battleOver = false                    // Ist der Kampf vorbei?
  private var playerWon = false                     // Hat der Spieler gewonnen?
  private var msg1 = ""                             // Zeile 1 der Ausgabe
  private var msg2 = ""                             // Zeile 2 der Ausgabe
  private val observers = scala.collection.mutable.ListBuffer[Observer]()  // Observer-Liste

  //keie´ne vars

  // ==================== OBSERVER-PATTERN (Vorlesung: Inversion of Control) ====================
  override def addObserver(o: Observer): Unit = observers += o
  override def notifyObservers(): Unit = observers.foreach(_.update())

  // Getter für View
  override def isBattleOver: Boolean = battleOver
  override def getMessage: (String, String) = (msg1, msg2)

  // ==================== SPIELER-ANGRIFF (Haupt-Logik) ====================
  /**
   * Führt einen Angriff des Spielers aus.
   * 
   * 1. Prüft, ob Kampf vorbei → return
   * 2. Berechnet Schaden mit Typ-Effektivität (Model!)
   * 3. Aktualisiert Gegner-HP (immutable → neues Pokemon)
   * 4. Setzt Nachrichten
   * 5. Benachrichtigt Observer
   * 6. Prüft auf KO → endBattle
   * 7. Gegner greift zurück (zufällig)
   */
  override def doPlayerAttack(attack: Attack): Unit = {
    if (battleOver) return  // Kampf vorbei → nichts tun

    // --- 1. Typ-Effektivität aus Model holen ---
    val eff = attack.attackType.effectivenessAgainst(enemy.pType)  // 2.0 / 0.5 / 0.0 / 1.0
    val damage = (attack.damage * eff).toInt                        // z.B. 8 * 2.0 = 16

    // --- 2. Gegner-HP aktualisieren (immutable!) ---
    enemy = enemy.withHp(enemy.currentHp - damage)

    // --- 3. Nachrichten setzen ---
    msg1 = s"${player.name} setzte ${attack.name} ein!"
    msg2 = s"${damage} Schaden an ${enemy.name}!${effMsg(eff)}"

    // --- 4. View aktualisieren ---
    notifyObservers()

    // --- 5. Gegner besiegt? ---
    if (enemy.isFainted) { 
      endBattle(true)   // Spieler gewinnt
      return 
    }

    // --- 6. GEGNER-ANGRIFF (zufällig, wie in echtem Pokémon) ---
    val rnd = Random()
    val enemyAtk = enemy.attacks(rnd.nextInt(enemy.attacks.size))  // Zufällige Attacke
    val eff2 = enemyAtk.attackType.effectivenessAgainst(player.pType)  // KORREKT!
    val damage2 = (enemyAtk.damage * eff2).toInt

    // --- 7. Spieler-HP aktualisieren ---
    player = player.withHp(player.currentHp - damage2)

    // --- 8. Nachrichten für Gegner-Angriff ---
    msg1 = s"${enemy.name} setzte ${enemyAtk.name} ein!"
    msg2 = s"${damage2} Schaden an ${player.name}!${effMsg(eff2)}"

    // --- 9. View aktualisieren ---
    notifyObservers()

    // --- 10. Spieler besiegt? ---
    if (player.isFainted) endBattle(false)
  }

  // ==================== FLUCHT ====================
  /**
   * Spieler flieht → Kampf sofort beendet
   */
  override def doFlee(): Unit = {
    if (!battleOver) {
      msg1 = "Du bist geflohen!"
      msg2 = ""
      battleOver = true
      notifyObservers()  // View zeigt Flucht an
    }
  }

  // ==================== KAMPF-ENDE (Hilfsmethode) ====================
  /**
   * Beendet den Kampf mit Sieg/Niederlage
   * 
   * @param won true = Spieler hat gewonnen
   */
  private def endBattle(won: Boolean): Unit = {
    battleOver = true
    playerWon = won
    msg1 = if (won) "Du hast gewonnen!" else "Du hast verloren!"
    msg2 = if (won) s"${enemy.name} ist besiegt!" else s"${player.name} ist besiegt!"
    notifyObservers()  // View zeigt Endbildschirm
  }

  // ==================== NACHRICHTEN FÜR TYP-EFFEKTIVITÄT ====================
  /**
   * Wandelt Multiplikator in Text um (wie in Pokémon-Spielen)
   * 
   * @param eff 2.0, 0.5, 0.0 oder 1.0
   * @return " Sehr effektiv!", " Nicht sehr effektiv...", etc.
   */
  private def effMsg(eff: Double): String = eff match {
    case e if e > 1.0  => " Sehr effektiv!"      // 2.0
    case e if e < 1.0 && e > 0 => " Nicht sehr effektiv..."  // 0.5
    case 0.0           => " Hat keine Wirkung!"  // immun
    case _             => ""                     // neutral
  }
}