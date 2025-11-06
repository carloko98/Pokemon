package de.htwg


@main def runBattleUI(): Unit =
  
  val enemyName       = "HORSEA"
  val enemyLevel      = 16
  val enemyHpBar      = "#######-------"
  val enemyPokemon     = "EnemyPicture"
  
  val playerName      = "SHELLY"
  val playerLevel     = 12
  val playerHpBar     = "##########-----"
  val playerHpCurr    = 28
  val playerHpMax     = 34
  val playerPokemon    = "PlayerPicture"
  
  val messageLine1    = "Enemy HORSEA used BUBBLE!"
  val messageLine2    = ""

 
  println(renderBattleScreen(
    enemyName, enemyLevel, enemyHpBar, enemyPokemon
,
    playerName, playerLevel, playerHpBar, playerHpCurr, playerHpMax, playerPokemon
,
    messageLine1, messageLine2
  ))


def renderBattleScreen(
  enemyName: String,
  enemyLevel: Int,
  enemyHpBar: String,
  enemyPokemon: String,
  playerName: String,
  playerLevel: Int,
  playerHpBar: String,
  playerHpCurr: Int,
  playerHpMax: Int,
  playerPokemon: String,
  messageLine1: String,
  messageLine2: String
): String =

  val width = 62 

  def padRight(text: String, total: Int): String =
    text + " " * (total - text.length).max(0)

  def line(content: String): String =
    "| " + padRight(content, width - 4) + " |"

  val border = "+" + "-" * (width - 2) + "+"

  val enemyStatus =
    Seq(
      line(s"$enemyName${" " * (width - enemyName.length - 10)}L$enemyLevel"),
      line(s"HP: [$enemyHpBar]"),
      line(""),
      line(padRight(enemyPokemon, width - 4))
    )

  val playerStatus =
    Seq(
      line(""),
      line(padRight(playerPokemon, width - 4)),
      line(s"$playerName${" " * (width - playerName.length - 10)}L$playerLevel"),
      line(s"HP: [$playerHpBar]     $playerHpCurr/$playerHpMax")
    )

  val messageBox =
    Seq(
      border,
      line(padRight(messageLine1, width - 4)),
      line(padRight(messageLine2, width - 4)),
      border
    )

  (Seq(border) ++ enemyStatus ++ playerStatus ++ messageBox).mkString("\n")


enum PokemonType{
    case Water, Fire, Plant, Electric, Normal, Flying
}
