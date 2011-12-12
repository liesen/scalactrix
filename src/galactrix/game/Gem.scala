package galactrix.game

import java.awt.Color

object GemColor extends Enumeration {
  type GemColor = Value
  
  val Blue, Green, Purple, Red, Silver, Yellow, Mine = Value
}

abstract case class Gem(val gemColor: GemColor.Value) {
  def color: Color
}

object Gem {
  case object Blue extends Gem(GemColor.Blue) { val color = Color.blue.darker }
  case object Green extends Gem(GemColor.Green) { val color = Color.green.darker }
  case object Purple extends Gem(GemColor.Purple) { val color = new Color(128, 0, 128) }
  case object Red extends Gem(GemColor.Red) { val color = Color.red.darker }
  case object Silver extends Gem(GemColor.Silver) { val color = Color.white.darker }
  case object Yellow extends Gem(GemColor.Yellow) { val color = Color.yellow.darker }
  case class Mine(val strength: Int) extends Gem(GemColor.Mine) { val color = Color.black }
  case object DefaultMine extends Mine(10)
  
  val asArray = Array(Blue, Green, Purple, Red, Silver, Yellow, DefaultMine)
  
  def iterator = asArray.iterator
  
  def fromColor(color: Color) = asArray find { _.color == color }
}