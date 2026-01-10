package de.htwg.se.model.PokemonComponent

import de.htwg.se.model.PokemonType 
import de.htwg.se.model.Attack      

trait IPokemon {
  def name: String
  def pType: PokemonType
  def maxHp: Int
  def currentHp: Int
  def attacks: Vector[Attack] 
  

  def isFainted: Boolean
  
  def withHp(newHp: Int): IPokemon
}