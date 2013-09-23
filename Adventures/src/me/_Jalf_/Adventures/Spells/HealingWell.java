package me._Jalf_.Adventures.Spells;

import java.util.ArrayList;
import java.util.List;

import me._Jalf_.Adventures.Main;
import me._Jalf_.Adventures.Methods;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class HealingWell 
{
	public static Main plugin;

	public HealingWell(Main plugin)
	{
		HealingWell.plugin = plugin;
	}
	@SuppressWarnings("deprecation")
	public static void healingWellSpell (final Player player, int range, double radiusValue, final int time, final int heal)
	{
		Block targetBlock = player.getTargetBlock(null, range);
		final Location targetBlockLocation = targetBlock.getLocation();
		final double radiusSquared = radiusValue * radiusValue;
		if (targetBlock != null)
		{
			if (targetBlock.getType() != Material.AIR)
			{
				final int radius = (int) Math.floor(radiusValue);

				List<Location> stoneList = Methods.circle(targetBlockLocation, radius, 1, true, false, 1);
				List<Location> lowStoneList = Methods.circle(targetBlockLocation, radius - 1, 1, false, false, 0);
				List<Location> waterList = Methods.circle(targetBlockLocation, radius - 1, 1, false, false, 1);
				final List<Block> airList = new ArrayList<>();
				
				for (Location stone : stoneList)
				{
					if (stone.getBlock().getType() == Material.AIR || stone.getBlock().getType() == Material.SNOW)
					{
						stone.getBlock().setType(Material.STEP);
						airList.add(stone.getBlock());
					}
				}
				for (Location stone : lowStoneList)
				{
					if (stone.getBlock().getType() == Material.AIR || stone.getBlock().getType() == Material.SNOW)
					{
						stone.getBlock().setType(Material.DOUBLE_STEP);
						airList.add(stone.getBlock());
					}
				}
				List<Material> waterDestroy = Methods.waterDestroy();
				for (Location stone : lowStoneList)
				{
					for (Material waterMat : waterDestroy)
					{
						if (stone.getBlock().getType() == waterMat)
						{
							if (stone.clone().add(0, 1, 0).getBlock().getType() == Material.AIR || stone.clone().add(0, 1, 0).getBlock().getType() == Material.SNOW)
							{
								stone.clone().add(0, 1, 0).getBlock().setType(Material.STEP);
								airList.add(stone.clone().add(0, 1, 0).getBlock());
							}
						}
					}
				}
				for (Location water : waterList)
				{
					for (Material waterMat : waterDestroy)
					{
						if (water.getBlock().getType() == waterMat)
						{
							if (water.clone().add(1, 0, 0).getBlock().getType() == Material.AIR || 
									water.clone().add(1, 0, 0).getBlock().getType() == Material.SNOW || water.clone().add(1, 0, 0).getBlock().getType() == Material.STATIONARY_WATER)
							{
								water.clone().add(1, 0, 0).getBlock().setType(Material.DOUBLE_STEP);
								airList.add(water.clone().add(1, 0, 0).getBlock());
							}
							if (water.clone().add(-1, 0, 0).getBlock().getType() == Material.AIR || 
									water.clone().add(-1, 0, 0).getBlock().getType() == Material.SNOW || 
									water.clone().add(-1, 0, 0).getBlock().getType() == Material.STATIONARY_WATER || water.clone().add(-1, 0, 0).getBlock().getType() == Material.WATER)
							{
								water.clone().add(-1, 0, 0).getBlock().setType(Material.DOUBLE_STEP);
								airList.add(water.clone().add(-1, 0, 0).getBlock());
							}
							if (water.clone().add(0, 0, 1).getBlock().getType() == Material.AIR || 
									water.clone().add(0, 0, 1).getBlock().getType() == Material.SNOW || water.clone().add(0, 0, 1).getBlock().getType() == Material.STATIONARY_WATER)
							{
								water.clone().add(0, 0, 1).getBlock().setType(Material.DOUBLE_STEP);
								airList.add(water.clone().add(0, 0, 1).getBlock());
							}
							if (water.clone().add(0, 0, -1).getBlock().getType() == Material.AIR || 
									water.clone().add(0, 0, -1).getBlock().getType() == Material.SNOW || water.clone().add(0, 0, -1).getBlock().getType() == Material.STATIONARY_WATER)
							{
								water.clone().add(0, 0, -1).getBlock().setType(Material.STEP);
								airList.add(water.clone().add(0, 0, -1).getBlock());
							}
						}
					}
					if (water.getBlock().getType() == Material.AIR || water.getBlock().getType() == Material.SNOW)
					{
						water.getBlock().setType(Material.STATIONARY_WATER);
						airList.add(water.getBlock());
					}
				}
				plugin.getServer().getScheduler().runTaskLater(plugin, new BukkitRunnable()
				{
					@Override
					public void run() 
					{
						if (!airList.isEmpty())
						{
							for (Block air : airList)
							{
								Chunk loadChunk = air.getLocation().getChunk();
								air.getWorld().loadChunk(loadChunk);
								
								air.setType(Material.AIR);
							}
						}
					}
				}, time);
				new BukkitRunnable() 
				{
					int timer = time;
					@Override
					public void run()
					{			
						if (timer > 0)
						{					
							if (player.getLocation().distanceSquared(targetBlockLocation) <= radiusSquared - 1)
							{	
								if (player.getMaxHealth() > player.getHealth() + heal)
								{
									player.setHealth(player.getHealth() + heal);
								} 
								else player.setHealth(player.getMaxHealth());
							}
							List<Entity> nearbyEntities = player.getNearbyEntities(150, 150, 150);
							
							for (Entity entity : nearbyEntities)
							{
								if (entity.getLocation().distanceSquared(targetBlockLocation) <= radiusSquared - 1)
								{	
									if (entity instanceof LivingEntity)
									{
										if (entity.getType() == EntityType.PIG_ZOMBIE || entity.getType() == EntityType.ZOMBIE || 
												entity.getType() == EntityType.SKELETON)
										{
											((LivingEntity) entity).damage((double) heal);
										}
//										if (party is inside well then heal)
									}
								}
							}				
							timer = timer - 20;
						} else this.cancel();
					}
				}.runTaskTimer(plugin, 0, 20);
			}
		}
	}
}
