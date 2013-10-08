package me._Jalf_.Adventures.Spells;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import me._Jalf_.Adventures.Main;
import me._Jalf_.Adventures.Methods;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

public class Bombardment 
{
	public static Main plugin;

	public Bombardment(Main plugin)
	{
		Bombardment.plugin = plugin;
	}
	@SuppressWarnings("deprecation")
	public static void bombardmentSpell (Player player, int count, int range, double radiusValue, final int strength)
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

				int pigs = 0;

				final List<LivingEntity> pigsList = new ArrayList<>();

				for (Location listLoc : circleList)
				{
					if (pigs <= count)
					{
						LivingEntity pig = (LivingEntity) player.getWorld().spawnEntity(listLoc, EntityType.PIG);
						pig.addPotionEffect(PotionEffectType.DAMAGE_RESISTANCE.createEffect(30, 10));
						pigsList.add(pig); 
						pigs++;
					}
				}
				plugin.getServer().getScheduler().runTaskLater(plugin, new BukkitRunnable()
				{
					@Override
					public void run() 
					{
						for (LivingEntity e : pigsList)
						{
							Chunk loadChunk = e.getLocation().getChunk();
							e.getWorld().loadChunk(loadChunk);
							e.getWorld().playSound(e.getLocation(), Sound.PIG_IDLE, 10, 1);
							e.getWorld().playSound(e.getLocation(), Sound.PIG_IDLE, 10, 1);
							plugin.getServer().getScheduler().runTaskLater(plugin, new BukkitRunnable()
							{
								@Override
								public void run() 
								{
									for (LivingEntity e : pigsList)
									{
										e.setHealth(0.0);
										e.getWorld().createExplosion(e.getLocation().getX(), e.getLocation().getY(), e.getLocation().getZ(), strength, false, false);
									}
								}
							}, 20);
						}
					}
				}, 25);
			}
		}
	}
}