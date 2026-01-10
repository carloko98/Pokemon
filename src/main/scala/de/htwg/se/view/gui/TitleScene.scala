package de.htwg.se.view.gui

import scalafx.scene.layout.VBox
import scalafx.scene.control.{Button, Label}
import scalafx.geometry.Pos
import scalafx.scene.image.{Image, ImageView}
import de.htwg.se.controller.IController


class TitleScene(controller: IController) extends VBox {


  spacing = 20
  alignment = Pos.Center
  style = "-fx-background-color: #222222;" 

  val titleLabel = new Label("POKEMON SCALA") {
    style = "-fx-font-size: 40px; -fx-text-fill: white; -fx-font-weight: bold;"
  }
  

  val newGameBtn = new Button("Neues Spiel") {
    style = "-fx-font-size: 18px; -fx-min-width: 200px;"
    onAction = _ => controller.handleInput("n") 
  }

  val loadGameBtn = new Button("Spiel laden") {
    style = "-fx-font-size: 18px; -fx-min-width: 200px;"
    onAction = _ => controller.handleInput("l")
  }

  val quitBtn = new Button("Beenden") {
    style = "-fx-font-size: 18px; -fx-min-width: 200px; -fx-background-color: #aa3333; -fx-text-fill: white;"
    onAction = _ => controller.handleInput("q")
  }


  children = Seq(
    titleLabel,
    newGameBtn,
    loadGameBtn,
    quitBtn
  )
}