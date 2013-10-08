package me._Jalf_.Adventures.Spells;

import java.util.List;

import me._Jalf_.Adventures.Main;
import me._Jalf_.Adventures.Methods;
import me._Jalf_.Adventures.PartyHandler;

import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class LightningStorm 
{
	public static Main plugin;
	
	public LightningStorm (Main plugin)
	{
		LightningStorm.plugin = plugin;
	}
	@SuppressWarnings("deprecation")
	public static void lightningStormSpell(final Player player, final int damage, final double radiusValue, int range, final int time) 
	{
		Block targetBlock = player.getTargetBlock(null, range);
		final Location targetBlockLocation = targetBlock.getLocation();

		if (targetBlock != null)
		{
			if (targetBlock.getType() != Material.AIR)
			{
				new BukkitRunnable() 
				{
					int timer = time;
					@Override
					public void run()
					{			
						if (timer > 0)
						{
							List<Entity> nearbyEntities = player.getNearbyEntities(150, 150, 150);
							double radiusSquared = radiusValue * radiusValue;
							int radius = (int) Math.floor(radiusValue);
							List<Location> circle = Methods.circle(targetBlockLocation, radius, 1, true, false, 5);
							for (Location loc : circle)
							{
								player.getWorld().playEffect(loc, Effect.MOBSPAWNER_FLAMES, 0);
							}
							for (Entity entity : nearbyEntities)
							{
								if (entity.getLocation().distanceSquared(targetBlockLocation) <= radiusSquared)
								{	
									if (entity instanceof LivingEntity)
									{
										if (entity instanceof Player)
										{
											if (!PartyHandler.isPlayerPartOfAskingPlayersParty(player, ((Player) entity)))
											{
												((LivingEntity) entity).damage(damage);
												player.getWorld().strikeLightningEffect(entity.getLocation());
											}
										}
										else
										{
											((LivingEntity) entity).damage(damage);
											player.getWorld().strikeLightningEffect(entity.getLocation());
										}
									}
								}
							}
							timer = timer - 40;
						} else this.cancel();
					}
				}.runTaskTimer(plugin, 0, 40);				
			}
		}
	}
}
