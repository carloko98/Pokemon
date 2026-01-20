package de.htwg.se.view.gui

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers
import de.htwg.se.controller.ControllerMockImpl.MockController
import javafx.embed.swing.JFXPanel
import scala.jdk.CollectionConverters._ // Wichtig für .asScala

class PokeCenterSceneSpec extends AnyWordSpec with Matchers {

  // Initialisiert die JavaFX Umgebung für den Test
  new JFXPanel()

  "A PokeCenterScene" should {
    val controller = new MockController()
    val scene = new PokeCenterScene(controller)

    "be created successfully with a header and content" in {
      scene should not be null
      scene.top.value should not be null    // Der rote Header
      scene.center.value should not be null // Der VBox Content
    }

    "contain the functional buttons with correct text" in {
      // Cast auf die JavaFX VBox, um auf die Kinder zuzugreifen
      val centerBox = scene.center.value.asInstanceOf[javafx.scene.layout.VBox]
      val children = centerBox.getChildren.asScala // Umwandlung in Scala-Liste

      val buttonTexts = children.collect { 
        case b: javafx.scene.control.Button => b.getText 
      }
      
      buttonTexts should contain ("Team Heilen")
      buttonTexts should contain ("Item Shop")
      buttonTexts should contain ("Zurück zum Menü")
    }
  }
}