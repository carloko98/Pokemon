package de.htwg.se.view.gui

import scalafx.scene.layout.{BorderPane, VBox, HBox, Priority}
import scalafx.scene.control.{Button, Label, ProgressBar}
import scalafx.geometry.{Pos, Insets}
import de.htwg.se.controller.Controller
import de.htwg.se.controller.state.{PlayerAttackState, EnemyAttackState}

class BattleScene(controller: Controller) extends BorderPane {

  val playerPoke = controller.getPlayerPokemon
  val enemyPoke = controller.getEnemyPokemon
  
  style = "-fx-background-color: #2b2b2b;" 

  val enemyInfo = new VBox {
    alignment = Pos.CenterRight
    padding = Insets(20)
    spacing = 5
    children = Seq(
      new Label(s"${enemyPoke.name} (Lvl ?)") { style = "-fx-text-fill: white; -fx-font-size: 18px;" },
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
      new Label(s"${enemyPoke.currentHp} / ${enemyPoke.maxHp}") { style = "-fx-text-fill: #aaaaaa;" },
      // Platzhalter für Gegner-Sprite 
      new Label("👾") { style = "-fx-font-size: 80px;" } 
    )
  }

  // --- MITTLERER BEREICH: SPIELER ---
  val playerInfo = new VBox {
    alignment = Pos.CenterLeft
    padding = Insets(20)
    spacing = 5
    children = Seq(
      // Platzhalter für Spieler-Sprite
      new Label("🤠") { style = "-fx-font-size: 80px;" }, 
      new Label(s"${playerPoke.name} (Lvl 5)") { style = "-fx-text-fill: white; -fx-font-size: 18px;" },
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
      new Label(s"${playerPoke.currentHp} / ${playerPoke.maxHp}") { style = "-fx-text-fill: #aaaaaa;" }
    )
  }

  // --- UNTERER BEREICH ---
  val actionBox = new VBox {
    padding = Insets(20)
    spacing = 10
    style = "-fx-background-color: #444444; -fx-border-color: #666666; -fx-border-width: 2px 0 0 0;"
    prefHeight = 150

    val (m1, m2) = controller.getMessage
    children += new Label(s"$m1\n$m2") {
      style = "-fx-text-fill: yellow; -fx-font-size: 16px; -fx-font-weight: bold;"
    }

    val controls = controller.state match {
      case _: PlayerAttackState => createPlayerActions()
      case _: EnemyAttackState => createWaitButton()
      case _ => new Label("")
    }
    children += controls
  }

  top = enemyInfo
  center = playerInfo
  bottom = actionBox


  // --- HILFSMETHODEN FÜR BUTTONS ---

  def createPlayerActions(): HBox = {
    val box = new HBox { spacing = 10; alignment = Pos.Center }
    
    // Attacken Buttons erstellen
    playerPoke.attacks.zipWithIndex.foreach { case (attack, index) =>
      val btn = new Button(s"${attack.name} (${attack.damage})") {
        style = "-fx-base: #555555; -fx-text-fill: white; -fx-font-size: 14px;"
        minWidth = 120
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

  def createWaitButton(): Button = {
    new Button("Weiter (Gegnerzug)...") {
      style = "-fx-font-size: 16px; -fx-base: #3333aa; -fx-text-fill: white;"
      maxWidth = Double.MaxValue
      onAction = _ => controller.handleInput("")  
    }
  }
}