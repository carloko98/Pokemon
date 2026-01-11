package de.htwg.se.model.PokemonComponent
   

trait IPokemon {
  def name: String
  def pType: PokemonType
  def maxHp: Int
  def currentHp: Int
  def attacks: Vector[Attack] 
  

  def isFainted: Boolean
  
  def withHp(newHp: Int): IPokemon
}