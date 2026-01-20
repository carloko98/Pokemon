package de.htwg.se.model.PokemonComponent
   

trait IPokemon {
  def name: String
  def pType: PokemonType
  def secondaryType: Option[PokemonType]
  def maxHp: Int
  def currentHp: Int
  def attacks: Vector[Attack] 
  def id: Int
  def spriteUrl: String
  

  def isFainted: Boolean
  
  def withHp(newHp: Int): IPokemon
}