// src/test/scala/BattleUISpec.scala

package de.htwg.battle

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers


import de.htwg.renderBattleScreen

class BattleUISpec extends AnyWordSpec with Matchers {


  val enemyName       = "HORSEA"
  val enemyLevel      = 16
  val enemyHpBar      = "#######-------"
  val enemyPokemon     = "EnemiePicture"
  
  val playerName      = "SHELLY"
  val playerLevel     = 12
  val playerHpBar     = "##########-----"
  val playerHpCurr    = 28
  val playerHpMax     = 34
  val playerPokemon    = "PlayerPicture"
  
  val messageLine1    = "Enemy HORSEA used BUBBLE!"
  val messageLine2    = ""


  val battleScreen = renderBattleScreen(
    enemyName, enemyLevel, enemyHpBar, enemyPokemon,
    playerName, playerLevel, playerHpBar, playerHpCurr, playerHpMax, playerPokemon,
    messageLine1, messageLine2
  )

  "The renderBattleScreen function" should {
    
    "correctly display the enemy name and level" in {
      battleScreen should include ("HORSEA")
      battleScreen should include ("L16")
    }

    "correctly display the player's current and max HP" in {
      battleScreen should include ("28/34")
      battleScreen should include (s"HP: [$playerHpBar]")
    }

    "correctly display the battle message in the message box" in {
      battleScreen should include ("Enemy HORSEA used BUBBLE!")
    }

    "be framed by borders" in {
      battleScreen.startsWith("+") should be (true)
    }
  }
}
