package de.htwg.controller

import de.htwg.model.{Pokemon, Attack}

// Implementierung der Spiel-Logik
class ControllerImpl(
    var player: Pokemon,  // var → kann im Controller geändert werden
    var enemy: Pokemon
) extends Controller {

  // Zustand
  private var msg1 = ""                  // Nachricht Zeile 1
  private var msg2 = ""                  // Nachricht Zeile 2
  private var battleOver = false         // Kampf vorbei?
  private var observers: List[Observer] = Nil  // Liste der Beobachter (TUI)

  // Spieler greift an
  override def doPlayerAttack(attack: Attack): Unit = {
    if (battleOver) return

    // --- Spieler-Angriff ---
    enemy = enemy.withHp(enemy.currentHp - attack.damage)
    msg1 = s"${player.name} setzte ${attack.name} ein!"
    msg2 = s"${attack.damage} Schaden an ${enemy.name}!"
    notifyObservers()  // UI aktualisieren

    if (enemy.isFainted) { endBattle(won = true); return }

    // --- Gegner-Angriff (immer erster Angriff) ---
    val enemyAtk = enemy.attacks.head
    player = player.withHp(player.currentHp - enemyAtk.damage)
    msg1 = s"${enemy.name} setzte ${enemyAtk.name} ein!"
    msg2 = s"${enemyAtk.damage} Schaden an ${player.name}!"
    notifyObservers()

    if (player.isFainted) endBattle(won = false)
  }

  // Flucht
  override def doFlee(): Unit = {
    msg1 = "Du bist geflohen!"
    msg2 = ""
    battleOver = true
    notifyObservers()
  }

  // Kampf beenden
  private def endBattle(won: Boolean): Unit = {
    battleOver = true
    if (won) {
      msg1 = s"${enemy.name} ist besiegt!"
      msg2 = "Du hast gewonnen!"
    } else {
      msg1 = s"${player.name} ist besiegt!"
      msg2 = "Du hast verloren!"
    }
    notifyObservers()
  }

  // Getter
  override def isBattleOver: Boolean = battleOver
  override def getMessage: (String, String) = (msg1, msg2)

  // Observer-Management
  override def addObserver(o: Observer): Unit = observers = o :: observers
  override def notifyObservers(): Unit = observers.foreach(_.update())
}