package de.htwg.se

import com.google.inject.AbstractModule
import net.codingwell.scalaguice.ScalaModule

import de.htwg.se.controller.IController
import de.htwg.se.controller.ControllerMockImpl.MockController

import de.htwg.se.model.FileIOComponent.IFileIO
import de.htwg.se.model.FileIOComponent.MockFileIOImpl.MockFileIO

import de.htwg.se.model.BattleLogicComponent.IBattleLogic
import de.htwg.se.model.BattleLogicComponent.MockBattleLogicImpl.MockBattleLogic

class PokemonMockModule extends AbstractModule with ScalaModule {
  override def configure(): Unit = {
    bind[IController].to[MockController]
    bind[IFileIO].to[MockFileIO]
    bind[IBattleLogic].to[MockBattleLogic]
  }
}