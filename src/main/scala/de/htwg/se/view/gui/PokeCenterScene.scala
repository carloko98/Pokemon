package de.htwg.se.view.gui

import scalafx.scene.layout.{VBox, BorderPane}
import scalafx.scene.control.{Button, Label}
import scalafx.geometry.{Pos, Insets}
import de.htwg.se.controller.IController

class PokeCenterScene(controller: IController) extends BorderPane {

  // Typischer PokéCenter Look (Weiß/Rot)
  style = "-fx-background-color: #ffffff;" 

  val header = new VBox {
    alignment = Pos.Center
    padding = Insets(30)
    spacing = 10
    style = "-fx-background-color: #ff4444;" // Roter Balken oben
    children = Seq(
      new Label("PokéCenter") {
        style = "-fx-font-size: 32px; -fx-font-weight: bold; -fx-text-fill: white;"
      },
      new Label("Wir heilen deine Pokémon zur vollen Gesundheit!") {
        style = "-fx-font-size: 14px; -fx-text-fill: white;"
      }
    )
  }
  
  val centerContent = new VBox {
    alignment = Pos.Center
    spacing = 20
    padding = Insets(40)

    // Nachricht vom Controller anzeigen (z.B. "Deine Pokémon sind wieder fit!")
    val msgLabel = new Label(controller.getMessage._1) {
      style = "-fx-font-size: 18px; -fx-text-fill: #333333; -fx-font-weight: bold;"
    }
    
    val subMsgLabel = new Label(controller.getMessage._2) {
      style = "-fx-font-size: 14px; -fx-text-fill: #666666;"
    }

    val btnStyle = "-fx-font-size: 14px; -fx-background-radius: 5; -fx-cursor: hand;"

    children = Seq(
      msgLabel,
      subMsgLabel,
      
      // Button: HEILEN
      new Button("Team Heilen") {
        style = s"$btnStyle -fx-base: #44ff44; -fx-text-fill: black; -fx-font-weight: bold;"
        minWidth = 180
        minHeight = 50
        onAction = _ => controller.handleInput("heilen")
      },

      // Button: ITEMS (Platzhalter)
      new Button("Item Shop") {
        style = s"$btnStyle -fx-base: #4444ff; -fx-text-fill: white;"
        minWidth = 180
        minHeight = 40
        onAction = _ => controller.handleInput("items")
      },

      // Button: ZURÜCK
      new Button("Zurück zum Menü") {
        style = s"$btnStyle -fx-base: #cccccc;"
        minWidth = 180
        minHeight = 40
        onAction = _ => controller.handleInput("back")
      }
    )
  }

  top = header
  center = centerContent
}