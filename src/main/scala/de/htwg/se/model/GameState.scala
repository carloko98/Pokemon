package de.htwg.se.model



case class GameState(
    player: Pokemon,
    enemy: Pokemon,
    battleOver: Boolean = false,
    msg1: String = "",
    msg2: String = ""
)

// copy konstruktor fur game model
//contoller darf nur noch ein var haben der den game state enthalt
//der prof will dass es nur einen var gibt der den ganzen gamestate enthält, und alle änderungen sollen ein neues objekt mit copy machen und es sollen dir nur die dinge neu gemacht werden die geändert worden sind.
// also gamestate soll einen copy konstruktor enthalten. es soll außerdem etwas wie player geben im model 
