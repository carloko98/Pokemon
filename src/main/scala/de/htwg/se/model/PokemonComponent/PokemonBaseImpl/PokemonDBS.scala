package de.htwg.se.model.PokemonComponent.PokemonBaseImpl

import play.api.libs.json.*
import scala.io.Source
import de.htwg.se.model.PokemonComponent.{PokemonType, Attack}
import scala.util.{Try, Random}

object PokemonDBS {

  private val allMoves: Map[String, Attack] = loadMoves()
  private val entries: Map[String, Pokemon] = loadPokedex()

  private def loadMoves(): Map[String, Attack] = {
      try {
        val source = Source.fromResource("moves.json")
        val json = Json.parse(source.getLines().mkString)
        json.as[List[JsValue]].map { m =>
          val name = (m \ "name").as[String]
          val damage = (m \ "damage").as[Int]
          val typeStr = (m \ "type").as[String]
          val atkType = Try(PokemonType.valueOf(typeStr)).getOrElse(PokemonType.Normal)
          name.toLowerCase -> Attack(name, damage, atkType)
        }.toMap
      } catch {
        case e: Exception =>
          println(s"Error loading moves: ${e.getMessage}")
          Map.empty
      }
  }

  private def loadPokedex(): Map[String, Pokemon] = {
    try {
      val source = Source.fromResource("pokedex.json")
      val json = Json.parse(source.getLines().mkString)
      
      (json \ "pokemon").as[List[JsValue]].map { p =>
        
        val name = (p \ "name").as[String]
        val id = (p \ "id").as[Int] //
        
        
        val imgUrl = s"https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/$id.png"

        val typeList = (p \ "type").as[List[String]] //
        
        val primStr = typeList.headOption.getOrElse("Normal")
        val primaryType = Try(PokemonType.valueOf(primStr)).getOrElse(PokemonType.Normal)
        
        val secondaryType: Option[PokemonType] = if (typeList.length > 1) {
             Try(PokemonType.valueOf(typeList(1))).toOption
        } else {
             None
        }
        
        val weightStr = (p \ "weight").asOpt[String].getOrElse("10.0 kg").split(" ")(0)
        val hpBase = 100 + Try(weightStr.toDouble / 2).getOrElse(10.0).toInt

        val tackle = allMoves.getOrElse("tackle", Attack("Tackle", 10, PokemonType.Normal))
        val typeMoves = getRandomMovesByType(primaryType, 3)
        val fillMoves = if (typeMoves.size < 3) getRandomMovesByType(PokemonType.Normal, 3 - typeMoves.size) else Vector.empty
        val pokemonMoves = (Vector(tackle) ++ typeMoves ++ fillMoves).take(4)

        name.toLowerCase -> Pokemon(
          name = name,
          id = id,                  
          pType = primaryType,
          secondaryType = secondaryType, 
          maxHp = hpBase,
          currentHp = hpBase,
          attacks = pokemonMoves,
          spriteUrl = imgUrl     
        )
      }.toMap
    } catch {
      case e: Exception =>
        println(s"CRITICAL: Fehler beim Laden der pokedex.json: ${e.getMessage}")
        Map.empty
    }
  }

  // ... (Hilfsmethoden getRandomMoves etc. bleiben gleich) ...
  private def getRandomMovesByType(pType: PokemonType, count: Int): Vector[Attack] = {
    val matching = allMoves.values.filter(_.attackType == pType).toVector
    if (matching.isEmpty) Vector.empty
    else Random.shuffle(matching).take(count)
  }
  
  def get(name: String): Option[Pokemon] = entries.get(name.toLowerCase)

  def getRandom: Pokemon = {
    if (entries.isEmpty) Pokemon("MissingNo", 0, PokemonType.Normal, None, 100, 100, Vector.empty, "")
    else {
      val keys = entries.keys.toVector
      entries(keys(Random.nextInt(keys.size))).copy()
    }
  }
}