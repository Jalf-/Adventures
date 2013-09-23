package me._Jalf_.Adventures.Spells;

import java.util.ArrayList;
import java.util.List;

import me._Jalf_.Adventures.Main;
import me._Jalf_.Adventures.Methods;

import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class FireWalk 
{
	public static Main plugin;

	public FireWalk(Main plugin)
	{
		FireWalk.plugin = plugin;
	}
	public static void fireWalkSpell(final Player player, final int width, final int time) 
	{
		new BukkitRunnable() 
		{
			int timer = time;
			@Override
			public void run() 
			{		
				if (timer > 0)
				{	
					int playerF = Math.abs(Methods.getF(player));
					int newX = 0;
					int newZ = 0;
					String compass = "";

					switch(playerF)
					{
					case 2:
						newZ = 2;
						compass = "North";
						break;
					case 1:
						newX = -2;
						compass = "East";
						break;
					case 0:
						newZ = -2;
						compass = "South";
						break;
					case 4:
						newZ = -2;
						compass = "South";
						break;
					case 3:
						newX = 2;
						compass = "West";
						break;
					}			
					List<Block> blockLocation = new ArrayList<>();
					final List<Block> removeBlockLocations = new ArrayList<Block>();
					final Block playerBlockLocation = player.getLocation().getBlock();
					if (width > 2)
					{
						int newestX = newX;
						int newestZ = newZ;
						
						if (compass.equals("North")) 
						{
							int temp = (int) Math.floor(width / 2);
							newestX = temp; 
							newX = width;
						}
						else if (compass.equals("East"))
						{
							int temp = (int) Math.floor(width / 2);
							newestZ = temp;
							newZ = width;
						} 
						else if (compass.equals("South"))
						{
							int temp = (int) Math.floor(width / 2);
							newestX = temp;
							newX = width;
						} 
						else if (compass.equals("West"))
						{
							int temp = (int) Math.floor(width / 2);
							newestZ = temp;
							newZ = width;
						} 
						int wallWidth = width;
						while (wallWidth != 0)
						{
							Block upWallLocations = playerBlockLocation.getLocation().getBlock();
							Block downWallLocations = playerBlockLocation.getLocation().getBlock();
							if (compass.equals("North")) 
							{
								upWallLocations = playerBlockLocation.getLocation().add(newestX, 0, newestZ).getBlock();
								downWallLocations = playerBlockLocation.getLocation().subtract(newestX, 0, -newestZ).getBlock();
							}
							else if (compass.equals("East"))
							{
								upWallLocations = playerBlockLocation.getLocation().add(newestX, 0, newestZ).getBlock();
								downWallLocations = playerBlockLocation.getLocation().subtract(-newestX, 0, newestZ).getBlock();
							} 
							else if (compass.equals("South"))
							{
								upWallLocations = playerBlockLocation.getLocation().add(newestX, 0, newestZ).getBlock();
								downWallLocations = playerBlockLocation.getLocation().subtract(newestX, 0, -newestZ).getBlock();
							} 
							else if (compass.equals("West"))
							{
								upWallLocations = playerBlockLocation.getLocation().add(newestX, 0, newestZ).getBlock();
								downWallLocations = playerBlockLocation.getLocation().subtract(-newestX, 0, newestZ).getBlock();
							}
							blockLocation.add(upWallLocations);
							blockLocation.add(downWallLocations);
							wallWidth--;

							if (compass.equals("North")) newestX--;
							else if (compass.equals("East")) newestZ--;
							else if (compass.equals("South")) newestX--;
							else if (compass.equals("West")) newestZ--;
						}
						for (Block newBlockLocations : blockLocation)
						{
							boolean breakWhile = false;
							boolean isAir = false;

							if (newBlockLocations.getType() != Material.AIR)
							{	
								if (newBlockLocations.getType() != Material.SNOW)
								{
									if (newBlockLocations.getLocation().add(0, -1, 0).getBlock().getType() == Material.AIR || 
											newBlockLocations.getLocation().add(0, -1, 0).getBlock().getType() == Material.SNOW)
									{
										newBlockLocations = newBlockLocations.getLocation().add(0, 1, 0).getBlock();
									}
									else breakWhile = true;
								}
							} else isAir = true;
							if (breakWhile == true || isAir == true)
							{
								if (newBlockLocations.getType() == Material.AIR || newBlockLocations.getType() == Material.SNOW)
								{
									if (newBlockLocations.getLocation().subtract(0, -1, 0).getBlock().getType().isSolid())
									{
										breakWhile = false;
									}
								}
							}
							if (breakWhile == false)
							{
								newBlockLocations.setType(Material.FIRE);						 
								removeBlockLocations.add(newBlockLocations);
							}
						}	
						plugin.getServer().getScheduler().runTaskLater(plugin, new BukkitRunnable()
						{
							@Override
							public void run() 
							{
								if (!removeBlockLocations.isEmpty())
								{
									for (Block newRemoveBlockLocations : removeBlockLocations)
									{
										newRemoveBlockLocations.setType(Material.AIR);
									}
								}
							}
						}, 100);
					}
					else
					{
						if (playerBlockLocation.getLocation().add(newX, 0, newZ).getBlock().getType() == Material.AIR || 
								playerBlockLocation.getLocation().add(newX, 0, newZ).getBlock().getType() == Material.SNOW)
						{
							final Block playerBlockLocationFire = playerBlockLocation.getLocation().add(newX, 0, newZ).getBlock();
							playerBlockLocationFire.setType(Material.FIRE);
							plugin.getServer().getScheduler().runTaskLater(plugin, new BukkitRunnable()
							{
								@Override
								public void run() 
								{
									Chunk loadChunk = playerBlockLocationFire.getLocation().getChunk();
									playerBlockLocationFire.getWorld().loadChunk(loadChunk);
									playerBlockLocationFire.setType(Material.AIR);
								}
							}, 100);
						}	
					}
					timer = timer - 1;
				} else this.cancel();
			}
		}.runTaskTimer(plugin, 0, 1);
	}
}
