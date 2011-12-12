package galactrix.ui

import galactrix.geom.HexagonGrid
import scala.collection.MapLike
import galactrix.geom.util.Hexagons
import java.awt.Rectangle
import java.awt.FontMetrics
import java.awt.Point
import java.awt.geom.Point2D
import galactrix.geom.Position
import galactrix.robot.{Moves, Move, Swap}
import java.awt.Graphics2D
import scala.swing.Panel
import java.awt.Dimension
import scala.swing.MainFrame
import java.awt.BasicStroke
import java.util.concurrent.FutureTask
import java.util.concurrent.Executors
import galactrix.game.Tile
import galactrix.geom.Hexagon
import java.awt.image.BufferedImage
import java.awt.Color

class BoardView(val hexagon: Hexagon) extends Panel {
  background = Color.darkGray
  
  size = new Dimension((2 * 7 * hexagon.circumradius).toInt,
                       (2 * 8 * hexagon.inradius).toInt)
  preferredSize = size

  val center = new Point2D.Double(size.width / 2, size.height / 2)
  
  val image = new BufferedImage(size.width, size.height, BufferedImage.TYPE_INT_ARGB)
  
  reactions += { case BoardEvent(tiles) => draw(tiles) }
  
  def draw(grid: HexagonGrid[Tile]) {
    implicit val g = image.createGraphics
    g.setColor(Color.DARK_GRAY)
    g.fillRect(0, 0, image.getWidth, image.getHeight)
    g.setBackground(background)
    g.setStroke(new BasicStroke(3f))
    g.translate(image.getWidth / 2, image.getHeight / 2)
    
    Moves.bestMoves(grid).toList sortBy { _.score } foreach {
      case Move(Swap(source, target), score) =>
        drawTile(source, Some(score.toString), grid)
        drawTile(target, Some(score.toString), grid)
    }
    
    g.dispose
    repaint
  }
  
  def drawTile(tile: Tile, label: Option[String], grid: HexagonGrid[Tile])(implicit g: Graphics2D) {
    val p = grid.positionOf(tile.position, new Point2D.Double(0, 0))
    val polygon = grid.hexagon.polygon
    polygon.translate((p.getX + .5).toInt, (p.getY + .5).toInt)
    
    g.setColor(tile.gem.color.darker.darker)
    g.fill(polygon)
    
    g.setColor(tile.gem.color)
    g.draw(polygon)
    
    // Draw tile info
    g.setColor(Color.white)
    
    label foreach { info =>    
      val bounds = hexagon.bounds
      bounds.translate((p.getX + .5).toInt, (p.getY + .5).toInt)
      val q = centerText(info, bounds, g.getFontMetrics);
      g.drawString(info, q.x, q.y);
    }
  }
  
  def centerText(text: String, rect: Rectangle, metrics: FontMetrics) =
    new Point(rect.x + (rect.width - metrics.stringWidth(text)) / 2,
              rect.y + (rect.height - metrics.getHeight()) / 2 + metrics.getAscent())
  
  override def paint(g: Graphics2D) {
    g.drawImage(image, null, 0, 0)
  }
}