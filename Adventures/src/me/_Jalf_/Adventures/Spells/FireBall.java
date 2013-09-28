package me._Jalf_.Adventures.Spells;

import me._Jalf_.Adventures.Main;

import org.bukkit.block.Block;
import org.bukkit.entity.LargeFireball;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.metadata.FixedMetadataValue;

public class FireBall 
{
	public static Main plugin;

	public FireBall(Main plugin) 
	{
		FireBall.plugin = plugin;
	}
	@SuppressWarnings("deprecation")
	public static void fireBallSpell(Player player, int damage, int strength, int range) 
	{
		Block targetBlock = player.getTargetBlock(null, range);
		
		if (targetBlock != null)
		{
			Projectile fireBall	= player.launchProjectile(LargeFireball.class);
			fireBall.setMetadata("Shooter", new FixedMetadataValue(plugin, player.getName()));
			fireBall.setMetadata("Damage" + damage, new FixedMetadataValue(plugin, damage));
			fireBall.setMetadata("Fire", new FixedMetadataValue(plugin, strength));
			fireBall.setMetadata("Explosive", new FixedMetadataValue(plugin, strength));
		}
	}
}
