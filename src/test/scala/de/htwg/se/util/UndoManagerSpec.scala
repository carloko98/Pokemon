package de.htwg.se.util

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers

class UndoManagerSpec extends AnyWordSpec with Matchers {
  "An UndoManager" should {
    val undoManager = new UndoManager

    // Ein Dummy Command zum Testen
    var testValue = 0
    class IncrCommand extends Command {
      override def doStep(): Unit = testValue += 1
      override def undoStep(): Unit = testValue -= 1
      override def redoStep(): Unit = testValue += 1
    }

    "execute commands correctly" in {
      val cmd = new IncrCommand
      undoManager.doStep(cmd)
      testValue should be(1)
    }

    "undo commands correctly" in {
      undoManager.undoStep()
      testValue should be(0)
    }

    "redo commands correctly" in {
      undoManager.redoStep()
      testValue should be(1)
    }
    
    "handle empty stacks gracefully" in {
       // Wenn Stack leer ist, darf nichts passieren (kein Crash)
       val emptyManager = new UndoManager
       noException should be thrownBy emptyManager.undoStep()
       noException should be thrownBy emptyManager.redoStep()
    }
  }
}