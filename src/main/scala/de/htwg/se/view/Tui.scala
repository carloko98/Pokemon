package de.htwg.se.view

import de.htwg.se.controller.Controller
import de.htwg.se.controller.state.{MenuState, PlayerAttackState, EnemyTurnState}
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
    // Das erste Render wird durch den Observer-Update oder inputLoop getriggert
  }

  def render(): Unit = {
    clearScreen()
    
    // Hier hat der Case gefehlt:
    controller.state match {
      case MenuState(_) =>
        println(border)
        println(line("HAUPTMENUE"))
        println(line(""))
        println(line("s. Neues Spiel starten"))
        println(line("q. Beenden"))
        println(border)
        val (m1, m2) = controller.getMessage
        if (m1.nonEmpty) println(s"\n$m1\n$m2")

      // Wenn der Spieler dran ist: Zeige Attacken (true)
      case PlayerAttackState(_) =>
        renderBattle(showActions = true)

      // NEU: Wenn der Gegner dran ist: Zeige nur "Weiter" (false)
      case EnemyTurnState(_) =>
        renderBattle(showActions = false)
    }
  }
  
  // Hilfsmethode (falls du sie noch nicht angepasst hattest):
  private def renderBattle(showActions: Boolean): Unit = {
    val (m1, m2) = controller.getMessage
    // Achtung: Nutze die neuen Getter vom Controller!
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
      // PlayerAttackState: Zeige Menü
      val attacks = p.attacks.zipWithIndex.map { case (atk, i) =>
        s"  ${i + 1}. ${atk.name} (${atk.damage} DMG, ${atk.attackType})"
      }
      val flee = "  f. Fliehen"
      Seq(line("Kampf-Aktion waehlen:"), line(attacks.mkString(" | ")), line(flee))
    } else {
      // EnemyTurnState: Zeige nur Info
      Seq(line(""), line(">> Druecke Enter fuer Gegnerzug... <<"))
    }

    val allLines = Seq(border) ++ enemyLines ++ playerLines ++ msgLines ++ menuLines ++ Seq(border)
    println(allLines.mkString("\n"))
  }

  def inputLoop(): Unit = {
    render() 
    // Endlosschleife: Wir lesen Input und geben ihn einfach an den Controller weiter.
    // Der Controller (bzw. der aktuelle State) entscheidet, was passiert.
    while (true) {
      val input = readLine().trim.toLowerCase
      controller.handleInput(input)
    }
  }

  // --- Hilfsmethoden für Layout ---
  private val width = 80 
  private def pad(s: String): String = s + " " * (width - 4 - s.length).max(0)
  private def line(txt: String): String = s"| ${pad(txt)} |"
  private def hpBar(cur: Int, max: Int): String = {
    val filled = ((cur.toDouble / max) * 20).round.toInt.min(20).max(0)
    "#" * filled + "-" * (20 - filled)
  }
  private val border = "+" + "-" * (width - 2) + "+"
  private val enemySprite = " " * 40 
  private val playerSprite = " " * 10 
  private def clearScreen(): Unit = print("\u001b[2J\u001b[H")
}