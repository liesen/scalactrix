package galactrix.geom

case class Direction(val delta: Position, override val toString: String) extends Position(delta.x, delta.y, delta.z)

case object AboveLeft extends Direction(Position(0, -1, 1), "NW")
case object Above extends Direction(Position(-1, 1, 0), "N")
case object AboveRight extends Direction(Position(-1, 0, 1), "NE")
case object BelowRight extends Direction(Position(0, -1, 1), "SE")
case object Below extends Direction(Position(1, -1, 0), "S")
case object BelowLeft extends Direction(Position(1, 0, -1), "SW")