package me._Jalf_.Adventures.Spells;

import java.util.List;

import me._Jalf_.Adventures.Main;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class PushAura 
{
	public static Main plugin;
	
	public PushAura (Main plugin)
	{
		PushAura.plugin = plugin;
	}
	public static void pushBubbleSpell (final Player player, final double radius, final int time)
	{
		new BukkitRunnable() 
		{
			int timer = time;
			@Override
			public void run() 
			{
				if (timer > 0)
				{
					Location playerLoc = player.getLocation();
					List<Entity> nearbyEntities = player.getNearbyEntities(radius, radius, radius);
					for (Entity entity : nearbyEntities)
					{							
						if (entity instanceof LivingEntity)
						{
							Location entityLoc = entity.getLocation();
							Vector entityVector = new Vector (entityLoc.getX()-playerLoc.getX(), entityLoc.getY()-playerLoc.getY(), entityLoc.getZ()-playerLoc.getZ());
							if (entityLoc.distance(playerLoc) < radius/2) entityVector = new Vector ((entityLoc.getX()-playerLoc.getX())*2, entityLoc.getY()-playerLoc.getY(), (entityLoc.getZ()-playerLoc.getZ())*2);
							entity.setVelocity(entityVector);
						}
					}
					timer = timer - 2;
				} else this.cancel();
			}
		}.runTaskTimer(plugin, 0, 2);
	}
}
