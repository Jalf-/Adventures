package me._Jalf_.Adventures.Spells;

import java.util.List;

import me._Jalf_.Adventures.Main;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

public class SmokeScreen 
{
	public static Main plugin;

	public SmokeScreen(Main plugin)
	{
		SmokeScreen.plugin = plugin;
	}
	public static void smokeScreenSpell (final Player player, final double radius, final int time)
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
							if (entity instanceof Player)
							{
//								((Player) entity).getScoreboard().     Debuff
								((LivingEntity) entity).addPotionEffect(PotionEffectType.BLINDNESS.createEffect(60, 5));
							}
							else
							{
								((LivingEntity) entity).addPotionEffect(PotionEffectType.BLINDNESS.createEffect(60, 5));
							}
							Location entityLocation = entity.getLocation();
							entityLocation.getWorld().createExplosion(entityLocation.add(0, 1, 0), 0.0F);
							entityLocation.getWorld().createExplosion(entityLocation.add(1, 0, 0), 0.0F);
							entityLocation.getWorld().createExplosion(entityLocation.add(-2, 0, 0), 0.0F);
							entityLocation.getWorld().createExplosion(entityLocation.add(1, 0, 1), 0.0F);
							entityLocation.getWorld().createExplosion(entityLocation.add(0, 0, -2), 0.0F);
							entityLocation.getWorld().createExplosion(entityLocation.add(0, 1, 1), 0.0F);
						}
					}
					timer = timer - 10;
				} else this.cancel();
			}
		}.runTaskTimer(plugin, 0, 10);
	}
}
