package de.htwg.se.model.fileio

import de.htwg.se.model.{Player, Pokemon, PokemonFactory}
import java.io._
import scala.xml.{Node, Elem, XML}

trait FileIO{
    def save(player: Player): Unit
    def load(name: String): Player
    def listSaveGames(): List[String]
}

class XmlFileIO extends FileIO{
    override def save(player: Player): Unit = {
    try {
      val xml = playerToXml(player)
      val file = new File(s"save_${player.name}.xml")
      val pw = new PrintWriter(file)
      pw.write(xml.toString())
      pw.close()
      println(s"Datei geschrieben nach: ${file.getAbsolutePath}") // Debug-Ausgabe!
    } catch {
      case e: Exception => println(s"Fehler beim Speichern: ${e.getMessage}")
    }
  }

    override def load(name: String): Player = {
        val file = XML.loadFile(s"save_${name}.xml")
        playerFromXml(file)
    }

    override def listSaveGames(): List[String] = {
        val dir = new File(".")
        dir.listFiles
            .filter(f => f.isFile() && f.getName.startsWith("save_") && f.getName.endsWith(".xml"))
            .map(f => f.getName.stripPrefix("save_").stripSuffix(".xml"))
            .toList
    }
}

private def playerToXml(player: Player): Elem = {
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

private def playerFromXml(node: Node): Player = {
    val name = (node \ "name").text.trim
    
    val teamNodes = (node \ "team" \ "pokemon")
    val team = teamNodes.map { pNode =>
      val pokeName = (pNode \ "name").text.trim
      val hp = (pNode \ "hp").text.trim.toInt
      
      // Wir nutzen die Factory für die Stats, setzen aber die gespeicherten HP
      val freshPoke = PokemonFactory.getPokemon(pokeName)
      freshPoke.withHp(hp)
    }.toVector
    
    Player(name, team)
}