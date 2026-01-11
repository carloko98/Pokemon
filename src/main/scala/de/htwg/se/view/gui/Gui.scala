package de.htwg.se.view.gui

import scalafx.application.JFXApp3
import scalafx.application.Platform
import scalafx.scene.Scene
import scalafx.scene.Parent
import scalafx.Includes._ 
import de.htwg.se.util.Observer
import de.htwg.se.controller.IController

import de.htwg.se.controller.ViewState
import de.htwg.se.controller.ViewState._ 

class Gui(val controller: IController) extends JFXApp3 with Observer {

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
      val newRoot: Parent = controller.viewState match {
        case VTitle         => new TitleScene(controller)
        case VMenu          => new MenuScene(controller)
        case VNameInput     => new NameInputScene(controller)
        case VSelectProfile => new SelectProfileScene(controller)
        case VPlayerAtk | VEnemyAtk => new BattleScene(controller)
      }
      
      if (stage != null && stage.scene() != null) {
         stage.scene().root = newRoot
      }
    }
  }
}