package de.htwg.se.model.fileio

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers.*
import de.htwg.se.model.*
import de.htwg.se.model.PokemonType.*
import java.io.File

class FileIOSpec extends AnyWordSpec:

  val fileio = new XmlFileIO()   // korrekt instanziiert

  val player = Player(
    name = "Ash",
    team = Vector(
      Pokemon("Glurak", Fire, 150, 120, Vector()),
      Pokemon("Bisaflor", Grass, 160, 0, Vector())
    )
  )

  val saveFile = new File("save_Ash.xml")

  "XmlFileIO" should {

    "save a player to XML and create a matching file" in {
      // Alte Datei entfernen, falls vorhanden
      if saveFile.exists() then saveFile.delete()

      fileio.save(player)
      saveFile.exists() shouldBe true

      // Aufräumen
      saveFile.delete()
    }

    "load a saved player correctly from XML" in {
      // Testdatei erstellen
      if saveFile.exists() then saveFile.delete()
      fileio.save(player)

      val loaded = fileio.load("Ash")
      loaded.name shouldBe "Ash"
      loaded.team.size shouldBe 2
      loaded.team(0).name shouldBe "Glurak"
      loaded.team(0).currentHp shouldBe 120
      loaded.team(1).isFainted shouldBe true

      // Aufräumen
      saveFile.delete()
    }

    "list existing savegames" in {
      if saveFile.exists() then saveFile.delete()
      fileio.save(player)

      val list = fileio.listSaveGames()
      list should contain("Ash")

      // Aufräumen
      saveFile.delete()
    }
  }
