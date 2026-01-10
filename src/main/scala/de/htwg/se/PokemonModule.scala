package de.htwg.se

import com.google.inject.{AbstractModule, Provides}
import net.codingwell.scalaguice.ScalaModule
import de.htwg.se.controller.IController
import de.htwg.se.controller.controllerImpl.Controller
import de.htwg.se.model.FileIOComponent.{IFileIO, XmlFileIO, JsonFileIO}
import de.htwg.se.model.PokemonComponent.PokemonBaseImpl.PokemonFactory 

class PokemonModule extends AbstractModule with ScalaModule {

  override def configure(): Unit = {
    
    bind[IFileIO].to[XmlFileIO]
    // bind[IFileIO].to[JsonFileIO]
  }

  @Provides
  def provideController(fileIo: IFileIO): IController = {
    
    val initialPlayer = PokemonFactory.createPlayer("Gast", Vector("Glurak", "Bisaflor"))
    val initialEnemy = PokemonFactory.createRandomEnemy()

    new Controller(initialPlayer, initialEnemy, fileIo)
  }
}