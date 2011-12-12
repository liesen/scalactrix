package galactrix.geom

import java.awt.Polygon

class Hexagon(val inradius: Double, val circumradius: Double) extends Hexagonal {
  def hexagon = this
  
  def polygon = 
    new Polygon(Array((inradius / 2).toInt, circumradius.toInt, (inradius / 2).toInt, (-inradius / 2).toInt, -circumradius.toInt, (-inradius / 2).toInt),
                Array(-inradius.toInt, 0, inradius.toInt, inradius.toInt, 0, -inradius.toInt),
                6);
  
  def bounds = 
    new java.awt.Rectangle(-circumradius.toInt, -inradius.toInt, (2 * circumradius + .5).toInt, (2 * inradius + .5).toInt);
}

object Hexagon {
  private val SQRT_3 = math.sqrt(3)
  
  def withCircumradius(circumradius: Double): Hexagon = new Hexagon((SQRT_3 * circumradius) / 2, circumradius)
  
  def withInradius(inradius: Double): Hexagon = new Hexagon(inradius, (inradius * 2) / SQRT_3)
}