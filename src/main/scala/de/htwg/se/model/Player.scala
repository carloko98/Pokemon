package de.htwg.se.model

case class Player(
    name: String,
    team: Vector[PokemonInterface] = Vector.empty,
    currentPokemonIndex: Int = 0,
    items: Vector[String] = Vector.empty // Erstmal String spaeter erweitern
) extends PlayerInterface{
    def activePokemon: PokemonInterface = team(currentPokemonIndex)

    def updatePokemon(newPokemon: PokemonInterface): Player = {
        val newTeam = team.updated(currentPokemonIndex, newPokemon)
        copy(team = newTeam)
    }

    def switchActivePokemon(index: Int): Player = {
        copy(currentPokemonIndex = index)
    }

    def addPokemon(p: PokemonInterface): Player = {
        val newTeam = team :+ p
        copy(team = newTeam)
    }

    //Abfragen

    def isActiveFainted: Boolean = activePokemon.isFainted

    def isDefeated: Boolean = team.forall(_.isFainted)

    def nextAlivePokemonIndex: Option[Int] = {
        val index = team.indexWhere(!_.isFainted)
        if (index >= 0) Some(index) else None
    }

    override def toString: String = name
}
