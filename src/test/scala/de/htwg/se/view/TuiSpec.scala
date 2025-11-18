package de.htwg.se.view

import de.htwg.se.controller.Controller
import de.htwg.se.model.{Pokemon, Attack, PokemonType}
import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers._
import java.io.{ByteArrayOutputStream, PrintStream}

class TuiSpec extends AnyWordSpec {

  // --- Test Daten Setup ---
  val tackle = Attack("Tackle", 6, PokemonType.Normal)
  val bubble = Attack("Bubble", 4, PokemonType.Water)

  val pikachu = Pokemon("PIKACHU", PokemonType.Electric, 34, 34, Vector(tackle))
  val horsea = Pokemon("HORSEA", PokemonType.Water, 40, 40, Vector(bubble))

  // Immutable Controller Instanz erstellen
  val ctrl = Controller(pikachu, horsea)
  val tui = new Tui(ctrl)

  "A Tui" should {

    "print the intro correctly" in {
      val out = new ByteArrayOutputStream()
      Console.withOut(out) {
        tui.intro()
      }
      val output = out.toString
      // Prüfen ob wichtige Elemente im Intro enthalten sind
      output should include("Ein wildes HORSEA ist erschienen!")
      output should include("+------------------------------------------------------------+")
    }

    "calculate the HP bar correctly (private method)" in {
      // Zugriff auf private Methode 'hpBar' via Reflection
      val method = classOf[Tui].getDeclaredMethod("hpBar", classOf[Int], classOf[Int])
      method.setAccessible(true)

      // Test 1: 50% HP (ca. 6-7 #)
      val bar50 = method.invoke(tui, 50: Integer, 100: Integer).asInstanceOf[String]
      bar50 should startWith("######") 
      
      // Test 2: 0 HP (leer)
      val bar0 = method.invoke(tui, 0: Integer, 100: Integer).asInstanceOf[String]
      bar0 should be("-------------")

      // Test 3: Voll (13 #)
      val barFull = method.invoke(tui, 100: Integer, 100: Integer).asInstanceOf[String]
      barFull should be("#############")
    }

    "render the battle field correctly (private method)" in {
      // Da 'render' private ist, nutzen wir Reflection oder capturen den Output
      // Hier via Reflection den Aufruf erzwingen und Output abfangen
      val out = new ByteArrayOutputStream()
      Console.withOut(out) {
        val method = classOf[Tui].getDeclaredMethod("render")
        method.setAccessible(true)
        method.invoke(tui)
      }
      
      val output = out.toString

      // 1. Wurde der Screen gecleared?
      output should include("\u001b[2J\u001b[H")
      
      // 2. Sind die Namen da?
      output should include("PIKACHU")
      output should include("HORSEA")
      
      // 3. Sind die HP Werte da?
      output should include("34/34")
      output should include("40/40")
      
      // 4. Ist das Menü da? (Da Kampf nicht vorbei ist)
      output should include("1. Tackle")
      output should include("f. Fliehen")
    }
    
    // Hinweis: 'inputLoop' ist schwer zu testen, da es eine Endlosschleife mit User-Input ist.
    // Man müsste System.in mocken, was in Unit-Tests oft zu Problemen führt (hängende Tests).
    // Da wir die Logik (Controller) separat getestet haben, ist das okay.
  }
}