val width = 62 

  def padRight(text: String, total: Int): String =
    text + " " * (total - text.length).max(0)

def line(content: String): String =
    "| " + padRight(content, width - 4) + " |"

line("..")

padRight("-", 1)