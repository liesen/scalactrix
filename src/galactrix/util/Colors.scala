package galactrix.util

import galactrix.geom.Rectangle
import java.awt.Color
import java.awt.image.BufferedImage

object Colors {
  def averageColor(im: BufferedImage, rect: Rectangle): Color =
    averageColor(im.getSubimage(rect.x, rect.y, rect.width, rect.height), rect.width, rect.height)
  
  def averageColor(im: BufferedImage, width: Int, height: Int): Color = {
    val pixels = Array.ofDim[Int](width * height)
    im.getRGB(0, 0, width, height, pixels, 0, width)
    
    ((0, 0, 0) /: pixels) { case ((red, green, blue), pixel) =>
      val pixelColor = new Color(pixel)
      (red + pixelColor.getRed, green + pixelColor.getGreen, blue + pixelColor.getBlue)
    } match {
      case (red, green, blue) =>
        new Color(red / pixels.length, green / pixels.length, blue / pixels.length)
    }
  }

  // Euclidean distance between two 3-dimensional points: (red, green, blue)
  def distance(color1: Color, color2: Color): Double = 
    math.sqrt(
        math.abs(color2.getRed * color2.getRed - color1.getRed * color1.getRed) +
        math.abs(color2.getGreen * color2.getGreen - color1.getGreen * color1.getGreen) +
        math.abs(color2.getBlue * color2.getBlue - color1.getBlue * color1.getBlue))
}