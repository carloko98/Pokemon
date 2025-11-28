package de.htwg.se.controller

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers
import de.htwg.se.model._
import de.htwg.se.model.PokemonType._
import de.htwg.se.controller.state._

class ControllerStateSpec extends AnyWordSpec with Matchers {

  val pikachu = Pokemon(
    name = "Pikachu",
    pType = Electric,
    maxHp = 100,
    currentHp = 100,
    attacks = Vector(
      Attack("Tackle", 40, Normal),
      Attack("Thunderbolt", 90, Electric),
      Attack("Quick Attack", 40, Normal),
      Attack("Iron Tail", 100, Steel)
    )
  )

  val charizard = Pokemon(
    name = "Charizard",
    pType = Fire,
    maxHp = 120,
    currentHp = 120,
    attacks = Vector(
      Attack("Flamethrower", 90, Fire),
      Attack("Fly", 70, Flying)
    )
  )

  val player = Player(name = "Ash", team = Vector(pikachu.copy(currentHp = 80)))
  val enemy = Player(name = "Gary", team = Vector(charizard.copy(currentHp = 100)))

  val gameState = GameState(player = player, enemy = enemy)

  "PlayerAttackState" should {
    "reduce enemy HP when attack is used" in {
      val state = PlayerAttackState(gameState, WildBattleLogic)
      val nextState = state.handle("1") // Attack index 1
      nextState.gameState.enemy.activePokemon.currentHp should be < enemy.activePokemon.currentHp
    }

    "end battle if enemy faints" in {
      val weakEnemy = Player(name = "Gary", team = Vector(charizard.copy(currentHp = 10)))
      val gs = GameState(player = player, enemy = weakEnemy)
      val state = PlayerAttackState(gs, WildBattleLogic)
      val nextState = state.handle("2") // Thunderbolt
      nextState shouldBe a[MenuState]
      nextState.gameState.battleOver shouldBe true
    }

    "not allow fleeing in trainer battle" in {
      val state = PlayerAttackState(gameState, TrainerBattleLogic)
      val nextState = state.handle("f")
      nextState.gameState.msg2 should include("Flucht unmöglich")
    }

    "allow fleeing in wild battle" in {
      val state = PlayerAttackState(gameState, WildBattleLogic)
      val nextState = state.handle("f")
      nextState shouldBe a[MenuState]
      nextState.gameState.battleOver shouldBe true
    }
  }

  "NameInputState" should {
    "reject empty name" in {
      val state = NameInputState(gameState)
      val nextState = state.handle("   ")
      nextState.gameState.msg2 should include("nicht leer")
    }

    "create new player and move to menu" in {
      val state = NameInputState(gameState)
      val nextState = state.handle("Ash")
      nextState shouldBe a[MenuState]
      nextState.gameState.player.name shouldBe "Ash"
      nextState.gameState.player.team.map(_.name) should contain allOf ("Glurak", "Bisaflor")
    }
  }

  "MenuState" should {
    "start wild battle" in {
      val state = MenuState(gameState)
      val nextState = state.handle("s")
      nextState shouldBe a[PlayerAttackState]
    }

    "start trainer battle" in {
      val state = MenuState(gameState)
      val nextState = state.handle("t")
      nextState shouldBe a[PlayerAttackState]
    }

    "handle unknown input" in {
      val state = MenuState(gameState)
      val nextState = state.handle("xyz")
      nextState.gameState.msg1 should include("Unbekannter Befehl")
    }
  }
}
