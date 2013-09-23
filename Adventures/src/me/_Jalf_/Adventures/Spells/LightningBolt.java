package me._Jalf_.Adventures.Spells;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

public class LightningBolt
{	
	@SuppressWarnings("deprecation")
	public static void lightningBoltSpell(Player player, int damage, double radius, int range) 
	{
		Block targetBlock = player.getTargetBlock(null, range);
		Location targetBlockLocation = targetBlock.getLocation();
		double radiusSquared = radius * radius;
		List<Entity> nearbyEntities = player.getNearbyEntities(150, 150, 150);

		if (targetBlock != null)
		{
			if (targetBlock.getType() != Material.AIR)
			{
				for (Entity entity : nearbyEntities)
				{
					if (entity.getLocation().distanceSquared(targetBlockLocation) <= radiusSquared)
					{	
						if (entity instanceof LivingEntity)
						{
							((LivingEntity) entity).damage(damage);
							player.getWorld().strikeLightningEffect(entity.getLocation());
						}
					}
				}				
			}
		}
	}
}
