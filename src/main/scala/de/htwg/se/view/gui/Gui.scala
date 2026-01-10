package de.htwg.se.view.gui

import scalafx.application.JFXApp3
import scalafx.application.Platform
import scalafx.scene.Scene
import scalafx.scene.Parent
import scalafx.Includes._ 
import de.htwg.se.util.Observer
import de.htwg.se.controller.IController

// WICHTIG: Importiere die ViewStates, damit das 'match' funktioniert
import de.htwg.se.controller.{
  TitleState, 
  MenuState, 
  NameInputState, 
  SelectProfileState, 
  PlayerAttackState, 
  EnemyAttackState
}

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
      // Matching auf die abstrakten ViewStates
      val newRoot: Parent = controller.viewState match {
        case TitleState         => new TitleScene(controller)
        case MenuState          => new MenuScene(controller)
        case NameInputState     => new NameInputScene(controller)
        case SelectProfileState => new SelectProfileScene(controller)
        case PlayerAttackState | EnemyAttackState => new BattleScene(controller)
      }
      
      if (stage != null && stage.scene() != null) {
         stage.scene().root = newRoot
      }
    }
  }
}