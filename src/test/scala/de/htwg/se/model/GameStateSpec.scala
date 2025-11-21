package de.htwg.se.model

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers

class GameStateSpec extends AnyWordSpec with Matchers {

  val pikachu = Pokemon("PIKACHU", PokemonType.Electric, 60, 60, Vector.empty)
  val horsea  = Pokemon("HORSEA",  PokemonType.Water,     40, 40, Vector.empty)

  "Ein GameState" should {

    "mit Default-Werten initialisiert werden" in {
      val state = GameState(pikachu, horsea)
      state.player should be(pikachu)
      state.enemy should be(horsea)
      state.battleOver should be(false)
      state.msg1 should be("")
      state.msg2 should be("")
    }

    "immutable sein – copy erzeugt neue Instanz" in {
      val s1 = GameState(pikachu, horsea)
      val s2 = s1.copy(msg1 = "Angriff!", battleOver = true)

      s1.msg1 should be("")
      s2.msg1 should be("Angriff!")
      s2.battleOver should be(true)
      s1 should not be theSameInstanceAs(s2)
    }

    "nur geaenderte Felder ueberschreiben" in {
      val s1 = GameState(pikachu, horsea)
      val s2 = s1.copy(enemy = s1.enemy.withHp(10))
      val s3 = s2.copy(battleOver = true, msg1 = "Gewonnen!")

      s3.enemy.currentHp should be(10)
      s3.battleOver should be(true)
      s3.player should be(pikachu)
    }
  }
}