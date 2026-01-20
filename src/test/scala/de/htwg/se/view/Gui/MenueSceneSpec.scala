package de.htwg.se.view.gui

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers
import de.htwg.se.controller.ControllerMockImpl.MockController
import javafx.embed.swing.JFXPanel

class MenuSceneSpec extends AnyWordSpec with Matchers {
  new JFXPanel()

  "A MenuScene" should {
    val controller = new MockController()
    val scene = new MenuScene(controller)

    "be initialized correctly" in {
      scene.children.size should be(7)
    }
  }
}