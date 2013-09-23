package me._Jalf_.Adventures.Spells;

import me._Jalf_.Adventures.Main;

import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class Leap 
{
	public static Main plugin;
	
	public Leap (Main plugin)
	{
		Leap.plugin = plugin;
	}
	public static void leapSpell (final Player player, final int timeValue, final int strength)
	{
		if(player.getLocation().getBlock().getRelative(BlockFace.DOWN).getType() != Material.AIR)
		{
			double yaw  = ((player.getLocation().getYaw() + 90)  * Math.PI) / 180;
			Vector vector = new Vector(Math.cos(yaw), 1, Math.sin(yaw));
			player.setVelocity(vector);
			new BukkitRunnable() 
			{
				int time = 20;
				@Override
				public void run()
				{	
					if (time <= 0) this.cancel();
					else time--;
					
					if (player.getLocation().getBlock().getRelative(BlockFace.DOWN).getType() == Material.AIR)
					{					
						player.setFallDistance(0);
					} 
					else 
					{
						player.addPotionEffect(PotionEffectType.DAMAGE_RESISTANCE.createEffect(timeValue, strength));
						this.cancel();
					}
				}
			}.runTaskTimer(plugin, 10, 1);
		}
	}
}
