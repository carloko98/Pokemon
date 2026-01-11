package de.htwg.se.model.FileIOComponent

import de.htwg.se.model.PlayerComponent.{IPlayer, PlayerService}
import de.htwg.se.model.PokemonComponent.PokemonService

import play.api.libs.json._
import scala.util.Try
import scala.io.Source
import java.io._

class JsonFileIO extends IFileIO {

  override def save(player: IPlayer): Try[Unit] = {
    Try {
      val json = Json.obj(
        "name" -> player.name,
        "team" -> Json.toJson(
          player.team.map { p =>
            Json.obj(
              "name" -> p.name,
              "hp" -> p.currentHp
            )
          }
        )
      )
      val pw = new PrintWriter(new File(s"save_${player.name}.json"))
      pw.write(Json.prettyPrint(json))
      pw.close()
    }
  }

  override def load(name: String): Try[IPlayer] = {
    Try {
      val source: String = Source.fromFile(s"save_$name.json").getLines.mkString
      val json: JsValue = Json.parse(source)
      val playerName = (json \ "name").as[String]
      
      val team = (json \ "team").as[JsArray].value.map { obj =>
        val pokeName = (obj \ "name").as[String]
        val hp = (obj \ "hp").as[Int]
        PokemonService.getPokemon(pokeName).withHp(hp)
      }.toVector
      
      PlayerService.buildPlayer(playerName, team)
    }
  }

  override def listSaveGames(): List[String] = {
    val dir = new File(".")
    Option(dir.listFiles)
      .map(_.filter(f => f.isFile && f.getName.startsWith("save_") && f.getName.endsWith(".json"))
      .map(f => f.getName.stripPrefix("save_").stripSuffix(".json"))
      .toList).getOrElse(List.empty)
  }
}