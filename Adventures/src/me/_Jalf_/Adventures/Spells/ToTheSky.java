package me._Jalf_.Adventures.Spells;

import java.util.List;

import me._Jalf_.Adventures.PartyHandler;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class ToTheSky 
{
	public static void toTheSkySpell (Player player, int strength, double radius)
	{
		List<Entity> nearbyEntities = player.getNearbyEntities(radius, radius, radius);
		for (Entity entity : nearbyEntities)
		{							
			if (entity instanceof LivingEntity)
			{
				if (entity instanceof Player)
				{
					if (!PartyHandler.isPlayerPartOfAskingPlayersParty(player, ((Player) entity))) entity.setVelocity(new Vector(0, strength, 0));
				}
				else entity.setVelocity(new Vector(0, strength, 0));
			}
		}
	}
}
