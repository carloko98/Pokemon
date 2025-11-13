// src/main/scala/de/htwg/view/Tui.scala
package de.htwg.view

import de.htwg.controller.{Controller, Observer}
import de.htwg.model.{Pokemon, Attack}
import scala.io.StdIn.readLine

/**
 * Pokémon-Style TUI mit Rahmen, HP-Balken, Sprites und dynamischem Menü
 */
class Tui(controller: Controller) extends Observer {

  controller.addObserver(this) // TUI beobachtet Controller

  // UI-Konstanten
  private val width = 62
  private def pad(s: String): String = s + " " * (width - 4 - s.length).max(0)
  private def line(txt: String): String = s"| ${pad(txt)} |"
  private def hpBar(cur: Int, max: Int): String = {
    val filled = ((cur.toDouble / max) * 13).round.toInt.min(13).max(0)
    "#" * filled + "-" * (13 - filled)
  }
  private val border = "+" + "-" * (width - 2) + "+"

  // Sprite-Platzhalter (kann später durch ASCII-Art ersetzt werden)
  private val enemySprite = " " * 27 + "HORSEA"
  private val playerSprite = " " * 27 + "PIKACHU"

  // Intro-Bildschirm
  def intro(): Unit = {
    clearScreen()
    println(border)
    println(line(s"Ein wildes ${controller.enemy.name} ist erschienen!"))
    println(border)
    Thread.sleep(1800)
  }

  // Bildschirm rendern
  def render(): Unit = {
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

  // Eingabe-Schleife
  def inputLoop(): Unit = {
    while (!controller.isBattleOver) {
      val input = readLine().trim.toLowerCase

      input match {
        case "f" =>
          controller.doFlee()

        case s if s.forall(_.isDigit) =>
          val choice = s.toInt
          val attacks = controller.player.attacks
          if (choice >= 1 && choice <= attacks.length) {
            controller.doPlayerAttack(attacks(choice - 1))
          } else {
            showInvalidInput()
          }

        case _ =>
          showInvalidInput()
      }

      if (!controller.isBattleOver) {
        Thread.sleep(1000) // Dramatik!
      }
    }

    // Nach Kampfende: Letzte Ansicht + Enter zum Beenden
    println("\nDruecke Enter, um zu beenden...")
    readLine()
  }

  private def showInvalidInput(): Unit = {
    println(s"Ungueltige Eingabe! Waehle 1–${controller.player.attacks.length} oder 'f'.")
    Thread.sleep(800)
  }

  // Wird bei Änderungen im Controller aufgerufen
  override def update(): Unit = render()

  // Hilfsmethode: Bildschirm löschen (ANSI)
  private def clearScreen(): Unit = print("\u001b[2J\u001b[H")
}