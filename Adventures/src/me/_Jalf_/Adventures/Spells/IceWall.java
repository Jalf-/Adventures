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

public class IceWall 
{
	public static Main plugin;

	public IceWall(Main plugin)
	{
		IceWall.plugin = plugin;
	}
	@SuppressWarnings("deprecation")
	public static void iceWallSpell(Player player, int range, int length, int height, final int time)
	{		
		Block targetBlock = player.getTargetBlock(null, range);
		Location targetBlockLocation = targetBlock.getLocation();
		if (targetBlock != null)
		{
			if (targetBlock.getType() != Material.AIR)
			{
				int playerF = Math.abs(Methods.getF(player));
				
				int newX = 0;
				int newZ = 0;
				String compass = "";

				switch(playerF)
				{
				case 2:
					newX = (int) Math.floor(length / 2);
					compass = "North";
					break;
				case 3:
					newZ = (int) Math.floor(length / 2);
					compass = "East";
					break;
				case 4:
					newX = (int) Math.floor(length / 2);
					compass = "South";
					break;
				case 0:
					newX = (int) Math.floor(length / 2);
					compass = "South";
					break;
				case 1:
					newZ = (int) Math.floor(length / 2);
					compass = "West";
					break;
				}
				List<Block> wallLocations = new ArrayList<Block>();
				final List<Block> removeWallLocations = new ArrayList<Block>();

				while (length != 0)
				{	
					int wallHeight = height;
					int newestX = newX;
					int newestZ = newZ;

					while (wallHeight != 0)
					{
						wallHeight--;
						Block upWallLocations = targetBlockLocation.add(newestX, wallHeight, newestZ).getBlock();
						Block downWallLocations = targetBlockLocation.subtract(newestX, wallHeight, newestZ).getBlock();
						if (!wallLocations.contains(upWallLocations)) wallLocations.add(upWallLocations);
						if (!wallLocations.contains(downWallLocations)) wallLocations.add(downWallLocations);				
					}					
					if (compass.equals("North")) newX--;
					else if (compass.equals("East")) newZ--;
					else if (compass.equals("South")) newX--;
					else if (compass.equals("West")) newZ--;

					length--;
				}
				for (Block newWallLocations : wallLocations)
				{
					boolean breakWhile = false;
					boolean isAir = false;

					if (newWallLocations.getType() != Material.AIR)
					{	
						if (newWallLocations.getType() != Material.SNOW)
						{
							if (newWallLocations.getLocation().add(0, 1, 0).getBlock().getType() == Material.AIR || 
									newWallLocations.getLocation().add(0, 1, 0).getBlock().getType() == Material.SNOW)
							{
								newWallLocations = newWallLocations.getLocation().add(0, 1, 0).getBlock();
							}
							else if (newWallLocations.getLocation().add(0, 2, 0).getBlock().getType() == Material.AIR || 
									newWallLocations.getLocation().add(0, 2, 0).getBlock().getType() == Material.SNOW)
							{
								newWallLocations = newWallLocations.getLocation().add(0, 2, 0).getBlock();
							}
							else if (newWallLocations.getLocation().add(0, 3, 0).getBlock().getType() == Material.AIR || 
									newWallLocations.getLocation().add(0, 3, 0).getBlock().getType() == Material.SNOW)
							{
								newWallLocations = newWallLocations.getLocation().add(0, 3, 0).getBlock();
							}
							else breakWhile = true;
						}
					} else isAir = true;
					if (breakWhile == true || isAir == true)
					{
						if (newWallLocations.getType() == Material.AIR || newWallLocations.getType() == Material.SNOW)
						{
							if (newWallLocations.getLocation().subtract(0, 1, 0).getBlock().getType().isSolid())
							{
								breakWhile = false;
							}
							else if (newWallLocations.getLocation().subtract(0, 2, 0).getBlock().getType().isSolid())
							{
								newWallLocations = newWallLocations.getLocation().subtract(0, 1, 0).getBlock();
								breakWhile = false;
							} 
							else if (newWallLocations.getLocation().subtract(0, 3, 0).getBlock().getType().isSolid())
							{
								newWallLocations = newWallLocations.getLocation().subtract(0, 2, 0).getBlock();
								breakWhile = false;
							}
						}
					}
					if (breakWhile == false)
					{
						newWallLocations.setType(Material.ICE);						 
						removeWallLocations.add(newWallLocations);
					}
				}
				plugin.getServer().getScheduler().runTaskLater(plugin, new BukkitRunnable()
				{
					@Override
					public void run() 
					{
						if (!removeWallLocations.isEmpty())
						{
							for (Block newRemoveWallLocations : removeWallLocations)
							{
								Chunk loadChunk = newRemoveWallLocations.getLocation().getChunk();
								newRemoveWallLocations.getWorld().loadChunk(loadChunk);
								
								newRemoveWallLocations.setType(Material.AIR);
							}
						}
					}
				}, time);
			}
		}
	}
}
