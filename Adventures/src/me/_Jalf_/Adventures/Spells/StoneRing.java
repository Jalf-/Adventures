package me._Jalf_.Adventures.Spells;

import java.util.ArrayList;
import java.util.List;

import me._Jalf_.Adventures.Main;
import me._Jalf_.Adventures.Methods;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class StoneRing 
{
public static Main plugin;
	
	public StoneRing(Main plugin)
	{
		StoneRing.plugin = plugin;
	}
	public static void stoneRingSpell (Player player, double radiusValue, int height, final int time)
	{
		final Location playerLoc = player.getLocation();
		int radius = (int) Math.floor(radiusValue);
		
		final List<Location> stoneRing = Methods.circle(playerLoc, radius, height, true, false, 0);
		final List<Block> removeRingBlocks = new ArrayList<Block>();
		for (Location stoneRingLocations : stoneRing)
		{
			Block stoneRingBlock = stoneRingLocations.getBlock();
			boolean breakWhile = false;
			boolean isAir = false;

			if (stoneRingBlock.getType() != Material.AIR)
			{	
				if (stoneRingBlock.getType() != Material.SNOW)
				{
					if (stoneRingBlock.getLocation().add(0, 1, 0).getBlock().getType() == Material.AIR || 
							stoneRingBlock.getLocation().add(0, 1, 0).getBlock().getType() == Material.SNOW)
					{
						stoneRingBlock = stoneRingBlock.getLocation().add(0, 1, 0).getBlock();
					}
					else if (stoneRingBlock.getLocation().add(0, 2, 0).getBlock().getType() == Material.AIR || 
							stoneRingBlock.getLocation().add(0, 2, 0).getBlock().getType() == Material.SNOW)
					{
						stoneRingBlock = stoneRingBlock.getLocation().add(0, 2, 0).getBlock();
					}
					else if (stoneRingBlock.getLocation().add(0, 3, 0).getBlock().getType() == Material.AIR || 
							stoneRingBlock.getLocation().add(0, 3, 0).getBlock().getType() == Material.SNOW)
					{
						stoneRingBlock = stoneRingBlock.getLocation().add(0, 3, 0).getBlock();
					}
					else breakWhile = true;
				}
			} else isAir = true;
			if (breakWhile == true || isAir == true)
			{
				if (stoneRingBlock.getType() == Material.AIR || stoneRingBlock.getType() == Material.SNOW)
				{
					if (stoneRingBlock.getLocation().subtract(0, 1, 0).getBlock().getType().isSolid())
					{
						breakWhile = false;
					}
					else if (stoneRingBlock.getLocation().subtract(0, 2, 0).getBlock().getType().isSolid())
					{
						stoneRingBlock = stoneRingBlock.getLocation().subtract(0, 1, 0).getBlock();
						breakWhile = false;
					} 
					else if (stoneRingBlock.getLocation().subtract(0, 3, 0).getBlock().getType().isSolid())
					{
						stoneRingBlock = stoneRingBlock.getLocation().subtract(0, 2, 0).getBlock();
						breakWhile = false;
					}
				}
			}
			if (breakWhile == false)
			{
				stoneRingBlock.setType(Material.SMOOTH_BRICK);						 
				removeRingBlocks.add(stoneRingBlock);
			}
		}
		plugin.getServer().getScheduler().runTaskLater(plugin, new BukkitRunnable()
		{
			@Override
			public void run() 
			{
				if (!removeRingBlocks.isEmpty())
				{
					for (Block newRemoveRingBlocks : removeRingBlocks)
					{
						Chunk loadChunk = newRemoveRingBlocks.getLocation().getChunk();
						newRemoveRingBlocks.getWorld().loadChunk(loadChunk);
						
						newRemoveRingBlocks.setType(Material.AIR);
					}
				}
			}
		}, time);
	}
}
