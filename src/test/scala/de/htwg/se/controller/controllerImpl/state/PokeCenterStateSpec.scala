package de.htwg.se.controller.controllerImpl.state

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers
import de.htwg.se.model.GameStateComponent.GameState
import de.htwg.se.model.PlayerComponent.IPlayer
import de.htwg.se.model.PokemonComponent.{IPokemon, Attack, PokemonType}

class PokeCenterStateSpec extends AnyWordSpec with Matchers {

  case class MockPokemon(currentHp: Int, maxHp: Int = 100) extends IPokemon {
    override def name: String = "TestMon"
    override def id: Int = 1
    override def pType: PokemonType = PokemonType.Normal
    override def secondaryType: Option[PokemonType] = None
    override def attacks: Vector[Attack] = Vector.empty
    override def spriteUrl: String = ""
    override def isFainted: Boolean = currentHp <= 0
    override def withHp(newHp: Int): IPokemon = copy(currentHp = newHp.max(0).min(maxHp))
    override def toString: String = name
  }

  case class MockPlayer(team: Vector[IPokemon]) extends IPlayer {
    override def name: String = "Tester"
    override def withTeam(newTeam: Vector[IPokemon]): IPlayer = copy(team = newTeam)
    override def activePokemon: IPokemon = team.head
    override def updatePokemon(p: IPokemon): IPlayer = this
    override def switchActivePokemon(index: Int): IPlayer = this
    override def addPokemon(p: IPokemon): IPlayer = this
    override def currentPokemonIndex: Int = 0
    override def items: Vector[String] = Vector.empty
    override def isActiveFainted: Boolean = false
    override def isDefeated: Boolean = false
    override def nextAlivePokemonIndex: Option[Int] = Some(0)
  }

  "A PokeCenterState" should {
    val damagedPoke = MockPokemon(10, 100)
    val player = MockPlayer(Vector(damagedPoke))
    val gs = GameState(player, player, false, "", "")
    val state = PokeCenterState(gs)

    "heal the team on input 'h'" in {
      val result = state.handle("h").asInstanceOf[PokeCenterState]
      result.gameState.player.team.head.currentHp should be (100)
      result.gameState.msg1 should include ("wieder fit")
    }

    "show a message for items" in {
      val result = state.handle("items").asInstanceOf[PokeCenterState]
      result.gameState.msg1 should include ("Shop ist noch leer")
    }

    "transition back to MenuState" in {
      val result = state.handle("back")
      result shouldBe a [MenuState]
    }

    "handle invalid input" in {
      val result = state.handle("xyz").asInstanceOf[PokeCenterState]
      result.gameState.msg2 should include ("Befehle: heilen, items, zurück")
    }
  }
}