package de.htwg.se.model

enum PlayerType{
    case enemy, player
}

case class Player(
    name: PlayerType,

)