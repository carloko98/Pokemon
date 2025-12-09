package de.htwg.se.view.gui

import scalafx.scene.layout.VBox
import scalafx.scene.control.{Button, Label, ScrollPane}
import scalafx.geometry.{Pos, Insets}
import de.htwg.se.controller.Controller

class SelectProfileScene(controller: Controller) extends VBox {
  spacing = 20
  alignment = Pos.Center
  style = "-fx-background-color: #222222;"
  padding = Insets(30)

  val titleLabel = new Label("Spielstand laden") {
    style = "-fx-font-size: 30px; -fx-text-fill: white;"
  }

  val errorLabel = new Label(controller.getMessage._2) {
    style = "-fx-text-fill: #ff5555;"
  }

  // Container für die Liste der Profile
  val listContainer = new VBox {
    spacing = 10
    alignment = Pos.Center
    style = "-fx-background-color: transparent;"
  }

  // Liste vom Controller holen
  val saves = controller.getAvailableSaves

  if (saves.isEmpty) {
    listContainer.children += new Label("Keine Spielstände gefunden.") {
       style = "-fx-text-fill: #888888; -fx-font-style: italic;"
    }
  } else {
    // Für jedes Savegame einen Button erstellen
    saves.foreach { saveName =>
      val btn = new Button(saveName) {
        prefWidth = 300
        style = "-fx-font-size: 16px; -fx-base: #444444; -fx-text-fill: white;"
        onAction = _ => controller.handleInput(saveName)
      }
      listContainer.children += btn
    }
  }

  // ScrollPane, falls es viele Saves gibt
  val scrollPane = new ScrollPane {
    content = listContainer
    fitToWidth = true
    maxWidth = 350
    maxHeight = 300
    style = "-fx-background: #222222; -fx-background-color: #222222;"
  }

  val backBtn = new Button("Zurück") {
    onAction = _ => controller.handleInput("b")
    style = "-fx-font-size: 14px;"
  }

  children = Seq(titleLabel, errorLabel, scrollPane, backBtn)
}