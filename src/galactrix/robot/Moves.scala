package galactrix.robot

import galactrix.geom.HexagonGrid
import galactrix.game.Gem
import galactrix.geom.Position
import galactrix.game.Tile

case class Swap(source: Tile, target: Tile)

case class Move(swap: Swap, score: Double)

object Moves {
  def bestMoves(grid: HexagonGrid[Tile]) = applyHeuristic(rank(grid)).toList sortBy { _.score } 
  
  def findBestMove(grid: HexagonGrid[Tile]): Move = bestMoves(grid).head
  
  def rank(tiles: HexagonGrid[Tile]): Iterable[Move] =
    for { tile <- tiles.values
          neighbor <- tiles.neighbors(tile.position).values
          move = score(Swap(tile, neighbor))(tiles) }
      yield move
  
  def applyHeuristic(moves: Iterable[Move]) = 
    moves map { case Move(swap @ Swap(Tile(_, gem), _), score) =>
      val multiplier = gem match {
        case Gem.Blue => 4
        case Gem.Green => 3
        case Gem.Purple => 2
        case Gem.Red => 5
        case Gem.Silver => 1
        case Gem.Yellow => 2
        case Gem.Mine(strength) => strength
      }
      
      Move(swap, math.pow(multiplier, score))
    }
  
  val aboveLeft = Position(0, -1, 1)
  val above = Position(-1, 1, 0)
  val aboveRight = Position(-1, 0, 1)
  val belowRight = Position(0, -1, 1)
  val below = Position(1, -1, 0)
  val belowLeft = Position(1, 0, -1)
  
  val deltas = List(aboveLeft, above, aboveRight, belowRight, below, belowLeft)
    
  def trySwap(swap: Swap)(implicit tiles: HexagonGrid[Tile]): (Swap, List[List[Tile]]) = swap match {
    case Swap(source, target) if source.gem != target.gem =>
      val score = List((aboveLeft, belowRight), (above, below), (belowLeft, aboveRight)) map { _ match {
        case (p, q) =>
//          val tiles2 = tiles -- List(source.position, target.position) ++ List(target.position -> source, source.position -> target)
//          val grid2: HexagonGrid[Tile] = HexagonGrid[Tile](tiles.hexagon, tiles2.toSeq: _*)
          val grid2 = tiles.swap(source.position, target.position)
          val xs = scoreMove(target.position, source.gem, p)(grid2)
          val ys = scoreMove(target.position, source.gem, q)(grid2)
          
          xs ++ ys
          // val score = 1 + scoreMove(target.position, source.gem, p)(tiles2).length + scoreMove(target.position, source.gem, q)(tiles2).length
          // if (score >= 3) score else 0
      } }
      
      (swap, score)
    
    case _ => (swap, Nil)
  }
    
  def score(swap: Swap)(implicit tiles: HexagonGrid[Tile]): Move = trySwap(swap) match {
    case (swap, Nil) => Move(swap, 0)
    case (swap, xs)  => Move(swap, xs map { _.length } sum)
  }
  
  def scoreBoard(tiles: Map[Position, Tile]): Int = {
    0
  }
    
  def scoreMove(position: Position, gem: Gem, direction: Position)(implicit tiles: HexagonGrid[Tile]): List[Tile] =
    Stream.iterate(position + direction) { _ + direction } takeWhile { p => tiles.contains(p) && tiles(p).gem == gem } map tiles toList
    
  def compact(tiles: Map[Position, Tile], direction: Position): Map[Position, Tile] = tiles
}