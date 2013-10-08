package me._Jalf_.Adventures.Spells;

import java.util.List;

import me._Jalf_.Adventures.Main;
import me._Jalf_.Adventures.PartyHandler;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class HiddenMine 
{
	public static Main plugin;

	public HiddenMine (Main plugin)
	{
		HiddenMine.plugin = plugin;
	}
	public static void hiddenMineSpell (final Player player, final int time, final double radius, final int strength, final int delay)
	{
		final Location playerLoc = player.getLocation();
		final double radiusSquared = radius * radius;
		if (playerLoc.getBlock().getType() == Material.AIR || playerLoc.getBlock().getType() == Material.SNOW)
		{
			new BukkitRunnable() 
			{
				int timer = time;
				@Override
				public void run()
				{			
					if (timer > 0)
					{
						Chunk loadChunk = playerLoc.getChunk();
						playerLoc.getWorld().loadChunk(loadChunk);
						if (playerLoc.getBlock().getType() == Material.AIR || playerLoc.getBlock().getType() == Material.SNOW)
						{
							playerLoc.getBlock().setType(Material.TNT);
							plugin.getServer().getScheduler().runTaskLater(plugin, new BukkitRunnable()
							{
								@Override
								public void run() 
								{
									playerLoc.getBlock().setType(Material.AIR);
								}
							}, 2);
						}
						List<Entity> nearbyEntities = player.getNearbyEntities(150, 150, 150);
						for (Entity entity : nearbyEntities)
						{
							if (entity.getLocation().distanceSquared(playerLoc) <= radiusSquared)
							{	
								if (entity instanceof LivingEntity)
								{
									if (entity instanceof Player)
									{
										if (!PartyHandler.isPlayerPartOfAskingPlayersParty(player, ((Player) entity)))
										{
											player.getWorld().createExplosion(playerLoc.getX(), playerLoc.getY(), playerLoc.getZ(), strength, false, false);
											playerLoc.getBlock().setType(Material.AIR);
											this.cancel();
											break;
										}
									}
									else 
									{
										player.getWorld().createExplosion(playerLoc.getX(), playerLoc.getY(), playerLoc.getZ(), strength, false, false);
										playerLoc.getBlock().setType(Material.AIR);
										this.cancel();
										break;
									}
								}
							}
						}
						timer = timer - 10;
					} else this.cancel();
				}
			}.runTaskTimer(plugin, delay, 10);			
		}
	}
}
