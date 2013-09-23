package me._Jalf_.Adventures.Spells;

import java.util.List;

import me._Jalf_.Adventures.Main;
import me._Jalf_.Adventures.Methods;

import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class LightningShield 
{
public static Main plugin;
	
	public LightningShield (Main plugin)
	{
		LightningShield.plugin = plugin;
	}
	public static void lightningShieldSpell(final Player player, final int damage, final double radiusValue, final int time) 
	{
		new BukkitRunnable() 
		{
			int timer = time;
			@Override
			public void run()
			{			
				if (timer > 0)
				{
					List<Entity> nearbyEntities = player.getNearbyEntities(150, 150, 150);
					double radiusSquared = radiusValue * radiusValue;
					int radius = (int) Math.floor(radiusValue);
					List<Location> circle = Methods.circle(player.getLocation(), radius, 1, true, false, 0);
					for (Location loc : circle)
					{
						player.getWorld().playEffect(loc, Effect.MOBSPAWNER_FLAMES, 0);
					}
					for (Entity entity : nearbyEntities)
					{
						if (entity.getLocation().distanceSquared(player.getLocation()) <= radiusSquared)
						{	
							if (entity instanceof LivingEntity)
							{
								((LivingEntity) entity).damage((double) damage);
								player.getWorld().strikeLightningEffect(entity.getLocation());
							}
						}
					}
					timer = timer - 40;
				} else this.cancel();
			}
		}.runTaskTimer(plugin, 0, 40);				
	}
}
