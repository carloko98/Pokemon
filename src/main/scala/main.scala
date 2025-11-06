package de.htwg

import scala.io.StdIn.readLine

@main def runBattleUI(): Unit =

// -----------------------------
// Beispiel-Attacken erstellen
// -----------------------------
 val tackle = Attack("Tackle", 6, PokemonType.Normal)
 val bubble = Attack("Bubble", 8, PokemonType.Water)

// -----------------------------
// Spieler- und Gegner-Pokémon
// -----------------------------
  val enemy = Pokemon(
    name = "HORSEA",
    level = 16,
    pType = PokemonType.Water,
    maxHp = 40,
    currentHp = 40,
    attacks = List(bubble, tackle)
  )

  val player = Pokemon(
    name = "PIKACHU",
    level = 12,
    pType = PokemonType.Electric,
    maxHp = 34,
    currentHp = 34,
    attacks = List(tackle)
  )

// -----------------------------
// Hilfsfunktionen für Text-UI
// -----------------------------
  val width = 62

  def padRight(text: String, total: Int): String =
    text + " " * (total - text.length).max(0)

  def line(content: String): String =
    "| " + padRight(content, width - 4) + " |"

  def hpBar(current: Int, max: Int, barWidth: Int = 13): String = {
    val filled = ((current.toDouble / max) * barWidth).round.toInt
    "#" * filled + "-" * (barWidth - filled)
  }

// -----------------------------
// Funktion zum Anzeigen einer Nachricht als Box
// -----------------------------
  def messageBox(msg1: String, msg2: String = ""): Unit = {
    val border = "+" + "-" * (width - 2) + "+"
    println(border)
    println(line(msg1))
    println(line(msg2))
    println(border)
  }

// -----------------------------
// renderBattleScreen: Zeigt Pokémon und Nachrichten
// -----------------------------

  def renderBattleScreen(
    enemy: Pokemon, 
    player: Pokemon,
    message1: String, 
    message2: String
): String = {

  val border = "+" + "-" * (width - 2) + "+"
  val spriteCenter = " " * 27

  val enemyStatus = Seq(
    line(s"L${enemy.level} ${enemy.name}"),
    line(s"HP: [${hpBar(enemy.currentHp, enemy.maxHp)}]"),
    line(""),
    line(spriteCenter + "<Enemy Sprite>")
  )

  val playerStatus = Seq(
    line(spriteCenter + "<Player Sprite>"),
    line(""),
    line(s"L${player.level} ${player.name}"),
    line(s"HP: [${hpBar(player.currentHp, player.maxHp)}]     ${player.currentHp}/${player.maxHp}")
  )

  // MESSAGE-BOX JETZT OHNE DIE UNTERE BORDER-ZEILE
  val messageBoxSeq = Seq(
    line(""),
    line(message1),
    line(message2)
  )

  (Seq(border) ++ enemyStatus ++ playerStatus ++ messageBoxSeq ++ Seq(border)).mkString("\n")
  
}

// -----------------------------
// Einfacher Game Loop
// -----------------------------
  var currentPlayer = player
  var currentEnemy = enemy
  var continueBattle = true

// >>> Zuerst die Intro-Nachricht anzeigen
  messageBox(s"Ein wildes ${enemy.name} ist erschienen!")

// Warte kurz, optional
  Thread.sleep(1000)

// Startnachrichten für Battle-Fenster
  var message1 = ""
  var message2 = ""

  while (continueBattle) {

   // 1. Leere Box (kein Text drin)
    println(renderBattleScreen(currentEnemy, currentPlayer, "", ""))
    println("Waehle eine Aktion: 1: Tackle  2: Fliehen ")
    val choice = readLine().trim
   

    choice match {
      case "1" =>
        val attack = currentPlayer.attacks.head
        val newEnemyHp = currentEnemy.currentHp - attack.damage
        currentEnemy = currentEnemy.withHp(newEnemyHp)
        message1 = s"${currentPlayer.name} setzte ${attack.name} ein!"
        message2 = s"${attack.damage} Schaden an ${currentEnemy.name}!"

      case "2" =>
        message1 = "Du bist geflohen!"
        message2 = ""
        println(renderBattleScreen(currentEnemy, currentPlayer, message1, message2))
        continueBattle = false
        return

      case _ =>
        message1 = "Ungültige Eingabe!"
        message2 = "1 oder 2 eingeben"
    }

    if (currentEnemy.isFainted) {
      message1 = s"${currentEnemy.name} ist besiegt!"
      message2 = "Du hast gewonnen!"
      println(renderBattleScreen(currentEnemy, currentPlayer, message1, message2))
      continueBattle = false
      return
    }

  
  }
