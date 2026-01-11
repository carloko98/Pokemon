package de.htwg.se.view.gui

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers
import de.htwg.se.controller.ViewState
import de.htwg.se.controller.ControllerMockImpl.MockController
import javafx.embed.swing.JFXPanel
import javafx.scene.layout.{VBox => JfxVBox}

class BattleSceneSpec extends AnyWordSpec with Matchers {

  new JFXPanel()

  "A BattleScene" should {
    val controller = new MockController()

    "display player actions when in VPlayerAtk state" in {
      controller.setViewState(ViewState.VPlayerAtk)
      val scene = new BattleScene(controller)
      
      scene should not be null
      val bottomNode = scene.bottom.value.asInstanceOf[JfxVBox]
      bottomNode.getChildren.size should be > 0
    }

    "display wait button when in VEnemyAtk state" in {
      controller.setViewState(ViewState.VEnemyAtk)
      val scene = new BattleScene(controller)
      
      scene should not be null
      val bottomNode = scene.bottom.value.asInstanceOf[JfxVBox]
      bottomNode.getChildren.size should be > 0
    }
  }
}