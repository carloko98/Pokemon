package de.htwg.se.view.gui

import scalafx.application.JFXApp3
import scalafx.application.Platform
import scalafx.scene.Scene
import scalafx.scene.Parent
import scalafx.Includes._  // <--- DAS IST DER FIX! (Die "Magie")
import de.htwg.se.controller.Controller
import de.htwg.se.util.Observer
import de.htwg.se.controller.state._

class Gui(val controller: Controller) extends JFXApp3 with Observer {

  controller.add(this)

  override def start(): Unit = {
    stage = new JFXApp3.PrimaryStage {
      title = "Pokemon Scala Edition"
      width = 800
      height = 600
      scene = new Scene {
        root = new TitleScene(controller)
      }
    }
  }

  override def update(): Unit = {
    Platform.runLater {
      val newRoot: Parent = controller.state match {
        case _: TitleState => new TitleScene(controller)
        case _: MenuState => new MenuScene(controller)
        case _: NameInputState => new NameInputScene(controller)
        case _: SelectProfileState => new SelectProfileScene(controller)
        case _: PlayerAttackState | _: EnemyAttackState => new BattleScene(controller)
        case _ => new TitleScene(controller)
      }
      
      if (stage != null && stage.scene() != null) {
         // Dank dem Import 'Includes._' funktioniert das jetzt:
         stage.scene().root = newRoot
      }
    }
  }
}