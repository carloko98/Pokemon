package de.htwg.se.view.gui

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers
import de.htwg.se.controller.ViewState
import de.htwg.se.controller.ControllerMockImpl.MockController 
import javafx.embed.swing.JFXPanel

class GuiSpec extends AnyWordSpec with Matchers {

  new JFXPanel()

  "A Gui" should {
    val controller = new MockController()
    val gui = new Gui(controller)

    "update successfully for VTitle" in {
      controller.setViewState(ViewState.VTitle)
      noException should be thrownBy gui.update()
    }

    "update successfully for VMenu" in {
      controller.setViewState(ViewState.VMenu)
      noException should be thrownBy gui.update()
    }

    "update successfully for VNameInput" in {
      controller.setViewState(ViewState.VNameInput)
      noException should be thrownBy gui.update()
    }

    "update successfully for VSelectProfile" in {
      controller.setViewState(ViewState.VSelectProfile)
      noException should be thrownBy gui.update()
    }

    "update successfully for VPlayerAtk" in {
      controller.setViewState(ViewState.VPlayerAtk)
      noException should be thrownBy gui.update()
    }

    "update successfully for VEnemyAtk" in {
      controller.setViewState(ViewState.VEnemyAtk)
      noException should be thrownBy gui.update()
    }
  }
}