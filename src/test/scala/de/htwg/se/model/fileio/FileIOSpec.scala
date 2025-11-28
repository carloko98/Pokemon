package de.htwg.se.model.fileio

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers
import de.htwg.se.model.{Player, Pokemon, Attack, PokemonType}
import java.io.File

class FileIOSpec extends AnyWordSpec with Matchers {

  "A XmlFileIO" should {
    val fileIo = new XmlFileIO()
    
    val attack = Attack("TestHieb", 10, PokemonType.Normal)
    val pokemon = Pokemon("Glurak", PokemonType.Fire, 150, 150, Vector(attack)) 
    val player = Player("TestUser_Unit_Test", Vector(pokemon))
    val filename = "save_TestUser_Unit_Test.xml"

    "save and reload a player correctly" in {
      fileIo.save(player)
      
      val file = new File(filename)
      file.exists() should be(true)

      val loadedPlayer = fileIo.load("TestUser_Unit_Test")
      
      loadedPlayer.name should be(player.name)
      loadedPlayer.team.head.name should be(player.team.head.name)
      loadedPlayer.team.head.currentHp should be(player.team.head.currentHp)

      file.delete()
    }

    "list existing savegames" in {
      fileIo.save(player)
      
      val list = fileIo.listSaveGames()
      list should contain("TestUser_Unit_Test")
      
      new File(filename).delete()
    }

    "handle errors gracefully when saving (coverage for catch block)" in {
      val invalidPlayer = Player("Inv/alid/Name", Vector())
      noException should be thrownBy fileIo.save(invalidPlayer)
    }
  }
}