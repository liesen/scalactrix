package galactrix.geom.util

import galactrix.geom.Hexagon

import java.awt.geom.Point2D

/**
 * Utility functions for dealing with hexagonal shapes.
 */
object Hexagons {
  def above(h: Hexagon, position: Point2D.Double): Point2D.Double =
    new Point2D.Double(position.x, position.y - 2 * h.inradius)
  
  def aboveLeft(h: Hexagon, position: Point2D.Double): Point2D.Double =
    new Point2D.Double(position.x - h.circumradius - h.circumradius / 2, position.y - h.inradius)

  def aboveRight(h: Hexagon, position: Point2D.Double): Point2D.Double =
    new Point2D.Double(position.x + h.circumradius + h.circumradius / 2, position.y - h.inradius)
  
  def below(h: Hexagon, position: Point2D.Double): Point2D.Double =
    new Point2D.Double(position.x, position.y + 2 * h.inradius)
  
  def belowRight(h: Hexagon, position: Point2D.Double): Point2D.Double =
    new Point2D.Double(position.x + h.circumradius + h.circumradius / 2, position.y + h.inradius)

  def belowLeft(h: Hexagon, position: Point2D.Double): Point2D.Double =
    new Point2D.Double(position.x - h.circumradius - h.circumradius / 2, position.y + h.inradius)

  def neighbors(h: Hexagon, position: Point2D.Double): Array[Point2D.Double] =
    Array(
      aboveLeft(h, position),
      above(h, position), 
      aboveRight(h, position),
      belowRight(h, position),
      below(h, position),
      belowLeft(h, position)
    )
}