package me._Jalf_.Adventures.Spells;

import me._Jalf_.Adventures.Main;

import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class ShotgunArrow 
{
	public static Main plugin;

	public ShotgunArrow(Main plugin)
	{
		ShotgunArrow.plugin = plugin;
	}
	public static void shotgunArrowSpell(Player player, final int strength, int count)
	{	
		double yaw = Math.toRadians(player.getLocation().getYaw()+90);
		double pitch = Math.toRadians(-player.getLocation().getPitch());
		for (int i = 0; i < count; i++)
		{
			Arrow arrow = player.launchProjectile(Arrow.class);

			if (i < count / 4)
			{
				yaw = Math.toRadians(player.getLocation().getYaw()+90-i);
				pitch = Math.toRadians(-player.getLocation().getPitch()+i);
			}
			else if (i < count / 2)
			{
				yaw = Math.toRadians(player.getLocation().getYaw()+90+(i-count/2));
				pitch = Math.toRadians(-player.getLocation().getPitch()+i);
			}
			else if (i < (count / 4) * 3)
			{
				yaw = Math.toRadians(player.getLocation().getYaw()+90-i+count/4*3);
				pitch = Math.toRadians(-player.getLocation().getPitch()-i+count/4*3);
			}
			else if (i < count)
			{
				yaw = Math.toRadians(player.getLocation().getYaw()+90+i-count/4*3);
				pitch = Math.toRadians(-player.getLocation().getPitch()-i+count/4*5);
			}
			double v = strength;

			double x = v*Math.cos(yaw)*Math.cos(pitch);
			double y = v*Math.sin(pitch);
			double z = v*Math.sin(yaw)*Math.cos(pitch);			

			arrow.setVelocity(new Vector(x,y,z));
		}
	}
}
