package de.htwg.se.model.FileIOComponent

import de.htwg.se.model.PlayerComponent.IPlayer
import scala.util.Try

trait IFileIO {
  def save(player: IPlayer): Try[Unit]
  def load(name: String): Try[IPlayer]
  def listSaveGames(): List[String]
}