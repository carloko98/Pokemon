package de.htwg.se.view.gui

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers
import de.htwg.se.controller.ControllerMockImpl.MockController
import javafx.embed.swing.JFXPanel

class TitleSceneSpec extends AnyWordSpec with Matchers {
  new JFXPanel()

  "A TitleScene" should {
    val controller = new MockController()
    val scene = new TitleScene(controller)

    "initialize correctly" in {
      scene.children.size should be(4)
    }
  }
}