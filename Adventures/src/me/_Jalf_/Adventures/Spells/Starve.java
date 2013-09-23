package me._Jalf_.Adventures.Spells;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

public class Starve 
{
	@SuppressWarnings("deprecation")
	public static void starveSpell(Player player, int potionStrength, double radius, int range, int time) 
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
							if (entity instanceof Player)
							{
//								((Player) entity).getScoreboard().
								((LivingEntity) entity).addPotionEffect(PotionEffectType.HUNGER.createEffect(time, potionStrength));
							}
							else
							{
								((LivingEntity) entity).addPotionEffect(PotionEffectType.WEAKNESS.createEffect(time, potionStrength));
							}
						}
					}
				}				
			}
		}
	}
}
