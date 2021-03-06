package me._Jalf_.Adventures.Spells;

import me._Jalf_.Adventures.Main;

import org.bukkit.block.Block;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.metadata.FixedMetadataValue;

public class PoisonDart 
{
	public static Main plugin;

	public PoisonDart(Main plugin) 
	{
		PoisonDart.plugin = plugin;
	}
	@SuppressWarnings("deprecation")
	public static void poisonDartSpell(Player player, int damage, int strength, int range) 
	{
		Block targetBlock = player.getTargetBlock(null, range);
		
		if (targetBlock != null)
		{
			Projectile arrow = player.launchProjectile(Arrow.class);
			arrow.setMetadata("Shooter", new FixedMetadataValue(plugin, player.getName()));
			arrow.setMetadata("Damage", new FixedMetadataValue(plugin, damage));
			arrow.setMetadata("Poison", new FixedMetadataValue(plugin, strength));
		}
	}
}
