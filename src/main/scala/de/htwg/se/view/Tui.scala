package de.htwg.se.view

import de.htwg.se.controller.Controller
import de.htwg.se.controller.state.{MenuState, PlayerAttackState, EnemyAttackState}
import de.htwg.se.util.Observer
import scala.io.StdIn.readLine

class Tui(val controller: Controller) extends Observer {

  controller.add(this)

  override def update(): Unit = {
    render()
  }

  def intro(): Unit = {
    clearScreen()
    println("POKEMON SCALA EDITION")
    println("... lade Texturen ...")
    Thread.sleep(1000)
  }

  def render(): Unit = {
    clearScreen()
    
    controller.state match {
      
      case MenuState(_) =>
        println(border)
        println(line("HAUPTMENUE"))
        println(line(""))
        println(line("s. Wilden Kampf starten"))   
        println(line("t. Trainer Kampf starten"))  
        println(line("q. Beenden"))
        println(border)
        val (m1, m2) = controller.getMessage
        if (m1.nonEmpty) println(s"\n$m1\n$m2")

      case PlayerAttackState(_, _) => 
        renderBattle(showActions = true)

      case EnemyAttackState(_, _) =>
        renderBattle(showActions = false)
    }
  }
  
  private def renderBattle(showActions: Boolean): Unit = {
    val (m1, m2) = controller.getMessage
    val e = controller.getEnemyPokemon
    val p = controller.getPlayerPokemon

    val enemyLines = Seq(
      line(s"  ${e.name} (${e.pType})"),
      line(s"  HP: [${hpBar(e.currentHp, e.maxHp)}] ${e.currentHp}/${e.maxHp}"),
      line(""),
      line(enemySprite)
    )

    val playerLines = Seq(
      line(playerSprite),
      line(""),
      line(s"  ${p.name} (${p.pType})"),
      line(s"  HP: [${hpBar(p.currentHp, p.maxHp)}] ${p.currentHp}/${p.maxHp}")
    )

    val msgLines = if (m1.nonEmpty || m2.nonEmpty) {
      Seq(line(""), line(m1), line(m2))
    } else Seq(line(""))

    val menuLines = if (showActions) {
        // Kampfmenü anzeigen
        val attacks = p.attacks.zipWithIndex.map { case (atk, i) =>
          s"  ${i + 1}. ${atk.name} (${atk.damage} DMG, ${atk.attackType})"
        }
        val flee = "  f. Fliehen"
        Seq(line("Kampf-Aktion waehlen:"), line(attacks.mkString(" | ")), line(flee))
    } else {
        // Warte-Bildschirm anzeigen
        Seq(line(""), line(">> Druecke Enter fuer Gegnerzug... <<"))
    }

    val allLines = Seq(border) ++ enemyLines ++ playerLines ++ msgLines ++ menuLines ++ Seq(border)
    println(allLines.mkString("\n"))
  }

  def inputLoop(): Unit = {
    render() 
    while (true) {
      val input = readLine().trim.toLowerCase
      controller.handleInput(input)
    }
  }

  // --- Hilfsmethoden ---
  private val width = 80 
  private def pad(s: String): String = s + " " * (width - 4 - s.length).max(0)
  private def line(txt: String): String = s"| ${pad(txt)} |"
  private def hpBar(cur: Int, max: Int): String = {
    val filled = ((cur.toDouble / max) * 20).round.toInt.min(20).max(0)
    "#" * filled + "-" * (20 - filled)
  }
  private val border = "+" + "-" * (width - 2) + "+"
  private val enemySprite = " " * 40 + "/^\\" 
  private val playerSprite = " " * 10 + "(o.o)" 
  private def clearScreen(): Unit = print("\u001b[2J\u001b[H")
}