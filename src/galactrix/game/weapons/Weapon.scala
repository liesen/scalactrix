package galactrix.game.weapons

import galactrix.game.Player

trait Weapon {
  def name: String
  
  def `type`: WeaponType.Value
  
  def apply(player: Player): Option[Player]
  
  def canApply(player: Player): Boolean
}

object WeaponType extends Enumeration {
  val Offensive, Defensive = Value
}

trait OffensiveWeapon extends Weapon {
  override val `type` = WeaponType.Offensive
}

trait DefensiveWeapon extends Weapon {
  override val `type` = WeaponType.Defensive
}