package pokemon

object Game {
  def createField(width: Int, height: Int, fill: String = "."): Array[Array[String]] =
    Array.fill(height, width)(fill)

  def setCell(field: Array[Array[String]], x: Int, y: Int, v: String): Unit =
    field(y)(x) = v
}

