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
import org.bukkit.util.Vector;

public class FireBreath 
{
	public static Main plugin;
	
	public FireBreath (Main plugin)
	{
		FireBreath.plugin = plugin;
	}
	public static void fireBreathSpell (Player player, double radius, int angle, int time)
	{
		Location playerLoc = player.getLocation();
		List<Vector> positions = Methods.getPositionsInCone(playerLoc.toVector(),(float) radius, angle, playerLoc.getDirection());
		final List<Block> removeCone = new ArrayList<Block>();
		
		for (Vector coneVector : positions)
		{
			Block coneBlock = new Location(player.getWorld(), coneVector.getX(), coneVector.getY(), coneVector.getZ()).getBlock();
			
			boolean breakWhile = false;
			boolean isAir = false;

			if (coneBlock.getType() != Material.AIR)
			{	
				if (coneBlock.getType() != Material.SNOW)
				{
					if (coneBlock.getLocation().add(0, 1, 0).getBlock().getType() == Material.AIR || 
							coneBlock.getLocation().add(0, 1, 0).getBlock().getType() == Material.SNOW)
					{
						coneBlock = coneBlock.getLocation().add(0, 1, 0).getBlock();
					}
					else if (coneBlock.getLocation().add(0, 2, 0).getBlock().getType() == Material.AIR || 
							coneBlock.getLocation().add(0, 2, 0).getBlock().getType() == Material.SNOW)
					{
						coneBlock = coneBlock.getLocation().add(0, 2, 0).getBlock();
					}
					else if (coneBlock.getLocation().add(0, 3, 0).getBlock().getType() == Material.AIR || 
							coneBlock.getLocation().add(0, 3, 0).getBlock().getType() == Material.SNOW)
					{
						coneBlock = coneBlock.getLocation().add(0, 3, 0).getBlock();
					}
					else breakWhile = true;
				}
			} else isAir = true;
			if (breakWhile == true || isAir == true)
			{
				if (coneBlock.getType() == Material.AIR || coneBlock.getType() == Material.SNOW)
				{
					if (coneBlock.getLocation().subtract(0, 1, 0).getBlock().getType().isSolid())
					{
						breakWhile = false;
					}
					else if (coneBlock.getLocation().subtract(0, 2, 0).getBlock().getType().isSolid())
					{
						coneBlock = coneBlock.getLocation().subtract(0, 1, 0).getBlock();
						breakWhile = false;
					} 
					else if (coneBlock.getLocation().subtract(0, 3, 0).getBlock().getType().isSolid())
					{
						coneBlock = coneBlock.getLocation().subtract(0, 2, 0).getBlock();
						breakWhile = false;
					}
				}
				
			}
			if (breakWhile == false)
			{
				coneBlock.setType(Material.FIRE);						 
				removeCone.add(coneBlock);
			}
		}
		plugin.getServer().getScheduler().runTaskLater(plugin, new BukkitRunnable()
		{
			@Override
			public void run() 
			{
				if (!removeCone.isEmpty())
				{
					for (Block newRemoveCone : removeCone)
					{
						Chunk loadChunk = newRemoveCone.getLocation().getChunk();
						newRemoveCone.getWorld().loadChunk(loadChunk);
						
						newRemoveCone.setType(Material.AIR);
					}
				}
			}
		}, time);
	}
}
