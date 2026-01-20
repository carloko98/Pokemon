package de.htwg.se.view.gui

import scalafx.scene.layout.VBox
import scalafx.scene.control.{Button, Label}
import scalafx.geometry.{Pos, Insets}
import de.htwg.se.controller.IController

class SwitchPokemonScene(controller: IController) extends VBox {
  spacing = 15
  alignment = Pos.Center
  style = "-fx-background-color: #222222;"
  padding = Insets(20)

  val title = new Label("Wähle ein Pokemon") {
    style = "-fx-font-size: 24px; -fx-text-fill: white; -fx-font-weight: bold;"
  }

  val msgLabel = new Label(controller.getMessage._2) {
    style = "-fx-text-fill: #ff5555;"
  }

  children += title
  children += msgLabel

  // Liste aller Pokemon im Team holen
  val team = controller.getPlayer.team
  val currentIndex = controller.getPlayer.currentPokemonIndex

  // Für jedes Pokemon einen Button erstellen
  team.zipWithIndex.foreach { case (poke, index) =>
    
    val statusText = if (poke.isFainted) "(K.O.)" else s"${poke.currentHp}/${poke.maxHp} HP"
    val btnText = s"${index + 1}. ${poke.name} - $statusText"
    
    val btn = new Button(btnText) {
      minWidth = 300
      style = if (index == currentIndex) {
        "-fx-base: #5555ff; -fx-text-fill: white; -fx-font-weight: bold;" // Aktives Pokemon blau
      } else if (poke.isFainted) {
        "-fx-base: #555555; -fx-text-fill: #aaaaaa;" // K.O. Pokemon grau
      } else {
        "-fx-base: #44aa44; -fx-text-fill: white;" // Wechselbare Pokemon grün
      }
      
      // Klick sendet einfach die Nummer (1, 2, ...) an den Controller
      onAction = _ => controller.handleInput((index + 1).toString)
    }
    
    children += btn
  }

  val backBtn = new Button("Zurück") {
    style = "-fx-base: #aa3333; -fx-text-fill: white; -fx-font-size: 14px;"
    minWidth = 150
    onAction = _ => controller.handleInput("z")
  }

  children += new Label("") // Leerer Platzhalter
  children += backBtn
}