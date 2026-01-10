package de.htwg.se.model.FileIOComponent

import de.htwg.se.model.PokemonComponent.PokemonBaseImpl.PokemonFactory // Korrigierter Import
import de.htwg.se.model.PlayerComponent.IPlayer
import de.htwg.se.model.PlayerComponent.PlayerBaseImpl.Player
import java.io._
import scala.xml.{Node, Elem, XML}
import scala.util.{Try, Success, Failure} 

class XmlFileIO extends IFileIO {

  override def save(player: IPlayer): Try[Unit] = {
    Try { 
      val xml = playerToXml(player)
      val file = new File(s"save_${player.name}.xml")
      val pw = new PrintWriter(file)
      pw.write(xml.toString())
      pw.close()
    }
  }

  override def load(name: String): Try[IPlayer] = {
    Try {
      val file = XML.loadFile(s"save_${name}.xml")
      playerFromXml(file)
    }
  }

  override def listSaveGames(): List[String] = {
    val dir = new File(".")
    Option(dir.listFiles) 
      .map(_.filter(f => f.isFile && f.getName.startsWith("save_") && f.getName.endsWith(".xml"))
      .map(f => f.getName.stripPrefix("save_").stripSuffix(".xml"))
      .toList)
      .getOrElse(List.empty)
  }

  private def playerToXml(player: IPlayer): Elem = {
    <player>
      <name>{player.name}</name>
      <team>
        {player.team.map(poke => 
          <pokemon>
            <name>{poke.name}</name>
            <hp>{poke.currentHp}</hp>
          </pokemon>
        )}
      </team>
    </player>
  }

  private def playerFromXml(node: Node): IPlayer = {
    val name = (node \ "name").text.trim
    val teamNodes = (node \ "team" \ "pokemon")
    val team = teamNodes.map { pNode =>
      val pokeName = (pNode \ "name").text.trim
      val hp = (pNode \ "hp").text.trim.toInt
      
      val freshPoke = PokemonFactory.getPokemon(pokeName)
      freshPoke.withHp(hp)
    }.toVector
    
    Player(name, team)
  }
}