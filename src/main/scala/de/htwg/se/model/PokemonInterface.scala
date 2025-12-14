package de.htwg.se.model

import de.htwg.se.model.PokemonType 
import de.htwg.se.model.Attack      

trait PokemonInterface {
  def name: String
  def pType: PokemonType
  def maxHp: Int
  def currentHp: Int
  def attacks: Vector[Attack] 
  

  def isFainted: Boolean
  
  def withHp(newHp: Int): PokemonInterface
}