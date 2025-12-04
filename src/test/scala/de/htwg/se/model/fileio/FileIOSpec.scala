package de.htwg.se.model.fileio

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers
import de.htwg.se.model.{Player, Pokemon, PokemonType, PokemonFactory}
import java.io.File

class XmlFileIOSpec extends AnyWordSpec with Matchers {
  "XmlFileIO" should {
    val fileIo = new XmlFileIO()
    val poke = PokemonFactory.getPokemon("Glurak")
    val player = Player("FileIOTestUser", Vector(poke))
    val filename = "save_FileIOTestUser.xml"

    "save a player correctly" in {
      val result = fileIo.save(player)
      result.isSuccess should be(true)
      new File(filename).exists() should be(true)
    }

    "load a player correctly" in {
      val result = fileIo.load("FileIOTestUser")
      result.isSuccess should be(true)
      result.get.name should be("FileIOTestUser")
      result.get.team.head.name should be("Glurak")
    }

    "fail gracefully when loading non-existent file" in {
      val result = fileIo.load("GibtsNicht12345")
      result.isFailure should be(true)
    }
    
    "list savegames" in {
       val list = fileIo.listSaveGames()
       list should contain ("FileIOTestUser")
       
       new File(filename).delete()
    }
  }
}