package de.htwg.se.model

sealed trait Command

object Command {
  case class AttackCommand(attack: Attack) extends Command
  case object FleeCommand extends Command
}