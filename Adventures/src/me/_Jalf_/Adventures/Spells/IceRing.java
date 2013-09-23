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

public class IceRing 
{
	public static Main plugin;
	
	public IceRing(Main plugin)
	{
		IceRing.plugin = plugin;
	}
	public static void iceRingSpell (Player player, double radiusValue, int height, final int time)
	{
		final Location playerLoc = player.getLocation();
		int radius = (int) Math.floor(radiusValue);
		
		final List<Location> iceRing = Methods.circle(playerLoc, radius, height, true, false, 0);
		final List<Block> removeRingBlocks = new ArrayList<Block>();
		for (Location iceRingLocations : iceRing)
		{
			Block iceRingBlock = iceRingLocations.getBlock();
			boolean breakWhile = false;
			boolean isAir = false;

			if (iceRingBlock.getType() != Material.AIR)
			{	
				if (iceRingBlock.getType() != Material.SNOW)
				{
					if (iceRingBlock.getLocation().add(0, 1, 0).getBlock().getType() == Material.AIR || 
							iceRingBlock.getLocation().add(0, 1, 0).getBlock().getType() == Material.SNOW)
					{
						iceRingBlock = iceRingBlock.getLocation().add(0, 1, 0).getBlock();
					}
					else if (iceRingBlock.getLocation().add(0, 2, 0).getBlock().getType() == Material.AIR || 
							iceRingBlock.getLocation().add(0, 2, 0).getBlock().getType() == Material.SNOW)
					{
						iceRingBlock = iceRingBlock.getLocation().add(0, 2, 0).getBlock();
					}
					else if (iceRingBlock.getLocation().add(0, 3, 0).getBlock().getType() == Material.AIR || 
							iceRingBlock.getLocation().add(0, 3, 0).getBlock().getType() == Material.SNOW)
					{
						iceRingBlock = iceRingBlock.getLocation().add(0, 3, 0).getBlock();
					}
					else breakWhile = true;
				}
			} else isAir = true;
			if (breakWhile == true || isAir == true)
			{
				if (iceRingBlock.getType() == Material.AIR || iceRingBlock.getType() == Material.SNOW)
				{
					if (iceRingBlock.getLocation().subtract(0, 1, 0).getBlock().getType().isSolid())
					{
						breakWhile = false;
					}
					else if (iceRingBlock.getLocation().subtract(0, 2, 0).getBlock().getType().isSolid())
					{
						iceRingBlock = iceRingBlock.getLocation().subtract(0, 1, 0).getBlock();
						breakWhile = false;
					} 
					else if (iceRingBlock.getLocation().subtract(0, 3, 0).getBlock().getType().isSolid())
					{
						iceRingBlock = iceRingBlock.getLocation().subtract(0, 2, 0).getBlock();
						breakWhile = false;
					}
				}
			}
			if (breakWhile == false)
			{
				iceRingBlock.setType(Material.ICE);						 
				removeRingBlocks.add(iceRingBlock);
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
