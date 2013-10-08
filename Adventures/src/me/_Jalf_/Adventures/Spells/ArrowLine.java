package me._Jalf_.Adventures.Spells;

import me._Jalf_.Adventures.Main;

import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class ArrowLine 
{
	public static Main plugin;
	
	public ArrowLine (Main plugin)
	{
		ArrowLine.plugin = plugin;
	}
	public static void arrowLineSpell (final Player player, final int count)
	{
		new BukkitRunnable() 
		{
			int i = 0;
			@Override
			public void run() 
			{
				Arrow arrow = player.launchProjectile(Arrow.class);
				Arrow arrow2 = player.launchProjectile(Arrow.class);

				double yaw = Math.toRadians(player.getLocation().getYaw()+90-i*2);
				double yaw2 = Math.toRadians(player.getLocation().getYaw()+90+i*2);
				double pitch = Math.toRadians(-player.getLocation().getPitch());

				double v = 2.0;

				double x = v*Math.cos(yaw)*Math.cos(pitch);
				double y = v*Math.sin(pitch);
				double z = v*Math.sin(yaw)*Math.cos(pitch);	
				
				double x2 = v*Math.cos(yaw2)*Math.cos(pitch);
				double y2 = v*Math.sin(pitch);
				double z2 = v*Math.sin(yaw2)*Math.cos(pitch);	

				arrow.setVelocity(new Vector(x,y,z));
				arrow2.setVelocity(new Vector(x2,y2,z2));

				arrow.setMetadata("Shooter", new FixedMetadataValue(plugin, player.getName()));
				arrow2.setMetadata("Shooter", new FixedMetadataValue(plugin, player.getName()));
				
				i++;
				if (i >= count / 2) this.cancel();
			}
		}.runTaskTimer(plugin, 0, 1);
	}
}
