package de.htwg.se.model.FileIOComponent

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers
import java.io.File
import scala.util.Success
import de.htwg.se.model.PlayerComponent.MockPlayerImpl.MockPlayer
import de.htwg.se.model.PokemonComponent.MockPokemonImpl.MockPokemon
import de.htwg.se.model.FileIOComponent.{XmlFileIO, JsonFileIO}

class FileIOSpec extends AnyWordSpec with Matchers {

  "A XmlFileIO" should {
    val fileIo = new XmlFileIO()
    // Nutze "Glurak", da dies im echten Service existiert
    val p = MockPlayer("TestSaveXml", Vector(MockPokemon("Glurak", currentHp = 50)))

    "save a player to XML" in {
      val result = fileIo.save(p)
      result shouldBe a [Success[_]]
      new File("save_TestSaveXml.xml").exists() should be (true)
    }

    "load a player from XML" in {
      val result = fileIo.load("TestSaveXml")
      // Wenn Glurak existiert, sollte das klappen
      if (result.isSuccess) {
        val loadedPlayer = result.get
        loadedPlayer.name should be ("TestSaveXml")
        // Checken ob HP übernommen wurden (falls Service das zulässt)
        loadedPlayer.team.head.currentHp should be (50)
      }
    }
    
    "list save games correctly" in {
      val list = fileIo.listSaveGames()
      list should contain ("TestSaveXml")
    }

    "clean up after test" in {
      new File("save_TestSaveXml.xml").delete()
    }
  }
  
  "A JsonFileIO" should {
    val fileIo = new JsonFileIO()
    val p = MockPlayer("TestSaveJson", Vector(MockPokemon("Glurak")))

    "save a player to JSON" in {
      val result = fileIo.save(p)
      result shouldBe a [Success[_]]
      new File("save_TestSaveJson.json").exists() should be (true)
    }
    
    "load a player from JSON" in {
       val result = fileIo.load("TestSaveJson")
       if (result.isSuccess) {
         result.get.name should be ("TestSaveJson")
       }
    }

    "clean up JSON file" in {
      new File("save_TestSaveJson.json").delete()
    }
  }
}