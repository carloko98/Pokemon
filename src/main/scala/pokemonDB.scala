

package de.htwg

case class Pokemon(
    name: String,
    level: Int,
    pType: PokemonType,
    maxHp: Int,
    currentHp: Int,
    attacks: List[Attack]

)