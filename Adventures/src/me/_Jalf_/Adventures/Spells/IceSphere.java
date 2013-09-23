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

public class IceSphere 
{
	public static Main plugin;

	public IceSphere (Main plugin)
	{
		IceSphere.plugin = plugin;
	}
	public static void iceSphereSpell (final Player player, final int time, final double radiusValue)
	{
		new BukkitRunnable() 
		{
			List<Block> removeSphereBlocks = new ArrayList<Block>();
			int timer = time;
			@Override
			public void run()
			{	
				if (timer > 0)
				{		
					for (Block newRemoveSphereBlocks : removeSphereBlocks)
					{
						Chunk loadChunk = newRemoveSphereBlocks.getLocation().getChunk();
						newRemoveSphereBlocks.getWorld().loadChunk(loadChunk);

						newRemoveSphereBlocks.setType(Material.AIR);
					}
					Location playerLoc = player.getLocation();
					int radius = (int) Math.floor(radiusValue);
					List<Location> iceSphere = Methods.circle(playerLoc, radius, radius, true, true, 1);
					for (Location iceSphereLocations : iceSphere)
					{
						Block iceSphereBlock = iceSphereLocations.getBlock();
						if (iceSphereBlock.getType() == Material.AIR || iceSphereBlock.getType() == Material.SNOW)
						{
							iceSphereBlock.setType(Material.ICE);						 
							removeSphereBlocks.add(iceSphereBlock);
						}
					}
					timer = timer - 2;
				} 
				else
				{
					for (Block newRemoveSphereBlocks : removeSphereBlocks)
					{
						Chunk loadChunk = newRemoveSphereBlocks.getLocation().getChunk();
						newRemoveSphereBlocks.getWorld().loadChunk(loadChunk);

						newRemoveSphereBlocks.setType(Material.AIR);
					}
					this.cancel();
				} 
			}
		}.runTaskTimer(plugin, 0, 2);
	}
}
