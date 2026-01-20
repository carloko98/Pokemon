package de.htwg.se.controller.controllerImpl.state

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers
import de.htwg.se.model.GameStateComponent.GameState
import de.htwg.se.model.PlayerComponent.IPlayer
import de.htwg.se.model.PokemonComponent.{IPokemon, Attack, PokemonType}

class NameInputStateSpec extends AnyWordSpec with Matchers {

  // 1. Mock für Pokemon (mit allen neuen Feldern)
  case class MockPokemon() extends IPokemon {
    override def name: String = "TestMon"
    override def id: Int = 0                                     // NEU
    override def pType: PokemonType = PokemonType.Normal
    override def secondaryType: Option[PokemonType] = None       // NEU
    override def maxHp: Int = 100
    override def currentHp: Int = 100
    override def attacks: Vector[Attack] = Vector.empty
    override def spriteUrl: String = ""                          // NEU
    override def isFainted: Boolean = false
    override def withHp(newHp: Int): IPokemon = this
    override def toString: String = name
  }

  // 2. Mock für Player (mit allen neuen Methoden)
  case class MockPlayer(name: String) extends IPlayer {
    override def team: Vector[IPokemon] = Vector(MockPokemon())
    override def withTeam(newTeam: Vector[IPokemon]): IPlayer = this // NEU
    override def currentPokemonIndex: Int = 0
    override def items: Vector[String] = Vector.empty
    override def activePokemon: IPokemon = MockPokemon()
    override def updatePokemon(p: IPokemon): IPlayer = this
    override def switchActivePokemon(index: Int): IPlayer = this
    override def addPokemon(p: IPokemon): IPlayer = this
    override def isActiveFainted: Boolean = false
    override def isDefeated: Boolean = false
    override def nextAlivePokemonIndex: Option[Int] = Some(0)
  }

  "A NameInputState" should {
    val p = MockPlayer("Old")
    val e = MockPlayer("Enemy")
    val gs = GameState(p, e, false, "", "")
    val state = NameInputState(gs)

    "stay in NameInputState on empty input" in {
      val result = state.handle("   ")
      result shouldBe a [NameInputState]
      result.asInstanceOf[NameInputState].gameState.msg2 should be ("Name darf nicht leer sein!")
    }

    "transition to MenuState on valid input" in {
      val result = state.handle("Ash")
      result shouldBe a [MenuState]
      val menuState = result.asInstanceOf[MenuState]
      
      // Da NameInputState den PokemonService nutzt, wird hier ein echter Player 
      // erstellt (kein MockPlayer), daher funktioniert der Test jetzt.
      menuState.gameState.player.name should be("Ash")
      menuState.gameState.msg1 should be("Hallo Ash!")
    }
  }
}