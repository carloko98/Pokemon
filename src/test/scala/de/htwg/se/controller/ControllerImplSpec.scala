package de.htwg.se.controller


import de.htwg.se.model.{Pokemon, Attack, PokemonType} 

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers._
import de.htwg.se.controller.{Controller, ControllerImpl, Observer}

/**
 * Test-Spezifikation für den ControllerImpl.
 * Nutzt BDD (Behaviour Driven Development) Stil.
 * * Ziel: 100% Code Coverage.
 */
class ControllerImplSpec extends AnyWordSpec {

  // ===================================================================
  // TEST-SETUP (Definitionen auf Klassenebene)
  // Diese vals sind jetzt für alle Methoden und Tests in dieser Klasse sichtbar.
  // ===================================================================

  // --- Attacken-Definitionen ---
  val tackle = Attack("Tackle", 10, PokemonType.Normal)
  val electroBall = Attack("Electro Ball", 20, PokemonType.Electric)
  val bubble = Attack("Bubble", 15, PokemonType.Water)
  val leafBlade = Attack("Leaf Blade", 20, PokemonType.Grass)

  // --- Basis-Pokémon-Definitionen ---

  val basePikachu = Pokemon("PIKACHU", PokemonType.Electric, 100, 100, Vector(electroBall))
  val baseHorsea = Pokemon("HORSEA", PokemonType.Water, 100, 100, Vector(bubble))
  val baseDigda = Pokemon("DIGDA", PokemonType.Ground, 100, 100, Vector(tackle))
  val baseBisasam = Pokemon("BISASAM", PokemonType.Grass, 100, 100, Vector(leafBlade))

  /**
   * HILFSMETHODE:
   * Erstellt eine saubere Testumgebung (Controller + Pokémon) für Standard-Tests.
   * Verwendet Pokémon mit niedriger HP für Sieg/Niederlage-Tests.
   */
  def createTestController: (ControllerImpl, Pokemon, Pokemon, Attack) = {

    val pikachuTest = Pokemon("PIKACHU", PokemonType.Electric, 34, 34, Vector(electroBall, tackle))
    val horseaTest = Pokemon("HORSEA", PokemonType.Water, 40, 40, Vector(bubble, tackle))
    
    val controller = new ControllerImpl(pikachuTest, horseaTest)
    (controller, pikachuTest, horseaTest, tackle) // Gibt 'tackle' für Angriffs-Tests zurück
  }

  /**
   * HILFS-KLASSE (INNER CLASS):
   * Ein Test-Observer, der prüft, ob er benachrichtigt wurde.
   */
  class TestObserver(controller: Controller) extends Observer {
    controller.addObserver(this)
    var updateCalled = false
    override def update(): Unit = updateCalled = true
  }

  // ===================================================================
  // TESTS
  // ===================================================================

  "Ein ControllerImpl" should {

    "mit korrekten Pokémon initialisieren" in {
      val (ctrl, player, enemy, _) = createTestController
      ctrl.player should be(player)
      ctrl.enemy should be(enemy)
      ctrl.isBattleOver should be(false)
    }

    "den Kampf beenden, wenn der Spieler gewinnt" in {
      val (ctrl, _, _, tackle) = createTestController
      // Gegner hat 40 HP. Wir setzen ihn auf 5 HP, damit er besiegt wird.
      val weakEnemy = ctrl.enemy.withHp(5) 
      ctrl.enemy = weakEnemy 

      ctrl.doPlayerAttack(tackle) // 10 Schaden (Tackle) > 5 HP -> KO
      
      ctrl.isBattleOver should be(true)
      ctrl.getMessage should be(("Du hast gewonnen!", "HORSEA ist besiegt!"))
    }

    "den Kampf beenden, wenn der Spieler verliert" in {
      val (ctrl, _, _, tackle) = createTestController
      // Spieler hat 34 HP. Wir setzen ihn auf 5 HP.
      val weakPlayer = ctrl.player.withHp(5)
      ctrl.player = weakPlayer

      // Spieler greift an (10 Schaden), Gegner überlebt.
      // Gegner (Horsea) kontert mit 'bubble' (15 Schaden) > 5 HP -> KO
      ctrl.doPlayerAttack(tackle) 
                                  
      ctrl.isBattleOver should be(true)
      ctrl.player.currentHp should be(0)
      ctrl.getMessage should be(("Du hast verloren!", "PIKACHU ist besiegt!"))
    }

    "Fliehen korrekt verarbeiten" in {
      val (ctrl, _, _, _) = createTestController
      ctrl.doFlee()
      ctrl.isBattleOver should be(true)
      ctrl.getMessage should be(("Du bist geflohen!", ""))
    }

    "Aktionen ignorieren, wenn der Kampf vorbei ist" in {
      // Testet: if (battleOver) return
      val (ctrl, player, enemy, tackle) = createTestController
      
      ctrl.doFlee() // 1. Kampf beenden
      
      // 2. Zustand speichern
      val initialEnemyHp = ctrl.enemy.currentHp
      val (initialMsg1, initialMsg2) = ctrl.getMessage
      val observer = new TestObserver(ctrl)
      observer.updateCalled = false

      // 3. Aktion: Erneut angreifen
      ctrl.doPlayerAttack(tackle) 

      // 4. Assertion: Nichts darf sich geändert haben
      ctrl.enemy.currentHp should be(initialEnemyHp)
      ctrl.getMessage should be((initialMsg1, initialMsg2))
      observer.updateCalled should be(false)
    }

    "Observer bei Aktionen benachrichtigen" in {
      val (ctrl, _, _, tackle) = createTestController
      val observer = new TestObserver(ctrl)

      // Test 1: Benachrichtigung bei Angriff
      observer.updateCalled = false
      ctrl.doPlayerAttack(tackle)
      observer.updateCalled should be(true)

      // Test 2: Benachrichtigung bei Flucht
      val (ctrl2, _, _, _) = createTestController
      val observer2 = new TestObserver(ctrl2)
      observer2.updateCalled = false 
      ctrl2.doFlee()
      observer2.updateCalled should be(true)
    }


    "show correct effectiveness messages" in {

      // Test 1: SEHR EFFEKTIV (2.0x)
   
      val ctrlSuper = new ControllerImpl(baseHorsea.copy(), baseBisasam.copy()) 
      ctrlSuper.doPlayerAttack(bubble) // Spieler greift an, Gegner überlebt.
      ctrlSuper.getMessage._2 should endWith("Sehr effektiv!")

      // Test 2: NICHT SEHR EFFEKTIV (0.5x)
     
      val ctrlNot = new ControllerImpl(baseBisasam.copy(), baseHorsea.copy())
      ctrlNot.doPlayerAttack(leafBlade) // Spieler greift an, Gegner überlebt.
      ctrlNot.getMessage._2 should endWith("Nicht sehr effektiv...")

      // Test 3: IMMUN (0.0x)
    
      val ctrlImmune = new ControllerImpl(baseDigda.copy(), basePikachu.copy())
      ctrlImmune.doPlayerAttack(tackle) // Spieler greift an, Gegner überlebt.
      ctrlImmune.getMessage._2 should endWith("Hat keine Wirkung!")
      
      // Test 4: NEUTRAL (1.0x)
    
      val ctrlNeutral = new ControllerImpl(basePikachu.copy(), baseHorsea.copy()) 
      ctrlNeutral.doPlayerAttack(electroBall) // Spieler greift an, Gegner überlebt.
      
      ctrlNeutral.getMessage._2 should endWith("!") // Es MUSS mit "!" enden
      ctrlNeutral.getMessage._2 should not endWith("Sehr effektiv!")
      ctrlNeutral.getMessage._2 should not endWith("Nicht sehr effektiv...")
      ctrlNeutral.getMessage._2 should not endWith("Hat keine Wirkung!")
    }
  }
}