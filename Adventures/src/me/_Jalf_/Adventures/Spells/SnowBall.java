package me._Jalf_.Adventures.Spells;

import me._Jalf_.Adventures.Main;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Snowball;
import org.bukkit.metadata.FixedMetadataValue;

public class SnowBall 
{
	public static Main plugin;

	public SnowBall(Main plugin) 
	{
		SnowBall.plugin = plugin;
	}
	@SuppressWarnings("deprecation")
	public static void snowBallSpell(Player player, int damage, int strength, int range) 
	{
		Block targetBlock = player.getTargetBlock(null, range);
		
		if (targetBlock != null)
		{
			Projectile snowBall = player.launchProjectile(Snowball.class);
			snowBall.setMetadata("Shooter", new FixedMetadataValue(plugin, player.getName()));
			snowBall.setMetadata("Damage", new FixedMetadataValue(plugin, damage));
			snowBall.setMetadata("Slow", new FixedMetadataValue(plugin, strength));
		}
	}
}
