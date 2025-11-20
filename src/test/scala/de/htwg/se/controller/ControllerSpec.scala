package de.htwg.se.controller

import de.htwg.se.model.{Pokemon, Attack, PokemonType}
import de.htwg.se.util.Observer
import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers._

class ControllerSpec extends AnyWordSpec {

  // ==================== TEST-DATEN ====================
  val tackle = Attack("Tackle", 10, PokemonType.Normal)
  val bubble = Attack("Bubble", 15, PokemonType.Water)
  val electroBall = Attack("Electro Ball", 20, PokemonType.Electric)
  val leafBlade = Attack("Leaf Blade", 20, PokemonType.Grass)

  val basePikachu = Pokemon("PIKACHU", PokemonType.Electric, 100, 100, Vector(electroBall, tackle))
  val baseHorsea = Pokemon("HORSEA", PokemonType.Water, 100, 100, Vector(bubble, tackle))
  val baseDigda = Pokemon("DIGDA", PokemonType.Ground, 100, 100, Vector(tackle))
  val baseBisasam = Pokemon("BISASAM", PokemonType.Grass, 100, 100, Vector(leafBlade))

  // Hilfs-Observer für Tests
  class TestObserver extends Observer {
    var updated: Boolean = false
    override def update(): Unit = updated = true
  }

  "Ein Controller" should {

    "korrekt initialisiert werden" in {
      val ctrl = new Controller(basePikachu.copy(), baseHorsea.copy())
      
      
      ctrl.player should be(basePikachu)
      ctrl.enemy should be(baseHorsea)
      ctrl.battleOver should be(false) 
      ctrl.getMessage should be(("", ""))
    }

    "Observer benachrichtigen bei Änderungen" in {
      val ctrl = new Controller(basePikachu.copy(), baseHorsea.copy())
      val observer = new TestObserver
      ctrl.add(observer)

      ctrl.doPlayerAttack(tackle)
      
      observer.updated should be(true)
    }

    "den Kampf beenden, wenn der Spieler gewinnt" in {
      // Setup: Gegner hat nur 5 HP
      val weakEnemy = baseHorsea.withHp(5)
      val ctrl = new Controller(basePikachu.copy(), weakEnemy)

      ctrl.doPlayerAttack(tackle)

      
      ctrl.battleOver should be(true)
      ctrl.getMessage._1 should be("Du hast gewonnen!")
      ctrl.getMessage._2 should be("HORSEA ist besiegt!")
    }

    "den Kampf beenden, wenn der Spieler verliert" in {
      // Setup: Spieler hat nur 5 HP
      val weakPlayer = basePikachu.withHp(5)
      val ctrl = new Controller(weakPlayer, baseHorsea.copy())

      // Gegner greift zurück an (da er überlebt) -> Spieler verliert
      ctrl.doPlayerAttack(tackle)

      ctrl.battleOver should be(true)
      ctrl.getMessage._1 should be("Du hast verloren!")
      ctrl.getMessage._2 should be("PIKACHU ist besiegt!")
    }

    "Fliehen korrekt verarbeiten" in {
      val ctrl = new Controller(basePikachu.copy(), baseHorsea.copy())

      ctrl.doFlee()

      ctrl.battleOver should be(true)
      ctrl.getMessage._1 should be("Du bist geflohen!")
    }

    "Aktionen ignorieren, wenn der Kampf bereits vorbei ist" in {
      val ctrl = new Controller(basePikachu.copy(), baseHorsea.copy())
      
      // 1. Kampf beenden
      ctrl.doFlee()
      val msgAfterFlee = ctrl.getMessage
      val hpBefore = ctrl.enemy.currentHp

      // 2. Versuchen anzugreifen
      ctrl.doPlayerAttack(tackle)

      // 3. Assert: HP und Nachricht dürfen sich NICHT geändert haben
      ctrl.enemy.currentHp should be(hpBefore)
      ctrl.getMessage should be(msgAfterFlee)
    }
    
    "Typ-Effektivität korrekt berechnen" in {
       // Setup: Starker Gegner
       val strongHorsea = baseHorsea.withHp(1000)
       val ctrlSuper = new Controller(basePikachu.copy(), strongHorsea)
       
       // Merken der HP vor dem Angriff
       val hpBefore = ctrlSuper.enemy.currentHp
       
       ctrlSuper.doPlayerAttack(electroBall)
       
       // Prüfen des Schadens (20 * 2.0 = 40)
       val damage = hpBefore - ctrlSuper.enemy.currentHp
       damage should be(40)
    }

    "die privaten Effektivitäts-Nachrichten korrekt formatieren (Reflection)" in {
      // Wir nutzen einen Dummy-Controller (Inhalt egal, da effMsg keine Zustände braucht)
      val ctrl = new Controller(basePikachu.copy(), baseHorsea.copy())
      
      // Zugriff auf die private Methode 'effMsg' via Reflection
      val method = classOf[Controller].getDeclaredMethod("effMsg", classOf[Double])
      method.setAccessible(true)

      // Fall 1
      val msg0 = method.invoke(ctrl, 0.0).asInstanceOf[String]
      msg0 should include("Hat keine Wirkung!")

      // Fall 2
      val msg05 = method.invoke(ctrl, 0.5).asInstanceOf[String]
      msg05 should include("Nicht sehr effektiv...")

      // Fall 3
      val msg20 = method.invoke(ctrl, 2.0).asInstanceOf[String]
      msg20 should include("Sehr effektiv!")

      // Fall 4
      val msg10 = method.invoke(ctrl, 1.0).asInstanceOf[String]
      msg10 should be("")
    }
  }
}