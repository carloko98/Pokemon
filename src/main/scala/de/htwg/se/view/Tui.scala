package de.htwg.se.view // (Oder dein Paketpfad, z.B. de.htwg.se.view)

import de.htwg.se.controller.Controller // (Pfad zum immutable Controller)
import de.htwg.se.model.{Pokemon, Attack}
import scala.io.StdIn.readLine

/**
 * Pokemon-Style TUI (Immutable-Version)
 *
 * Dies ist die "Imperative Shell":
 * 1. Sie hält den *aktuellen* Zustand in 'var controller'.
 * 2. Sie ist KEIN Observer.
 * 3. Sie ruft 'render()' manuell auf, nachdem sie den Zustand aktualisiert hat.
 */
class Tui(var controller: Controller) { // <-- Das 'var' ist entscheidend

  // ==================== UI-Konstanten & Helfer ====================
  private val width = 62
  private def pad(s: String): String = s + " " * (width - 4 - s.length).max(0)
  private def line(txt: String): String = s"| ${pad(txt)} |"
  private def hpBar(cur: Int, max: Int): String = {
    val filled = ((cur.toDouble / max) * 13).round.toInt.min(13).max(0)
    "#" * filled + "-" * (13 - filled)
  }
  private val border = "+" + "-" * (width - 2) + "+"

  // Sprite-Platzhalter
  private val enemySprite = " " * 27 + "HORSEA"
  private val playerSprite = " " * 27 + "PIKACHU"

  // ==================== Öffentliche Methoden (für main) ====================

  /**
   * Zeigt den Intro-Text an.
   * (Ist 'public', damit 'main' sie aufrufen kann)
   */
  def intro(): Unit = {
    clearScreen()
    println(border)
    println(line(s"Ein wildes ${controller.enemy.name} ist erschienen!"))
    println(border)
    Thread.sleep(1800) // (1.8 Sekunden)
  }

  /**
   * Die Haupt-Spielschleife.
   * Übernimmt die Kontrolle, bis das Spiel vorbei ist.
   */
  def inputLoop(): Unit = {
    // 1. Initiales Rendern, um den Start-Zustand zu zeigen
    render()

    // 2. Schleife läuft, solange der *aktuelle* Controller-Zustand 'battleOver' false ist
    while (!controller.isBattleOver) {
      val input = readLine().trim.toLowerCase

      input match {
        case "f" =>
          // 3. Zustand aktualisieren: 'controller' wird mit dem *neuen* Zustand überschrieben
          controller = controller.doFlee()

        case s if s.forall(_.isDigit) =>
          val choice = s.toInt
          val attacks = controller.player.attacks
          if (choice >= 1 && choice <= attacks.length) {
            // 3. Zustand aktualisieren: 'controller' wird mit dem *neuen* Zustand überschrieben
            controller = controller.doPlayerAttack(attacks(choice - 1))
          } else {
            showInvalidInput()
          }

        case _ =>
          showInvalidInput()
      }

      // 4. Manuell neu rendern: Zeigt den *neuen* Zustand an,
      //    egal ob Angriff, Flucht oder Fehleingabe.
      render()
      
      // (Dieser Block stellt sicher, dass der End-Bildschirm
      //  gerendert wird, *bevor* die Schleife beim nächsten
      //  Durchlauf 'controller.isBattleOver' prüft.)

      if (!controller.isBattleOver) {
        Thread.sleep(1000) // (Kleine Pause vor der nächsten Eingabe)
      }
    }

    // 5. Nach der Schleife (Kampf ist vorbei)
    println("\nDruecke Enter, um zu beenden...")
    readLine()
  }

  // ==================== Private Helfer (Nur für TUI) ====================

  /**
   * Zeichnet den *aktuellen* Zustand des Controllers auf die Konsole.
   */
  private def render(): Unit = {
    val (m1, m2) = controller.getMessage
    val e = controller.enemy
    val p = controller.player

    val enemyLines = Seq(
      line(s"  ${e.name}"),
      line(s"  HP: [${hpBar(e.currentHp, e.maxHp)}] ${e.currentHp}/${e.maxHp}"),
      line(""),
      line(enemySprite)
    )

    val playerLines = Seq(
      line(playerSprite),
      line(""),
      line(s"  ${p.name}"),
      line(s"  HP: [${hpBar(p.currentHp, p.maxHp)}] ${p.currentHp}/${p.maxHp}")
    )

    val msgLines = if (m1.nonEmpty || m2.nonEmpty) {
      Seq(line(""), line(m1), line(m2))
    } else Seq(line(""))

    val menuLines = if (!controller.isBattleOver) {
      val attacks = p.attacks.zipWithIndex.map { case (atk, i) =>
        s"  ${i + 1}. ${atk.name} (${atk.damage} DMG, ${atk.attackType})"
      }
      val flee = "  f. Fliehen"
      Seq(line("Waehle eine Aktion:"), line(attacks.mkString("  |  ")), line(flee))
    } else Seq.empty

    clearScreen()
    val allLines = Seq(border) ++ enemyLines ++ playerLines ++ msgLines ++ menuLines ++ Seq(border)
    println(allLines.mkString("\n"))
  }

  /**
   * Zeigt eine Fehlermeldung (und rendert *nicht* neu, das macht die inputLoop).
   */
  private def showInvalidInput(): Unit = {
    println(s"Ungueltige Eingabe! Waehle 1–${controller.player.attacks.length} oder 'f'.")
    Thread.sleep(800)
  }

  /**
   * Löscht die Konsole.
   */
  private def clearScreen(): Unit = print("\u001b[2J\u001b[H")
}