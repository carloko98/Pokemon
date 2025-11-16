package de.htwg.se.model

// Angriff: Name, Schaden, Typ
case class Attack(
    name: String,        // z. B. "Tackle"
    damage: Int,         // Basis-Schaden
    attackType: PokemonType  // z. B. Normal, Water
)