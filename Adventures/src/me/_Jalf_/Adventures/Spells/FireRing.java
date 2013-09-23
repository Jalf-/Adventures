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

public class FireRing 
{
public static Main plugin;
	
	public FireRing(Main plugin)
	{
		FireRing.plugin = plugin;
	}
	public static void fireRingSpell (Player player, double radiusValue, int width, final int time)
	{
		final Location playerLoc = player.getLocation();
		int radius = (int) Math.floor(radiusValue);

		List<Location> fireRing = new ArrayList<>();
		for (int i = width; i > 0; i--)
		{
			fireRing.addAll(Methods.circle(playerLoc, radius+i, 1, true, false, 0));
		}
		final List<Block> removeRingBlocks = new ArrayList<Block>();
		for (Location fireRingLocations : fireRing)
		{
			Block fireRingBlock = fireRingLocations.getBlock();
			boolean breakWhile = false;
			boolean isAir = false;

			if (fireRingBlock.getType() != Material.AIR)
			{	
				if (fireRingBlock.getType() != Material.SNOW)
				{
					if (fireRingBlock.getLocation().add(0, 1, 0).getBlock().getType() == Material.AIR || 
							fireRingBlock.getLocation().add(0, 1, 0).getBlock().getType() == Material.SNOW)
					{
						fireRingBlock = fireRingBlock.getLocation().add(0, 1, 0).getBlock();
					}
					else if (fireRingBlock.getLocation().add(0, 2, 0).getBlock().getType() == Material.AIR || 
							fireRingBlock.getLocation().add(0, 2, 0).getBlock().getType() == Material.SNOW)
					{
						fireRingBlock = fireRingBlock.getLocation().add(0, 2, 0).getBlock();
					}
					else if (fireRingBlock.getLocation().add(0, 3, 0).getBlock().getType() == Material.AIR || 
							fireRingBlock.getLocation().add(0, 3, 0).getBlock().getType() == Material.SNOW)
					{
						fireRingBlock = fireRingBlock.getLocation().add(0, 3, 0).getBlock();
					}
					else breakWhile = true;
				}
			} else isAir = true;
			if (breakWhile == true || isAir == true)
			{
				if (fireRingBlock.getType() == Material.AIR || fireRingBlock.getType() == Material.SNOW)
				{
					if (fireRingBlock.getLocation().subtract(0, 1, 0).getBlock().getType().isSolid())
					{
						breakWhile = false;
					}
					else if (fireRingBlock.getLocation().subtract(0, 2, 0).getBlock().getType().isSolid())
					{
						fireRingBlock = fireRingBlock.getLocation().subtract(0, 1, 0).getBlock();
						breakWhile = false;
					} 
					else if (fireRingBlock.getLocation().subtract(0, 3, 0).getBlock().getType().isSolid())
					{
						fireRingBlock = fireRingBlock.getLocation().subtract(0, 2, 0).getBlock();
						breakWhile = false;
					}
				}
			}
			if (breakWhile == false)
			{
				fireRingBlock.setType(Material.FIRE);						 
				removeRingBlocks.add(fireRingBlock);
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
