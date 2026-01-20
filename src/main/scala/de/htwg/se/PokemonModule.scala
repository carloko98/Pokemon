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
    
    // ÄNDERUNG: Zufälliges Starter-Team für den Spieler!
    val initialPlayer = PokemonFactory.createRandomPlayer("Ash")
    
    // Start-Gegner (Standardmäßig wild oder Trainer, hier nehmen wir mal Wild)
    val initialEnemy = PokemonFactory.createWildEnemy()

    new Controller(initialPlayer, initialEnemy, fileIo)
  }
}