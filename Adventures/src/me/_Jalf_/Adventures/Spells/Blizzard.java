package me._Jalf_.Adventures.Spells;

import java.util.Collections;
import java.util.List;
import java.util.Random;

import me._Jalf_.Adventures.Main;
import me._Jalf_.Adventures.Methods;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class Blizzard 
{
	public static Main plugin;
	
	public Blizzard (Main plugin)
	{
		Blizzard.plugin = plugin;
	}
	
	@SuppressWarnings("deprecation")
	public static void blizzardSpell(final Player player, final int damage, final double radiusValue, int range, final int time, final int strength) 
	{
		Block targetBlock = player.getTargetBlock(null, range);
		final Location targetBlockLocation = targetBlock.getLocation();

		if (targetBlock != null)
		{
			if (targetBlock.getType() != Material.AIR)
			{
				new BukkitRunnable() 
				{
					int timer = time;
					@Override
					public void run()
					{			
						if (timer > 0)
						{
							int radius = (int) Math.floor(radiusValue);
							List<Location> snowCircle = Methods.circle(targetBlockLocation, radius, 1, false, false, 10);
							
							long seed = System.nanoTime();
							Collections.shuffle(snowCircle, new Random(seed));							
							int i = (int) Math.ceil(snowCircle.size() / (radius*2));
							for (Location loc : snowCircle)
							{
								if (i > 0)
								{
									Entity snowBall = loc.getWorld().spawnEntity(loc, EntityType.SNOWBALL);
									snowBall.setVelocity(new Vector(0, -0.5, 0));
									snowBall.setMetadata("IsProjectile", new FixedMetadataValue(plugin, true));
//									snowBall.setMetadata("Damage" + damage, new FixedMetadataValue(plugin, true));
//									snowBall.setMetadata("Slow" + strength, new FixedMetadataValue(plugin, true));
//									snowBall.setMetadata("Blindness1", new FixedMetadataValue(plugin, true));
									
									snowBall.setMetadata("Shooter", new FixedMetadataValue(plugin, player.getName()));
									snowBall.setMetadata("Damage", new FixedMetadataValue(plugin, damage));
									snowBall.setMetadata("Slow", new FixedMetadataValue(plugin, strength));
									snowBall.setMetadata("Blindness", new FixedMetadataValue(plugin, 1));
									i--;
								}else break;
							}
							timer = timer - 2;
						} else this.cancel();
					}
				}.runTaskTimer(plugin, 0, 2);				
			}
		}
	}
}
