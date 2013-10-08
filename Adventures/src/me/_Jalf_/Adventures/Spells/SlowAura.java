package me._Jalf_.Adventures.Spells;

import java.util.List;

import me._Jalf_.Adventures.Main;
import me._Jalf_.Adventures.PartyHandler;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

public class SlowAura 
{
	public static Main plugin;

	public SlowAura(Main plugin)
	{
		SlowAura.plugin = plugin;
	}
	public static void slowAuraSpell(final Player player, final int potionStrength, final double radius, final int time) 
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
								if (!PartyHandler.isPlayerPartOfAskingPlayersParty(player, ((Player) entity)))
									((LivingEntity) entity).addPotionEffect(PotionEffectType.SLOW.createEffect(60, potionStrength));
							}
							else ((LivingEntity) entity).addPotionEffect(PotionEffectType.SLOW.createEffect(60, potionStrength));
						}
					}
					timer = timer - 10;
				} else this.cancel();
			}
		}.runTaskTimer(plugin, 0, 10);
	}
}

