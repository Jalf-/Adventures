package me._Jalf_.Adventures.Spells;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import me._Jalf_.Adventures.Main;
import me._Jalf_.Adventures.PartyHandler;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class ChainLightning 
{
	public static Main plugin;
	
	public ChainLightning (Main plugin)
	{
		ChainLightning.plugin = plugin;
	}
	@SuppressWarnings("deprecation")
	public static void chainLightningSpell (final Player player, final int damage, final double radius, int range, final int strengthValue)
	{
		final Block targetBlock = player.getTargetBlock(null, range);
		final double radiusSquared = radius * radius;

		if (targetBlock != null)
		{
			if (targetBlock.getType() != Material.AIR)
			{	
				new BukkitRunnable() 
				{
					int strength = strengthValue;
					Location targetBlockLocation = targetBlock.getLocation();
					List<UUID> entitiesHit = new ArrayList<>();
					@Override
					public void run()
					{	
						List<Double> entitiesInside = new ArrayList<>();
						if (strength > 0)
						{
							List<Entity> nearbyEntities = player.getNearbyEntities(150, 150, 150);
							for (Entity entity : nearbyEntities)
							{
								if (entity.getLocation().distanceSquared(targetBlockLocation) <= radiusSquared)
								{	
									if (entity instanceof LivingEntity)
									{
										if (entity instanceof Player)
										{
											if (!PartyHandler.isPlayerPartOfAskingPlayersParty(player, ((Player) entity)))
											{
												if (!entitiesHit.contains(entity.getUniqueId())) 
													entitiesInside.add(entity.getLocation().distanceSquared(targetBlockLocation));
											}
										}
										else if (!entitiesHit.contains(entity.getUniqueId())) 
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
											((LivingEntity) entity).damage((double) damage);
											player.getWorld().strikeLightningEffect(entity.getLocation());
											entitiesHit.add(entity.getUniqueId());
											
											targetBlockLocation = entity.getLocation();
											entitiesInside.clear();
											break;
										}
									}
								}
							} else this.cancel();
							strength--;
						} else this.cancel();
					}
				}.runTaskTimer(plugin, 0, 10);	
			}
		}
	}
}
