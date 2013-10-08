package me._Jalf_.Adventures.Spells;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import me._Jalf_.Adventures.Main;
import me._Jalf_.Adventures.Methods;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.util.Vector;

public class ArrowRain 
{
	public static Main plugin;

	public ArrowRain(Main plugin)
	{
		ArrowRain.plugin = plugin;
	}
	@SuppressWarnings("deprecation")
	public static void arrowRainSpell (Player player, int count, int range, double radiusValue)
	{
		Block targetBlock = player.getTargetBlock(null, range);
		Location targetBlockLocation = targetBlock.getLocation();

		if (targetBlock != null)
		{
			if (targetBlock.getType() != Material.AIR)
			{
				int radius = (int) Math.floor(radiusValue);

				List<Location> circleList = Methods.circle(targetBlockLocation, radius, 1, false, false, 15);

				long seed = System.nanoTime();
				Collections.shuffle(circleList, new Random(seed));

				int arrows = 0;

				final List<Arrow> arrowsList = new ArrayList<>();

				for (Location listLoc : circleList)
				{
					if (arrows <= count)
					{
						Vector velocity = new Vector(0, -1, 0);
						float speed = 0.6f;
						float spread = 12f;
						Arrow arrow = player.getWorld().spawnArrow(listLoc, velocity, speed, spread);
						arrow.setShooter(player);
						arrow.setBounce(false);
						arrow.setMetadata("Shooter", new FixedMetadataValue(plugin, player.getName()));

						arrowsList.add(arrow); 
						arrows++;
					}
				}			
			}
		}
	}
}
