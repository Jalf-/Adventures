package me._Jalf_.Adventures.Spells;

import me._Jalf_.Adventures.Main;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.SmallFireball;
import org.bukkit.metadata.FixedMetadataValue;

public class FireBolt
{
	public static Main plugin;

	public FireBolt(Main plugin) 
	{
		FireBolt.plugin = plugin;
	}
	@SuppressWarnings("deprecation")
	public static void fireBoltSpell(Player player, int damage, int strength, int range) 
	{
		Block targetBlock = player.getTargetBlock(null, range);
		
		if (targetBlock != null)
		{
			Projectile fireBolt = player.launchProjectile(SmallFireball.class);
			fireBolt.setMetadata("Damage" + damage, new FixedMetadataValue(plugin, true));
			fireBolt.setMetadata("Fire" + strength, new FixedMetadataValue(plugin, true));
		}
	}
}
