package de.htwg.se.model.fileio

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers.*
import org.scalatest.BeforeAndAfterAll
import de.htwg.se.model.*
import de.htwg.se.model.PokemonType.*
import java.io.File

class FileIOSpec extends AnyWordSpec with BeforeAndAfterAll:

  val fileio = new XmlFileIO()   // <-- korrekt instanziiert

  val player = Player(
    name = "Ash",
    team = Vector(
      Pokemon("Glurak", Fire, 150, 120, Vector()),
      Pokemon("Bisaflor", Grass, 160, 0, Vector())
    )
  )

  "XmlFileIO" should {

    "save a player to XML and create a matching file" in {
      fileio.save(player)
      val file = File("save_Ash.xml")
      file.exists() shouldBe true
    }

    "load a saved player correctly from XML" in {
      val loaded = fileio.load("Ash")

      loaded.name shouldBe "Ash"
      loaded.team.size shouldBe 2
      loaded.team(0).name shouldBe "Glurak"
      loaded.team(0).currentHp shouldBe 120
      loaded.team(1).isFainted shouldBe true
    }

    "list existing savegames" in {
      val list = fileio.listSaveGames()
      list should contain ("Ash")
    }
  }

  // Cleanup nach allen Tests
  override def afterAll(): Unit =
    val file = File("save_Ash.xml")
    if file.exists() then file.delete()
