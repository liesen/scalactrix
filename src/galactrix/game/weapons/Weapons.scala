package galactrix.game.weapons

import galactrix.game.weapons.{DefensiveWeapon, OffensiveWeapon}
import galactrix.game.Gem
import galactrix.game.Player

object ShieldMatrix extends DefensiveWeapon {
  override val name = "Shield matrix"
  
  override def canApply(player: Player) =
    player.gems.getOrElse(Gem.Yellow, 0) >= 6 && player.gems.getOrElse(Gem.Green, 0) >= 3
    
  override def apply(player: Player) =
    if (!canApply(player)) {
      None
    } else {
      player.gems(Gem.Yellow) -= 6
      player.gems(Gem.Green) -= 3
      Some(player)
    }
}

object SpaceBomb extends OffensiveWeapon {
  override val name = "Space bomb"
    
  override def canApply(player: Player) =
    player.gems.getOrElse(Gem.Red, 0) >= 9 && player.gems.getOrElse(Gem.Green, 0) >= 2
    
  override def apply(player: Player) =
    if (!canApply(player)) {
      None
    } else {
      player.gems(Gem.Red) -= 9
      player.gems(Gem.Green) -= 2
      Some(player)
    }
}

object TridentLaser extends OffensiveWeapon {
  override val name = "Trident laser"
    
  override def canApply(player: Player) =
    player.gems.getOrElse(Gem.Red, 0) >= 15
    
  override def apply(player: Player) =
    if (!canApply(player)) {
      None
    } else {
      player.gems(Gem.Red) -= 15
      Some(player)
    }
}