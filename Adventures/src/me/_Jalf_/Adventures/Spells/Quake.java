package me._Jalf_.Adventures.Spells;

import java.util.List;

import me._Jalf_.Adventures.Main;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class Quake 
{
	public static Main plugin;
	
	public Quake(Main plugin)
	{
		Quake.plugin = plugin;
	}
	public static void quakeSpell (final Player player,final  int strength,final double radius, final int time)
	{
		new BukkitRunnable() 
		{
			int timer = time;
			@Override
			public void run()
			{			
				if (timer > 0)
				{		
					List<Entity> nearbyEntities = player.getNearbyEntities(radius, radius, radius);
					for (Entity entity : nearbyEntities)
					{							
						if (entity instanceof LivingEntity)
						{
							entity.setVelocity(new Vector(0, 0.5, 0));
						}
					}
					timer-=5;
				} else this.cancel();
			}
		}.runTaskTimer(plugin, 0, 10);
	}
}
