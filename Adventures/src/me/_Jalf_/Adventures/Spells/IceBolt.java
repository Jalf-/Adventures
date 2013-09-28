package me._Jalf_.Adventures.Spells;

import me._Jalf_.Adventures.Main;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Snowball;
import org.bukkit.metadata.FixedMetadataValue;

public class IceBolt 
{
	public static Main plugin;

	public IceBolt(Main plugin) 
	{
		IceBolt.plugin = plugin;
	}
	@SuppressWarnings("deprecation")
	public static void iceBoltSpell(Player player, int damage, int strength, int range) 
	{
		Block targetBlock = player.getTargetBlock(null, range);
		
		if (targetBlock != null)
		{
			Projectile iceBolt = player.launchProjectile(Snowball.class);
			iceBolt.setMetadata("Shooter", new FixedMetadataValue(plugin, player.getName()));
			iceBolt.setMetadata("Damage", new FixedMetadataValue(plugin, damage));
			iceBolt.setMetadata("Slow", new FixedMetadataValue(plugin, strength));
			iceBolt.setMetadata("Weakness", new FixedMetadataValue(plugin, strength));
		}
	}
}
