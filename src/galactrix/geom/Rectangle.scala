package galactrix.geom

case class Rectangle(val x: Int, val y: Int, val width: Int, val height: Int) {
  def +(delta: Position): Rectangle = this + (delta.x, delta.y)
  
  def +(dx: Int, dy: Int): Rectangle = Rectangle(x + dx, y + dy, width, height)
}