package me._Jalf_.Adventures.Spells;

import me._Jalf_.Adventures.Main;

import org.bukkit.Location;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class ExplodingArrow 
{
	public static Main plugin;

	public ExplodingArrow(Main plugin)
	{
		ExplodingArrow.plugin = plugin;
	}
	public static void explodingArrowSpell(Player player, int strength) 
	{
		Arrow arrow = player.launchProjectile(Arrow.class);
		Location arrowLocation = arrow.getLocation();
		Vector arrowVelocity = arrow.getVelocity();
		arrow.remove();

		final TNTPrimed tnt = player.getWorld().spawn(arrowLocation, TNTPrimed.class);
		tnt.setVelocity(arrowVelocity);
		tnt.setFuseTicks(200);
		tnt.setYield(strength);

		new BukkitRunnable() 
		{
			@Override
			public void run() 
			{
				if (tnt.isOnGround())
				{
					tnt.setFuseTicks(0);
					this.cancel();
				}
			}
		}.runTaskTimer(plugin, 0, 4);
	}
}
