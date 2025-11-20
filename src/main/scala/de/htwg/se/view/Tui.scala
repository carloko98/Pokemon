package de.htwg.se.view

import de.htwg.se.controller.Controller
import de.htwg.se.util.Observer
import scala.io.StdIn.readLine

// $COVERAGE-OFF$
class Tui(controller: Controller) extends Observer {

  controller.add(this) 

  // Konstanten für die Anzeige
  private val width = 62
  private def pad(s: String): String = s + " " * (width - 4 - s.length).max(0)
  private def line(txt: String): String = s"| ${pad(txt)} |"
  private def hpBar(cur: Int, max: Int): String = {
    val filled = ((cur.toDouble / max) * 13).round.toInt.min(13).max(0)
    "#" * filled + "-" * (13 - filled)
  }
  private val border = "+" + "-" * (width - 2) + "+"
  private val enemySprite = " " * 27 + "HORSEA"
  private val playerSprite = " " * 27 + "PIKACHU"
  private def clearScreen(): Unit = print("\u001b[2J\u001b[H")


  
  override def update(): Unit = {
    render()
  }

  def intro(): Unit = {
    clearScreen()
    println(border)
    println(line(s"Ein wildes ${controller.getEnemy.name} ist erschienen!"))
    println(border)
    Thread.sleep(1800)
  }

  def render(): Unit = {
    val (m1, m2) = controller.getMessage
    val e = controller.getEnemy
    val p = controller.getPlayer

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

    val menuLines = if (!controller.battleOver) {
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

  def inputLoop(): Unit = {
    render() 

    while (!controller.battleOver) {
      val input = readLine().trim.toLowerCase

      input match {
        case "f" =>
          controller.doFlee()

        case s if s.forall(_.isDigit) =>
          val choice = s.toInt
          val attacks = controller.getPlayer.attacks
          if (choice >= 1 && choice <= attacks.length) {
            controller.doPlayerAttack(attacks(choice - 1))
          } else {
            println("Ungültige Eingabe")
          }
        case _ => println("Ungültige Eingabe")
      }
    }
    
    println("\nDruecke Enter, um zu beenden...")
    readLine()
  }
}
// $COVERAGE-ON$