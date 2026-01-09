package de.htwg.se.model.fileio

import de.htwg.se.model.{Player, PokemonFactory, PlayerInterface}
import java.io._
import scala.xml.{Node, Elem, XML}
import scala.util.{Try, Success, Failure} 

trait FileIOInterface {
  def save(player: PlayerInterface): Try[Unit]
  def load(name: String): Try[PlayerInterface]
  def listSaveGames(): List[String]
}

class XmlFileIO extends FileIOInterface {

  override def save(player: PlayerInterface): Try[Unit] = {
    Try { 
      val xml = playerToXml(player)
      val file = new File(s"save_${player.name}.xml")
      val pw = new PrintWriter(file)
      pw.write(xml.toString())
      pw.close()
    }
  }

  override def load(name: String): Try[PlayerInterface] = {
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

  private def playerToXml(player: PlayerInterface): Elem = {
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

  private def playerFromXml(node: Node): PlayerInterface = {
    val name = (node \ "name").text.trim
    val teamNodes = (node \ "team" \ "pokemon")
    val team = teamNodes.map { pNode =>
      val pokeName = (pNode \ "name").text.trim
      val hp = (pNode \ "hp").text.trim.toInt
      // Factory gibt Interface zurück, das ist ok
      val freshPoke = PokemonFactory.getPokemon(pokeName)
      freshPoke.withHp(hp)
    }.toVector
    
    // Da wir im Model-Paket (bzw. Sub-Paket) sind, dürfen wir die konkrete Klasse Player zur Instanziierung nutzen
    Player(name, team)
  }
}