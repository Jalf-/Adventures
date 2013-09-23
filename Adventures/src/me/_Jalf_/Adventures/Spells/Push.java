package me._Jalf_.Adventures.Spells;

import java.util.List;

import me._Jalf_.Adventures.Methods;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class Push 
{
	public static void pushSpell (Player player, double radius, int angle)
	{
		Location playerLoc = player.getLocation();
		List<Entity> nearbyEntities = player.getNearbyEntities(50, 50, 50);
		List<Entity> entities = Methods.getEntitiesInCone(nearbyEntities, playerLoc.toVector(),(float) radius, angle, playerLoc.getDirection());
		for (Entity e : entities)
		{
			if (e instanceof LivingEntity)
			{
				Location entityLoc = e.getLocation();
				Vector entityVector = new Vector (entityLoc.getX()-playerLoc.getX(), entityLoc.getY()-playerLoc.getY(), entityLoc.getZ()-playerLoc.getZ());
				if (entityLoc.distance(playerLoc) < radius/2) entityVector = new Vector ((entityLoc.getX()-playerLoc.getX())*2, entityLoc.getY()-playerLoc.getY(), (entityLoc.getZ()-playerLoc.getZ())*2);
				e.setVelocity(entityVector);
			}
		}
	}
}
