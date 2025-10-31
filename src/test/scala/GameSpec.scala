import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers
import pokemon.Game

class GameSpec extends AnyWordSpec with Matchers {

  "Game.createField" should {
    "create a 2D array with the correct dimensions" in {
      val field = Game.createField(3, 2)
      field.length shouldBe 2
      field.head.length shouldBe 3
    }

    "fill all cells with the given value" in {
      val field = Game.createField(2, 2, "X")
      all(field.flatten) shouldBe "X"
    }

    "use '.' as the default fill value" in {
      val field = Game.createField(2, 2)
      all(field.flatten) shouldBe "."
    }
  }

  "Game.setCell" should {
    "change only the selected cell" in {
      val field = Game.createField(3, 3, ".")
      Game.setCell(field, 1, 1, "P")
      field(1)(1) shouldBe "P"
      field(0)(0) shouldBe "."
    }
  }
}
