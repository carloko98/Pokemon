package de.htwg.se.controller

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers
import de.htwg.se.model._
import de.htwg.se.controller.state._
import de.htwg.se.model.PokemonType._

class ControllerSpec extends AnyWordSpec with Matchers {

  // Dummy-Pokémon – schwach, damit kein echter Kampf aus Versehen beendet wird
  val dummy = Pokemon(
    name = "Dummy",
    maxHp = 20,
    currentHp = 20,
    pType = Normal,
    attacks = Vector(Attack("Tackle", 1, Normal))
  )

  val player = Player("Ash", Vector(dummy))
  val enemy = Player("Gary", Vector(dummy))

  "A Controller" should {

    "start in TitleState" in {
      val controller = new Controller(player, enemy)
      controller.state shouldBe a[TitleState]
    }

    "handle SelectProfileState correctly (press 'b' returns to TitleState)" in {
      val controller = new Controller(player, enemy)
      controller.state = SelectProfileState(GameState(player, enemy))
      controller.handleInput("b")
      controller.state shouldBe a[TitleState]
    }

    "handle NameInputState correctly" in {
      val controller = new Controller(player, enemy)
      controller.state = NameInputState(GameState(player, enemy))
      controller.handleInput("Eevee")
      controller.state shouldBe a[MenuState]
      controller.getPlayer.name shouldBe "Eevee"
    }

    "start a wild battle from MenuState" in {
      val controller = new Controller(player, enemy)
      controller.state = MenuState(GameState(player, enemy))
      controller.handleInput("s")
      controller.state shouldBe a[PlayerAttackState]
    }

    "start a trainer battle from MenuState" in {
      val controller = new Controller(player, enemy)
      controller.state = MenuState(GameState(player, enemy))
      controller.handleInput("t")
      controller.state shouldBe a[PlayerAttackState]
    }

    "save the game explicitly when 'save' is entered" in {
      val controller = new Controller(player, enemy)
      controller.state = MenuState(GameState(player, enemy))
      controller.handleInput("save")
      // Test besteht, wenn kein Fehler auftritt
    }

    "auto-save when a battle ends (wasBattleState coverage)" in {
      val controller = new Controller(player, enemy)

      // simulate that fight has ended
      val finalState = PlayerAttackState(
        GameState(player, enemy, battleOver = true),
        WildBattleLogic
      )

      controller.state = finalState
      controller.handleInput("1") // controller muss zum Menü wechseln → auto-save
      controller.state shouldBe a[MenuState]
    }

    "list save files" in {
      val controller = new Controller(player, enemy)
      controller.getAvailableSaves shouldBe a[List[String]]
    }
  }
}
