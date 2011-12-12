package galactrix.ui

import galactrix.geom.HexagonGrid
import java.awt.Font
import galactrix.game.Tile
import java.awt.FontMetrics
import java.awt.Rectangle
import java.awt.Graphics2D
import java.awt.Point
import java.awt.Dimension
import java.awt.Color
import scala.swing.Panel
import java.awt.image.BufferedImage

class GalactrixView(var backgroundImage: BufferedImage) extends Panel {
  size = new Dimension(backgroundImage.getWidth, backgroundImage.getHeight)
  
  preferredSize = size
  
  val center = new Point(size.width / 2, size.height / 2)
  
  val foregroundImage = new BufferedImage(size.width, size.height, BufferedImage.TYPE_INT_ARGB)
  
  reactions += {
    case ImageEvent(image) => { println("X"); backgroundImage = image; repaint() }
    case BoardEvent(grid) => { 
      implicit val g = foregroundImage.createGraphics
      // g.clearRect(0, 0, foregroundImage.getWidth, foregroundImage.getHeight)
      g.setFont(new Font("Arial", 0, 10))
      
      grid.values foreach { tile => 
        val p = grid.positionOf(tile.position, center)
        val poly = grid.hexagon.polygon
        poly.translate((p.x + .5).toInt, (p.y + .5).toInt)
        g.setColor(Color.WHITE)
        g.draw(poly)
        
        drawTileText(tile, grid)
      }
      
      g.dispose
      repaint()
    }
  }
  
  def drawTileText(tile: Tile, grid: HexagonGrid[Tile])(implicit g: Graphics2D) {
    val text = "(%s;%s;%s)".format(tile.position.x, tile.position.y, tile.position.z)
    val p = grid.positionOf(tile.position, center)
    val bounds = grid.hexagon.bounds
    bounds.translate((p.x + .5).toInt, (p.y + .5).toInt)
    val c = centerText(text, bounds, g.getFontMetrics)
    g.drawString(text, c.x, c.y)
  }
  
  def centerText(text: String, rect: Rectangle, metrics: FontMetrics) =
    new Point(rect.x + (rect.width - metrics.stringWidth(text)) / 2,
              rect.y + (rect.height - metrics.getHeight()) / 2 + metrics.getAscent())
  
  override def paint(g: Graphics2D) {
    val x = (backgroundImage.getWidth - center.x) / 2
    val y = (backgroundImage.getHeight - center.y) / 2 
    
    g.drawImage(backgroundImage, null, -x, 0)
    g.drawImage(foregroundImage, null, -x, 13)
  }
}