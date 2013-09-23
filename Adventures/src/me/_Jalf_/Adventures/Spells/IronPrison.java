package me._Jalf_.Adventures.Spells;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import me._Jalf_.Adventures.Main;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class IronPrison 
{
	public static Main plugin;

	public IronPrison(Main plugin)
	{
		IronPrison.plugin = plugin;
	}	
	@SuppressWarnings("deprecation")
	public static void ironPrisonSpell(final Player player, int time, int range, double radius)
	{
		Block targetBlock = player.getTargetBlock(null, range);
		Location targetBlockLocation = targetBlock.getLocation();
		double radiusSquared = radius * radius;
		List<Entity> nearbyEntities = player.getNearbyEntities(150, 150, 150);
		List<Double> entitiesInside = new ArrayList<>();
		List<Block> cageLoc = new ArrayList<>();
		List<Block> cageIronLoc = new ArrayList<>();
		
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
							entitiesInside.add(entity.getLocation().distance(targetBlockLocation));
						}
					}
				}
				if (!entitiesInside.isEmpty())
				{
					int indexValue = entitiesInside.indexOf(Collections.min(entitiesInside));
					double minValue = entitiesInside.get(indexValue);

					for (Entity entity : nearbyEntities)
					{
						if (entity.getLocation().distance(targetBlockLocation) == minValue)
						{	
							if (entity instanceof LivingEntity)
							{
								Block blockLoc = entity.getLocation().getBlock();
								cageIronLoc.add(blockLoc.getLocation().add(0, -1, 0).getBlock());

								for (int i = 0; i < 2; i++)
								{
									cageLoc.add(blockLoc.getLocation().add(1, i, 0).getBlock());
									cageLoc.add(blockLoc.getLocation().add(-1, i, 0).getBlock());
									cageLoc.add(blockLoc.getLocation().add(0, i, 1).getBlock());
									cageLoc.add(blockLoc.getLocation().add(0, i, -1).getBlock());
									cageLoc.add(blockLoc.getLocation().add(1, i, 1).getBlock());
									cageLoc.add(blockLoc.getLocation().add(-1, i, -1).getBlock());
									cageLoc.add(blockLoc.getLocation().add(-1, i, 1).getBlock());
									cageLoc.add(blockLoc.getLocation().add(1, i, -1).getBlock());
								}
								for (int i = -1; i < 3; i += 3)
								{
									cageIronLoc.add(blockLoc.getLocation().add(1, i, 0).getBlock());
									cageIronLoc.add(blockLoc.getLocation().add(-1, i, 0).getBlock());
									cageIronLoc.add(blockLoc.getLocation().add(0, i, 1).getBlock());
									cageIronLoc.add(blockLoc.getLocation().add(0, i, -1).getBlock());
									cageIronLoc.add(blockLoc.getLocation().add(1, i, 1).getBlock());
									cageIronLoc.add(blockLoc.getLocation().add(-1, i, -1).getBlock());
									cageIronLoc.add(blockLoc.getLocation().add(-1, i, 1).getBlock());
									cageIronLoc.add(blockLoc.getLocation().add(1, i, -1).getBlock());
								}
								cageIronLoc.add(blockLoc.getLocation().add(0, 2, 0).getBlock());
								break;
							}
						}
					}
				}
				if (!cageLoc.isEmpty())
				{
					final List<Block> cageBlockList = new ArrayList<>();
					for (Block cageBlock : cageLoc)
					{
						if (cageBlock.getType() == Material.SNOW || cageBlock.getType() == Material.AIR)
						{
							cageBlock.setType(Material.IRON_FENCE);
							cageBlockList.add(cageBlock);
						}
					}
					for (Block cageIronBlock : cageIronLoc)
					{
						if (cageIronBlock.getType() == Material.SNOW || cageIronBlock.getType() == Material.AIR)
						{
							cageIronBlock.setType(Material.IRON_BLOCK);
							cageBlockList.add(cageIronBlock);
						}
					}
					plugin.getServer().getScheduler().runTaskLater(plugin, new BukkitRunnable()
					{
						@Override
						public void run() 
						{
							if (!cageBlockList.isEmpty())
							{
								for (Block cageBlock : cageBlockList)
								{
									Chunk loadChunk = cageBlock.getLocation().getChunk();
									cageBlock.getWorld().loadChunk(loadChunk);

									cageBlock.setType(Material.AIR);
								}
							}
						}
					}, time);
				}
			}
		}	
	}
}
