package galactrix.util

import java.awt.Color

trait ColorDistanceOrdering extends Ordering[Color] {
  /** Reference color */
  def reference: Color
  
  def distance(color: Color) = Colors.distance(reference, color)
  
  def compare(x: Color, y: Color) = distance(x) compare distance(y)
}

class ColorDistance(val reference: Color) extends ColorDistanceOrdering