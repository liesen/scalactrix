package galactrix.geom

import scala.collection.MapProxy
import galactrix.geom._

import scala.collection.SortedMap
import java.awt.geom.Point2D

class HexagonGrid[Cell](val hexagon: Hexagon, val self: Map[Position, Cell]) extends MapProxy[Position, Cell] {
  import HexagonGrid._

  def positionOf(p: Position, origin: Point2D): Point2D.Double = HexagonGrid.positionOf(p, hexagon, origin)

  val aboveLeft = Position(0, -1, 1)
  val above = Position(-1, 1, 0)
  val aboveRight = Position(-1, 0, 1)
  val belowRight = Position(0, -1, 1)
  val below = Position(1, -1, 0)
  val belowLeft = Position(1, 0, -1)

  val deltas = List(aboveLeft, above, aboveRight, belowRight, below, belowLeft)

  def neighbors(p: Position) = HexagonGrid[Cell](hexagon, deltas map { _ + p } flatMap { q => this get q map { h => (q, h) } }: _*)
  
  def swap(source: Position, destination: Position): HexagonGrid[Cell] = {
    val sourceCell = this(source)
    val destinationCell = this(destination)    
    new HexagonGrid(hexagon, this -- List(source, destination) ++ List(source -> destinationCell, destination -> sourceCell) toMap)
  }
}

object HexagonGrid {
  val SQRT_3 = math.sqrt(3)

  def apply[Cell](hexagon: Hexagon, cells: (Position, Cell)*): HexagonGrid[Cell] = 
    new HexagonGrid(hexagon, SortedMap.empty[Position, Cell] ++ cells)

  def positionOf(p: Position, hexagon: Hexagon, origin: Point2D): Point2D.Double =
    new Point2D.Double(origin.getX + SQRT_3 * hexagon.inradius * p._1,
      origin.getY + 2 * hexagon.inradius * (p._1 / 2 + p._3) + ((p._1 % 2) * hexagon.inradius))
}