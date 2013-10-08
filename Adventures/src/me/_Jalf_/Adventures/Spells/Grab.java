package me._Jalf_.Adventures.Spells;

import java.util.List;

import me._Jalf_.Adventures.Methods;
import me._Jalf_.Adventures.PartyHandler;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class Grab 
{
	public static void grabSpell (Player player, double radius, int angle)
	{
		Location playerLoc = player.getLocation();
		List<Entity> nearbyEntities = player.getNearbyEntities(50, 50, 50);
		List<Entity> entities = Methods.getEntitiesInCone(nearbyEntities, playerLoc.toVector(),(float) radius, angle, playerLoc.getDirection());
		for (Entity entity : entities)
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
