package me._Jalf_.Adventures.Spells;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import me._Jalf_.Adventures.Main;
import me._Jalf_.Adventures.PartyHandler;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

public class FoulPlay 
{
	public static Main plugin;

	public FoulPlay(Main plugin)
	{
		FoulPlay.plugin = plugin;
	}
	@SuppressWarnings("deprecation")
	public static void foulPlaySpell (final Player player, int range, double radius, final int time, final int damage)
	{
		Block targetBlock = player.getTargetBlock(null, range);
		Location targetBlockLocation = targetBlock.getLocation();
		double radiusSquared = radius * radius;
		List<Entity> nearbyEntities = player.getNearbyEntities(150, 150, 150);
		List<Double> entitiesInside = new ArrayList<>();

		if (targetBlock != null)
		{
			if (targetBlock.getType() != Material.AIR)
			{
				for (Entity entity : nearbyEntities)
				{
					if (entity.getLocation().distanceSquared(targetBlockLocation) <= radiusSquared)
					{
						if (entity instanceof LivingEntity)
						{
							if (entity instanceof Player)
							{
								if (!PartyHandler.isPlayerPartOfAskingPlayersParty(player, ((Player) entity)))
									entitiesInside.add(entity.getLocation().distanceSquared(targetBlockLocation));
							}
							else entitiesInside.add(entity.getLocation().distanceSquared(targetBlockLocation));
						}
					}
				}
				if (!entitiesInside.isEmpty())
				{
					int indexValue = entitiesInside.indexOf(Collections.min(entitiesInside));
					double minValue = entitiesInside.get(indexValue);

					for (final Entity entity : nearbyEntities)
					{
						if (entity.getLocation().distanceSquared(targetBlockLocation) == minValue)
						{	
							if (entity instanceof LivingEntity)
							{
								float pitch = player.getLocation().getPitch();
								float yaw = player.getLocation().getYaw();
								Location tpLoc = new Location(player.getWorld(), entity.getLocation().getX(), entity.getLocation().getY(), entity.getLocation().getZ(), yaw, pitch);
								
								float pitchE = entity.getLocation().getPitch();
								float yawE = entity.getLocation().getYaw();
								Location tpLocE = new Location(entity.getWorld(), player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ(), yawE, pitchE);
								
								player.teleport(tpLoc, TeleportCause.PLUGIN);
								entity.teleport(tpLocE, TeleportCause.PLUGIN);
								((LivingEntity) entity).damage((double) damage);
								((LivingEntity) entity).addPotionEffect(PotionEffectType.BLINDNESS.createEffect(100, 4));
								
								plugin.getServer().getScheduler().runTaskLater(plugin, new BukkitRunnable()
								{
									@Override
									public void run() 
									{
										if (entity.getTicksLived() > time)
										{
											float pitch = player.getLocation().getPitch();
											float yaw = player.getLocation().getYaw();
											Location tpLoc = new Location(player.getWorld(), entity.getLocation().getX(), entity.getLocation().getY(), entity.getLocation().getZ(), yaw, pitch);
											
											float pitchE = entity.getLocation().getPitch();
											float yawE = entity.getLocation().getYaw();
											Location tpLocE = new Location(entity.getWorld(), player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ(), yawE, pitchE);
											
											player.teleport(tpLoc, TeleportCause.PLUGIN);
											entity.teleport(tpLocE, TeleportCause.PLUGIN);
											((LivingEntity) entity).damage((double) damage);
										}
									}
								}, time);
								break;
							}
						}
					}
				}
			}
		}
	}
}
