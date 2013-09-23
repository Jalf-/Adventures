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

public class Meteor 
{
	public static Main plugin;

	public Meteor (Main plugin)
	{
		Meteor.plugin = plugin;
	}
	@SuppressWarnings("deprecation")
	public static void meteorSpell (final Player player, final int strength, final double radiusValue, int range)
	{
		Block targetBlock = player.getTargetBlock(null, range);
		final Location targetBlockLocation = targetBlock.getLocation();

		if (targetBlock != null)
		{
			if (targetBlock.getType() != Material.AIR)
			{
				new BukkitRunnable() 
				{
					int y = 0;
					List<Block> removeStone = new ArrayList<>();
					@Override
					public void run()
					{				
						if (!removeStone.isEmpty())
						{
							for (Block removeBlock : removeStone)
							{
								Chunk loadChunk = removeBlock.getChunk();
								removeBlock.getWorld().loadChunk(loadChunk);
								removeBlock.setType(Material.AIR);
							}
						}					
						int radius = (int) Math.floor(radiusValue);
						boolean explode = false;
						List<Location> stoneList = Methods.circle(targetBlockLocation, radius, radius, false, true, 15 - y);
						List<Location> impactLocation = new ArrayList<>();
						
						for (Location stoneBlock : stoneList)
						{
							Chunk loadChunk = stoneBlock.getChunk();
							stoneBlock.getWorld().loadChunk(loadChunk);
							if (stoneBlock.getBlock().getType() == Material.AIR)
							{
								stoneBlock.getBlock().setType(Material.STONE);
								removeStone.add(stoneBlock.getBlock());
							}
							else
							{
								impactLocation.add(stoneBlock);
								explode = true;
							}
						}
						if (explode == false)
						{
							for (Location stoneBlock : stoneList)
							{
								if (stoneBlock.clone().add(0, 1, 0).getBlock().getType() == Material.AIR)
								{
									stoneBlock.clone().add(0, 1, 0).getBlock().setType(Material.FIRE);
									removeStone.add(stoneBlock.clone().add(0, 1, 0).getBlock());
								}
							}
						}	
						if (explode == true)
						{
							for (Block removeBlock : removeStone)
							{
								removeBlock.setType(Material.AIR);
							}
							for (Location explodeLoc : impactLocation)
							{
								player.getWorld().createExplosion(targetBlockLocation.getX(), explodeLoc.getY(), targetBlockLocation.getZ(), strength, false, false);
								player.getWorld().createExplosion(targetBlockLocation.getX()+1, explodeLoc.getY(), targetBlockLocation.getZ(), strength-1, false, false);
								player.getWorld().createExplosion(targetBlockLocation.getX(), explodeLoc.getY(), targetBlockLocation.getZ()+1, strength-1, false, false);
								player.getWorld().createExplosion(targetBlockLocation.getX()-1, explodeLoc.getY(), targetBlockLocation.getZ(), strength-1, false, false);
								player.getWorld().createExplosion(targetBlockLocation.getX(), explodeLoc.getY(), targetBlockLocation.getZ()-1, strength-1, false, false);
							}
							this.cancel();
						}
						y++;
					}
				}.runTaskTimer(plugin, 0, 4);				
			}
		}
	}
}
