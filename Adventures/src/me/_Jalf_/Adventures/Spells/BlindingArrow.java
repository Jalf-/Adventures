package me._Jalf_.Adventures.Spells;

import me._Jalf_.Adventures.Main;

import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.metadata.FixedMetadataValue;

public class BlindingArrow 
{
	public static Main plugin;

	public BlindingArrow(Main plugin) 
	{
		BlindingArrow.plugin = plugin;
	}
	
	public static void blindingArrowSpell(Player player, int damage, int strength) 
	{
		Projectile arrow = player.launchProjectile(Arrow.class);
		arrow.setMetadata("Damage" + damage, new FixedMetadataValue(plugin, true));
		arrow.setMetadata("Blindness" + strength, new FixedMetadataValue(plugin, true));
	}
}
