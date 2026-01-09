package de.htwg.se.model.PokemonComponent.PokemonImpl

import de.htwg.se.model.PokemonComponent.IntAttack
import de.htwg.se.model.PokemonComponent.IntPokemonType


case class Attack(
    name: String,
    damage: Int,
    attackType: IntPokemonType
) extends IntAttack