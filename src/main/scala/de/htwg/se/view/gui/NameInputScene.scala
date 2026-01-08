package de.htwg.se.view.gui

import scalafx.scene.layout.VBox
import scalafx.scene.control.{Button, Label, TextField}
import scalafx.geometry.Pos
import de.htwg.se.controller.Controller

class NameInputScene(controller: Controller) extends VBox {
  spacing = 20
  alignment = Pos.Center
  style = "-fx-background-color: #222222;"

  val instructionLabel = new Label("Wie heißt du, Trainer?") {
    style = "-fx-font-size: 24px; -fx-text-fill: white;"
  }

  val nameField = new TextField {
    maxWidth = 300
    promptText = "Gib deinen Namen ein..."
    style = "-fx-font-size: 16px;"
  }

  val errorLabel = new Label(controller.getMessage._2) {
    style = "-fx-text-fill: #ff5555;" 
  }

  val startBtn = new Button("Abenteuer starten") {
    style = "-fx-font-size: 18px; -fx-base: #44aa44; -fx-text-fill: white;"
    defaultButton = true 
    onAction = _ => controller.handleInput(nameField.text.value)
  }

  children = Seq(instructionLabel, nameField, errorLabel, startBtn)
}