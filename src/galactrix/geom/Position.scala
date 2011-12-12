package galactrix.geom

case class Position(val x: Int, val y: Int, val z: Int) extends (Int, Int, Int)(x, y, z) with Ordered[Position] {
  def +(that: Position) = Position(x + that.x, y + that.y, z + that.z)
  
  def -(that: Position) = Position(x - that.x, y - that.y, z - that.z)
  
  def compare(that: Position) = Ordering.Tuple3[Int, Int, Int].compare((x, y, z), (that.x, that.y, that.z))
}