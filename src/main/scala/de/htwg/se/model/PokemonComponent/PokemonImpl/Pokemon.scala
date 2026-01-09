package de.htwg.se.model.PokemonComponent.PokemonImpl

import de.htwg.se.model.PokemonComponent.IntPokemon
import de.htwg.se.model.PokemonComponent.IntPokemonType

case class Pokemon(
    name: String,
    pType: IntPokemonType,
    maxHp: Int,
    currentHp: Int,
    attacks: Vector[Attack]
) extends IntPokemon {

  override def isFainted: Boolean = currentHp <= 0

  override def withHp(newHp: Int): IntPokemon = {
    val clamped = newHp.max(0).min(maxHp)
    copy(currentHp = clamped)
  }

  // Optional: hilfreiche toString nur für Debugging
  override def toString: String = s"$name (HP: $currentHp/$maxHp, Typ: $pType)"
}