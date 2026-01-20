package de.htwg.se.view.gui

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers
import de.htwg.se.controller.ControllerMockImpl.MockController
import javafx.embed.swing.JFXPanel
import scala.jdk.CollectionConverters._ // Der wichtigste Import für .asScala

class SwitchPokemonSceneSpec extends AnyWordSpec with Matchers {

  // Initialisiert die JavaFX Umgebung für den Test
  new JFXPanel()

  "A SwitchPokemonScene" should {
    val controller = new MockController()
    val scene = new SwitchPokemonScene(controller)

    "display the correct title and error labels" in {
      scene should not be null
      // .asScala macht aus der Java-Liste eine Scala-Collection
      val labels = scene.getChildren.asScala.collect { 
        case l: javafx.scene.control.Label => l.getText 
      }
      labels should contain ("Wähle ein Pokemon")
    }

    "have buttons for each Pokemon in the team and a back button" in {
      // Auch hier .asScala verwenden, damit 'collect' funktioniert
      val buttons = scene.getChildren.asScala.collect { 
        case b: javafx.scene.control.Button => b 
      }
      
      // Es sollte mindestens der eine Player-Mon-Button + Back-Button existieren
      buttons.size should be >= 2
      
      val buttonTexts = buttons.map(_.getText)
      buttonTexts.exists(_.contains("Zurück")) shouldBe true
    }

    "have proper styles set on buttons" in {
      val buttons = scene.getChildren.asScala.collect { 
        case b: javafx.scene.control.Button => b 
      }
      
      buttons.foreach { btn =>
        btn.getStyle should not be empty
      }
    }
  }
}