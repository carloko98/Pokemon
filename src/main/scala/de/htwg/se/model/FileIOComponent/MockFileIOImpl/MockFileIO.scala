package de.htwg.se.model.FileIOComponent.MockFileIOImpl 

import de.htwg.se.model.FileIOComponent.IFileIO 

import de.htwg.se.model.PlayerComponent.IPlayer
import de.htwg.se.model.PlayerComponent.PlayerService
import de.htwg.se.model.PokemonComponent.PokemonService
import scala.util.{Try, Success, Failure}

class MockFileIO extends IFileIO {
  override def save(player: IPlayer): Try[Unit] = Success(())

  override def load(name: String): Try[IPlayer] = {
    // Dummy return
    val dummyPoke = PokemonService.getPokemon("Glurak")
    val dummyPlayer = PlayerService.buildPlayer(name, Vector(dummyPoke))
    Success(dummyPlayer)
  }

  override def listSaveGames(): List[String] = List("TestSave1", "TestSave2")
}