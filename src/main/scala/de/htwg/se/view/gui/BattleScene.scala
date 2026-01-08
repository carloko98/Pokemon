package de.htwg.se.view.gui

import scalafx.scene.layout.{BorderPane, VBox, HBox}
import scalafx.scene.control.{Button, Label, ProgressBar}
import scalafx.geometry.{Pos, Insets}
import de.htwg.se.controller.ControllerInterface

class BattleScene(controller: ControllerInterface) extends BorderPane {

  private val playerPoke = controller.getPlayerPokemon
  private val enemyPoke = controller.getEnemyPokemon

  style = "-fx-background-color: #2b2b2b;"

  // --- GEGNER INFO (oben rechts) ---
  val enemyInfo = new VBox {
    alignment = Pos.CenterRight
    padding = Insets(20)
    spacing = 5
    children = Seq(
      new Label(s"${enemyPoke.name} (Lvl ?)") {
        style = "-fx-text-fill: white; -fx-font-size: 18px;"
      },
      new HBox {
        alignment = Pos.CenterRight
        spacing = 10
        children = Seq(
          new Label("HP:") { style = "-fx-text-fill: white;" },
          new ProgressBar {
            progress = enemyPoke.currentHp.toDouble / enemyPoke.maxHp
            style = "-fx-accent: #ff4444;"
            prefWidth = 200
          }
        )
      },
      new Label(s"${enemyPoke.currentHp} / ${enemyPoke.maxHp}") {
        style = "-fx-text-fill: #aaaaaa;"
      },
      new Label("👾") { style = "-fx-font-size: 80px;" }
    )
  }

  // --- SPIELER INFO (unten links) ---
  val playerInfo = new VBox {
    alignment = Pos.CenterLeft
    padding = Insets(20)
    spacing = 5
    children = Seq(
      new Label("🤠") { style = "-fx-font-size: 80px;" },
      new Label(s"${playerPoke.name} (Lvl 5)") {
        style = "-fx-text-fill: white; -fx-font-size: 18px;"
      },
      new HBox {
        alignment = Pos.CenterLeft
        spacing = 10
        children = Seq(
          new Label("HP:") { style = "-fx-text-fill: white;" },
          new ProgressBar {
            progress = playerPoke.currentHp.toDouble / playerPoke.maxHp
            style = "-fx-accent: #44ff44;"
            prefWidth = 200
          }
        )
      },
      new Label(s"${playerPoke.currentHp} / ${playerPoke.maxHp}") {
        style = "-fx-text-fill: #aaaaaa;"
      }
    )
  }

  // --- UNTERER BEREICH: NACHRICHTEN + AKTIONEN ---
  val actionBox = new VBox {
    padding = Insets(20)
    spacing = 10
    style = "-fx-background-color: #444444; -fx-border-color: #666666; -fx-border-width: 2px 0 0 0;"
    prefHeight = 150

    val (m1, m2) = controller.getMessage
    children += new Label(s"$m1\n$m2") {
      style = "-fx-text-fill: yellow; -fx-font-size: 16px; -fx-font-weight: bold;"
    }

    // Entscheide basierend auf currentPhase, welche Controls angezeigt werden
    val controls = if (controller.currentPhase == "player_attack") {
      createPlayerActions()
    } else {  // enemy_attack oder andere
      createWaitButton()
    }

    children += controls
  }

  top = enemyInfo
  center = playerInfo
  bottom = actionBox

  // --- HILFSMETHODEN ---
  private def createPlayerActions(): HBox = {
    val box = new HBox {
      spacing = 10
      alignment = Pos.Center
    }

    playerPoke.attacks.zipWithIndex.foreach { case (attack, index) =>
      val btn = new Button(s"${attack.name} (${attack.damage} DMG)") {
        style = "-fx-base: #555555; -fx-text-fill: white; -fx-font-size: 14px;"
        minWidth = 150
        onAction = _ => controller.handleInput((index + 1).toString)
      }
      box.children += btn
    }

    val fleeBtn = new Button("Fliehen") {
      style = "-fx-base: #aa3333; -fx-text-fill: white;"
      onAction = _ => controller.handleInput("f")
    }
    box.children += fleeBtn

    box
  }

  private def createWaitButton(): Button = {
    new Button("Weiter (Gegner am Zug)...") {
      style = "-fx-font-size: 16px; -fx-base: #3333aa; -fx-text-fill: white;"
      maxWidth = Double.MaxValue
      onAction = _ => controller.handleInput("")  // Enter drücken → leerer Input
    }
  }
}