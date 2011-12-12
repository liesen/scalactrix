package galactrix.ui

import scala.collection.mutable.ListBuffer
import galactrix.geom.{ Hexagon, HexagonGrid, Position, Rectangle }
import galactrix.geom.util.Hexagons
import galactrix.game.{ Gem, Tile }
import galactrix.util.{ Colors, ColorDistance }
import galactrix.robot.{ Moves, Move, Swap }

import scala.swing._
import scala.swing.event._

import java.awt.{ Color, Point }
import java.awt.geom.Point2D
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO

case class ImageEvent(val image: BufferedImage) extends Event
case class BoardEvent(val tiles: HexagonGrid[Tile]) extends Event
case class MoveEvent(val robot: java.awt.Robot, val image: BufferedImage) extends Event

class Bot(val hexagon: Hexagon, val offset: Point, val origin: Point2D.Double) extends Publisher with Reactor {
  reactions += { case ImageEvent(image) =>
    println("New image");
    val grid = parseImage(image)
    val bestMove = Moves.bestMoves(grid).reverse.head
    println(bestMove)
    publish(BoardEvent(grid))
  }
  
  reactions += { case MoveEvent(robot, image) => 
    println("Move")
    val grid = parseImage(image);
    publish(BoardEvent(grid)) // Might aswell
    val bestMoves = Moves.bestMoves(grid).reverse
    bestMoves take 3 foreach println
    val bestMove = bestMoves.head
    execute(bestMove)(robot) 
  }

  val positions = {
    val deltas = List(Position(1, 0, -1), Position(0, 1, -1), Position(-1, 1, 0), Position(-1, 0, 1), Position(0, -1, 1), Position(1, -1, 0))
    val positions = new ListBuffer[Position]

    for { r <- 0 until 5 } {
      var p = Position(0, -r, r)
      positions += p

      for (j <- 0 until 6) {
        for (i <- 0 until (if (j == 5) r - 1 else r)) {
          p += deltas(j)
          positions += p
        }
      }
    }
    
    val corners = List(Position(0, 4, -4), Position(-4, 4, 0), Position(-4, 0, 4), Position(0, -4, 4), Position(4, -4, 0), Position(4, 0, -4))
    positions -- corners
  }

  def parseImage(image: BufferedImage) = {
    val ts = positions map { c => 
      val p = positionOf(c)
      val gem = gemForPosition(p, image)
      (c, Tile(c, gem))
    }
    
    HexagonGrid[Tile](hexagon, ts: _*)
  }
  
  def gemForPosition(p: Point2D.Double, image: BufferedImage) = {
    val colorRect = Rectangle((-hexagon.inradius / 2).toInt, (-hexagon.inradius / 2).toInt, hexagon.inradius.toInt, hexagon.inradius.toInt) + (p.getX.toInt, p.getY.toInt)
    val avgColor = Colors.averageColor(image, colorRect)
    gemForColor(avgColor)
  }

  def gemForColor(c: Color): Gem = {
    implicit val colorOrdering: Ordering[Color] = new ColorDistance(c)
    Gem.asArray.toList sortBy { _.color } head
  }

  def execute(move: Move)(implicit robot: java.awt.Robot) {
    def click(x: Int, y: Int) {
      robot.mouseMove(x, y)
      robot.mousePress(java.awt.event.InputEvent.BUTTON1_MASK)
      robot.mouseRelease(java.awt.event.InputEvent.BUTTON1_MASK)
    }

    val o = positionOf(Position(0, 0, 0))
    click(o.x.toInt + offset.x, o.y.toInt + offset.y) // focus

    List(move.swap.source, move.swap.target) foreach { tile =>
      val position = positionOf(tile.position)
      click(position.x.toInt + offset.x, position.y.toInt + offset.y)
    }
  }

  def positionOf(coordinate: Position) = HexagonGrid.positionOf(coordinate, hexagon, origin)
}

object Bot extends SimpleSwingApplication with Publisher {
  val hexagon = new Hexagon(19.5, 22.5)

  val screenSize = new Point(1280, 778) // 1224x778

  val size = new Point(826, 465)

  val offset = new Point((screenSize.x - size.x) / 2, 0) //(screenSize.y - size.y) / 2)

  val origin = new Point2D.Double(413, 245) // 613, 317)

  val robot = new java.awt.Robot { setAutoDelay(100) }

  val image = ImageIO.read(new File("resources/galactrix1.png"))
  val bot = new Bot(hexagon, offset, origin)

  val updateButton = Button("Update") {
    val region = new java.awt.Rectangle(offset.x, offset.y, size.x, size.y)
    val image = robot.createScreenCapture(region)
    publish(ImageEvent(image))
  }

  val moveButton = Button("Move") { 
    val region = new java.awt.Rectangle(offset.x, offset.y, size.x, size.y)
    val image = robot.createScreenCapture(region)
    publish(MoveEvent(robot, image))
  }
  
  bot.listenTo(this, updateButton, moveButton)

  val view = new BoardView(hexagon) { listenTo(bot) }
  val gview = new GalactrixView(image) { listenTo(this, bot, updateButton) }

  def top = new TranslucentMainFrame(.5f) {
    val buttonPanel = new BoxPanel(Orientation.Horizontal) {
      contents ++= Seq(updateButton, moveButton)
    }

    preferredSize = size

    contents = new BoxPanel(Orientation.Vertical) {
      contents ++= Seq(buttonPanel, view, gview)
    }
  }
}
