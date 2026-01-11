package de.htwg.se.view.gui

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers
import de.htwg.se.controller.ControllerMockImpl.MockController
import javafx.embed.swing.JFXPanel
import scalafx.scene.layout.VBox
import scalafx.scene.control.ScrollPane

class SelectProfileSceneSpec extends AnyWordSpec with Matchers {
  new JFXPanel()

  "A SelectProfileScene" should {
    val controller = new MockController()
    
    "initialize with empty list correctly" in {
      val scene = new SelectProfileScene(controller)
      scene.children.size should be(4)
    }

    "show saves in the list" in {
      val scene = new SelectProfileScene(controller)
      val scrollPane = scene.children(2).asInstanceOf[javafx.scene.control.ScrollPane]
      val container = scrollPane.getContent.asInstanceOf[javafx.scene.layout.VBox]
      
      container.getChildren.size should be > 0
    }
  }
}