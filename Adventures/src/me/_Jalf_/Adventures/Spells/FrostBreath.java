package me._Jalf_.Adventures.Spells;

import java.util.List;

import me._Jalf_.Adventures.FireworkEffectPlayer;
import me._Jalf_.Adventures.Methods;

import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

public class FrostBreath 
{
	public static void frostBreathSpell (Player player, double radius, int angle, int strength)
	{
		Location playerLoc = player.getLocation();
		List<Entity> nearbyEntities = player.getNearbyEntities(50, 50, 50);
		List<Entity> entities = Methods.getEntitiesInCone(nearbyEntities, playerLoc.toVector(),(float) radius, angle, playerLoc.getDirection());
		for (Entity e : entities)
		{
			if (e instanceof LivingEntity)
			{
				((LivingEntity) e).addPotionEffect(PotionEffectType.SLOW.createEffect(50*strength, strength));	
				try {
					FireworkEffectPlayer.playFirework(e.getLocation().getWorld(), e.getLocation(), FireworkEffect.builder().with(Type.BURST).withColor(Color.NAVY).build());
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		}
	}
}
