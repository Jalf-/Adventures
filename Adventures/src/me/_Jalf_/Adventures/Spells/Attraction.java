package me._Jalf_.Adventures.Spells;

import java.util.List;

import me._Jalf_.Adventures.PartyHandler;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class Attraction 
{
	public static void attractionSpell (Player player, double radius)
	{
		Location playerLoc = player.getLocation();
		List<Entity> nearbyEntities = player.getNearbyEntities(radius, radius, radius);
		for (Entity entity : nearbyEntities)
		{							
			if (entity instanceof LivingEntity)
			{
				Location entityLoc = entity.getLocation();
				Vector entityVector = new Vector ((playerLoc.getX()-entityLoc.getX())/2, playerLoc.getY()-entityLoc.getY(), (playerLoc.getZ()-entityLoc.getZ())/2);
				if (entity instanceof Player)
				{
					if (!PartyHandler.isPlayerPartOfAskingPlayersParty(player, ((Player) entity))) entity.setVelocity(entityVector);
				}
				else entity.setVelocity(entityVector);				
			}
		}
	}
}
