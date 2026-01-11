package de.htwg.se.model.PokemonComponent.MockPokemonImpl

import de.htwg.se.model.PokemonComponent.{IPokemon, PokemonType, Attack}

case class MockPokemon(
    name: String = "MockPokemon",
    pType: PokemonType = PokemonType.Normal,
    maxHp: Int = 100,
    currentHp: Int = 100,
    attacks: Vector[Attack] = Vector.empty
) extends IPokemon {
  
  override def isFainted: Boolean = currentHp <= 0
  
  override def withHp(newHp: Int): IPokemon = copy(currentHp = newHp)
  
  override def toString: String = name

  // Dummies für Interface-Methoden, falls IPokemon mehr fordert:
  def attack: Int = 10
  def defense: Int = 10
  def speed: Int = 10
  def xp: Int = 0
  def level: Int = 5
}