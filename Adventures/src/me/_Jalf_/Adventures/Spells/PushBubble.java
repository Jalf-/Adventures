package me._Jalf_.Adventures.Spells;

import java.util.List;

import me._Jalf_.Adventures.PartyHandler;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class PushBubble 
{
	public static void pushBubbleSpell (Player player, double radius)
	{
		Location playerLoc = player.getLocation();
		List<Entity> nearbyEntities = player.getNearbyEntities(radius, radius, radius);
		for (Entity entity : nearbyEntities)
		{							
			if (entity instanceof LivingEntity)
			{
				Location entityLoc = entity.getLocation();
				Vector entityVector = new Vector (entityLoc.getX()-playerLoc.getX(), entityLoc.getY()-playerLoc.getY(), entityLoc.getZ()-playerLoc.getZ());
				
				if (entity instanceof Player)
				{
					if (!PartyHandler.isPlayerPartOfAskingPlayersParty(player, ((Player) entity)))
					{
						if (entityLoc.distance(playerLoc) < radius/2) entityVector = new Vector ((entityLoc.getX()-playerLoc.getX())*2, entityLoc.getY()-playerLoc.getY(), (entityLoc.getZ()-playerLoc.getZ())*2);
						entity.setVelocity(entityVector);
					}
				}
				else
				{
					if (entityLoc.distance(playerLoc) < radius/2) entityVector = new Vector ((entityLoc.getX()-playerLoc.getX())*2, entityLoc.getY()-playerLoc.getY(), (entityLoc.getZ()-playerLoc.getZ())*2);
					entity.setVelocity(entityVector);
				}
			}
		}
	}
}
