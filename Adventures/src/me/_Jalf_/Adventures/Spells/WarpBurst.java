package me._Jalf_.Adventures.Spells;

import me._Jalf_.Adventures.Main;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.scheduler.BukkitRunnable;

public class WarpBurst 
{
	public static Main plugin;

	public WarpBurst(Main plugin)
	{
		WarpBurst.plugin = plugin;
	}
	@SuppressWarnings("deprecation")
	public static void warpBurstSpell (final Player player, final int strength, int range)
	{
		Block targetBlock = player.getTargetBlock(null, range);
		Location targetBlockLocation = targetBlock.getLocation();
		final Location playerLocation = player.getLocation();
		
		if (targetBlock != null)
		{
			if (targetBlock.getType() != Material.AIR)
			{
				if (targetBlockLocation.clone().add(0, 1, 0).getBlock().getType() == Material.AIR)
				{
					if (targetBlockLocation.clone().add(0, 2, 0).getBlock().getType() == Material.AIR)
					{
						final float pitch = player.getLocation().getPitch();
						final float yaw = player.getLocation().getYaw();
						Location tpLoc = new Location(player.getWorld(), targetBlockLocation.getX(), targetBlockLocation.getY()+1, targetBlockLocation.getZ(), yaw, pitch);
						player.teleport(tpLoc, TeleportCause.PLUGIN);
						plugin.getServer().getScheduler().runTaskLater(plugin, new BukkitRunnable()
						{
							@Override
							public void run() 
							{
								player.getWorld().createExplosion(playerLocation.getX(), playerLocation.getY(), playerLocation.getZ(), strength, false, false);
							}
						}, 2);
					}
				}
			}
		}
	}
}
