package de.htwg.se.view.gui

import scalafx.application.JFXApp3
import scalafx.application.Platform
import scalafx.scene.Scene
import scalafx.scene.Parent
import scalafx.Includes._
import de.htwg.se.util.Observer
import de.htwg.se.controller.ControllerInterface


class Gui(val controller: ControllerInterface) extends JFXApp3 with Observer {

  controller.add(this)

  override def start(): Unit = {
    stage = new JFXApp3.PrimaryStage {
      title = "Pokemon Scala Edition"
      width = 900   // etwas breiter für bessere Darstellung
      height = 700
      scene = new Scene {
        root = createSceneForPhase(controller.currentPhase)
      }
    }
    stage.show()
  }

  override def update(): Unit = {
    Platform.runLater {
      val newRoot: Parent = createSceneForPhase(controller.currentPhase)

      if (stage != null && stage.scene() != null) {
        stage.scene().root = newRoot
      }
    }
  }

  /**
   * Zentrale Methode: Wählt die richtige Scene basierend auf dem aktuellen Phase-String
   * Kein Zugriff auf konkrete States mehr – perfekte Kapselung!
   */
  private def createSceneForPhase(phase: String): Parent = phase match {
    case "title"           => new TitleScene(controller)
    case "name_input"      => new NameInputScene(controller)
    case "menu"            => new MenuScene(controller)
    case "select_profile"  => new SelectProfileScene(controller)
    case "player_attack"   => new BattleScene(controller)
    case "enemy_attack"    => new BattleScene(controller)
    case _                 => new TitleScene(controller)  // Sicherer Fallback
  }
}