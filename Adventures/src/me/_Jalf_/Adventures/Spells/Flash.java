package me._Jalf_.Adventures.Spells;

import java.util.List;

import me._Jalf_.Adventures.PartyHandler;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

public class Flash 
{
	public static void flashSpell (Player player, double radius, int time)
	{
		Location playerLoc = player.getLocation();
		player.getWorld().strikeLightningEffect(playerLoc);
		player.getWorld().strikeLightningEffect(playerLoc.add(1, 0, 0));
		player.getWorld().strikeLightningEffect(playerLoc.add(0, 0, 1));
		player.getWorld().strikeLightningEffect(playerLoc.add(-1, 0, 0));
		player.getWorld().strikeLightningEffect(playerLoc.add(0, 0, -1));
		List<Entity> nearbyEntities = player.getNearbyEntities(radius, radius, radius);
		for (Entity entity : nearbyEntities)
		{							
			if (entity instanceof LivingEntity)
			{
				if (entity instanceof Player)
				{
					if (!PartyHandler.isPlayerPartOfAskingPlayersParty(player, ((Player) entity))) 
						((LivingEntity) entity).addPotionEffect(PotionEffectType.BLINDNESS.createEffect(time, 5));
				}
				else ((LivingEntity) entity).addPotionEffect(PotionEffectType.BLINDNESS.createEffect(time, 5));
			}
		}
	}
}
