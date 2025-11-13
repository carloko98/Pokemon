package de.htwg.view

import de.htwg.controller.{Controller, Observer}
import scala.io.StdIn.readLine

// Text-UI: Zeigt Zustand an, nimmt Eingaben entgegen
class Tui(controller: Controller) extends Observer {
  controller.addObserver(this)  // TUI beobachtet Controller

  // UI-Konstanten
  private val width = 62
  private def pad(s: String) = s + " " * (width - 4 - s.length).max(0)
  private def line(txt: String) = s"| ${pad(txt)} |"
  private def hpBar(cur: Int, max: Int) = {
    val filled = ((cur.toDouble / max) * 13).round.toInt.min(13).max(0)
    "#" * filled + "-" * (13 - filled)
  }
  private val border = "+" + "-" * (width - 2) + "+"
  private val sprite = " " * 27

  // Intro-Bildschirm
  def intro(): Unit = {
    println(border)
    println(line(s"Ein wildes ${controller.enemy.name} ist erschienen!"))
    println(border)
    Thread.sleep(1500)
  }

  // Bildschirm rendern
  def render(): Unit = {
    val (m1, m2) = controller.getMessage
    val e = controller.enemy
    val p = controller.player

    val enemyLines = Seq(
      line(s"L${e.level} ${e.name}"),
      line(s"HP: [${hpBar(e.currentHp, e.maxHp)}]"),
      line(""),
      line(sprite + "<Enemy Sprite>")
    )
    val playerLines = Seq(
      line(sprite + "<Player Sprite>"),
      line(""),
      line(s"L${p.level} ${p.name}"),
      line(s"HP: [${hpBar(p.currentHp, p.maxHp)}]     ${p.currentHp}/${p.maxHp}")
    )
    val msgLines = Seq(line(""), line(m1), line(m2))

    print("\u001b[2J\u001b[H")  // Bildschirm löschen
    println((Seq(border) ++ enemyLines ++ playerLines ++ msgLines ++ Seq(border)).mkString("\n"))
  }

  // Eingabe-Schleife
  def inputLoop(): Unit = while (!controller.isBattleOver) {
    println("Waehle: 1: Tackle  2: Fliehen")
    readLine().trim match {
      case "1" => controller.doPlayerAttack(controller.player.attacks.head)
      case "2" => controller.doFlee()
      case _   => // Ungültige Eingabe → ignorieren
    }
    Thread.sleep(1200)  // Dramatische Pause
  }

  // Wird bei Änderungen im Controller aufgerufen
  override def update(): Unit = render()
}