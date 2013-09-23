package me._Jalf_.Adventures.Spells;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

public class Terrify 
{	
	@SuppressWarnings("deprecation")
	public static void terrifySpell(final Player player, int time, int range, double radius, int strength)
	{
		Block targetBlock = player.getTargetBlock(null, range);
		Location targetBlockLocation = targetBlock.getLocation();
		double radiusSquared = radius * radius;
		List<Entity> nearbyEntities = player.getNearbyEntities(150, 150, 150);
		List<Double> entitiesInside = new ArrayList<>();
		
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
							entitiesInside.add(entity.getLocation().distanceSquared(targetBlockLocation));
						}
					}
				}
				if (!entitiesInside.isEmpty())
				{
					int indexValue = entitiesInside.indexOf(Collections.min(entitiesInside));
					double minValue = entitiesInside.get(indexValue);

					for (Entity entity : nearbyEntities)
					{
						if (entity.getLocation().distanceSquared(targetBlockLocation) == minValue)
						{	
							if (entity instanceof LivingEntity)
							{
								((LivingEntity) entity).addPotionEffect(PotionEffectType.CONFUSION.createEffect(time, strength));
								((LivingEntity) entity).addPotionEffect(PotionEffectType.SLOW.createEffect(time, strength));
								break;
							}
						}
					}
				}
			}
		}	
	}
}
