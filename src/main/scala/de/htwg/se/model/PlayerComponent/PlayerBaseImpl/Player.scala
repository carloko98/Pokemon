package de.htwg.se.model.PlayerComponent.PlayerBaseImpl
import de.htwg.se.model.PlayerComponent.IPlayer
import de.htwg.se.model.PokemonComponent.IPokemon

case class Player(
    name: String,
    team: Vector[IPokemon] = Vector.empty,
    currentPokemonIndex: Int = 0,
    items: Vector[String] = Vector.empty // Erstmal String spaeter erweitern
) extends IPlayer{
    def activePokemon: IPokemon = team(currentPokemonIndex)

    def updatePokemon(newPokemon: IPokemon): Player = {
        val newTeam = team.updated(currentPokemonIndex, newPokemon)
        copy(team = newTeam)
    }

    def switchActivePokemon(index: Int): Player = {
        copy(currentPokemonIndex = index)
    }

    def addPokemon(p: IPokemon): Player = {
        val newTeam = team :+ p
        copy(team = newTeam)
    }

    

    def isActiveFainted: Boolean = activePokemon.isFainted

    def isDefeated: Boolean = team.forall(_.isFainted)

    def nextAlivePokemonIndex: Option[Int] = {
        val index = team.indexWhere(!_.isFainted)
        if (index >= 0) Some(index) else None
    }

    def withTeam(newTeam: Vector[IPokemon]): IPlayer = {
        copy(team = newTeam)
    }

    override def toString: String = name
}
