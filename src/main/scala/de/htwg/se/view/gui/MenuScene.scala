package de.htwg.se.view.gui

import scalafx.scene.layout.VBox
import scalafx.scene.control.{Button, Label}
import scalafx.geometry.Pos
import de.htwg.se.controller.IController

class MenuScene(controller: IController) extends VBox {
  spacing = 20
  alignment = Pos.Center
  style = "-fx-background-color: #336699;" 

  val titleLabel = new Label("HAUPTMENÜ") {
    style = "-fx-font-size: 30px; -fx-text-fill: white;"
  }
  
  val msgLabel = new Label(controller.getMessage._1) {
     style = "-fx-text-fill: yellow;"
  }

  val wildBtn = new Button("Wilder Kampf") {
    onAction = _ => controller.handleInput("s")
    style = "-fx-font-size: 16px; -fx-min-width: 150px;"
  }

  val trainerBtn = new Button("Trainer Kampf") {
    onAction = _ => controller.handleInput("t")
    style = "-fx-font-size: 16px; -fx-min-width: 150px;"
  }
  
  val saveBtn = new Button("Speichern") {
    onAction = _ => controller.handleInput("save")
  }
  val exitBtn = new Button("Beenden") {
    onAction = _ => System.exit(0)
    style = "-fx-font-size: 16px; -fx-min-width: 200px; -fx-background-color: #aa3333; -fx-text-fill: white;"
  }

  children = Seq(titleLabel, msgLabel, wildBtn, trainerBtn, saveBtn, exitBtn)
}