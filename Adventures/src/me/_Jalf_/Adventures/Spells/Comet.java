package me._Jalf_.Adventures.Spells;

import java.util.ArrayList;
import java.util.List;

import me._Jalf_.Adventures.Main;
import me._Jalf_.Adventures.Methods;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

public class Comet 
{
	public static Main plugin;
	
	public Comet (Main plugin)
	{
		Comet.plugin = plugin;
	}
	@SuppressWarnings("deprecation")
	public static void cometSpell (final Player player, final int strength, final double radiusValue, int range)
	{
		Block targetBlock = player.getTargetBlock(null, range);
		final Location targetBlockLocation = targetBlock.getLocation();
		final double radiusSquared = radiusValue * radiusValue;

		if (targetBlock != null)
		{
			if (targetBlock.getType() != Material.AIR)
			{
				new BukkitRunnable() 
				{
					int y = 0;
					List<Block> removeIce = new ArrayList<>();
					@Override
					public void run()
					{				
						if (!removeIce.isEmpty())
						{
							for (Block removeBlock : removeIce)
							{
								Chunk loadChunk = removeBlock.getChunk();
								removeBlock.getWorld().loadChunk(loadChunk);
								removeBlock.setType(Material.AIR);
							}
						}					
						int radius = (int) Math.floor(radiusValue);
						boolean explode = false;
						List<Location> iceList = Methods.circle(targetBlockLocation, radius, radius, false, true, 15 - y);
						Location impactLocation = null;
						
						for (Location iceBlock : iceList)
						{
							Chunk loadChunk = iceBlock.getChunk();
							iceBlock.getWorld().loadChunk(loadChunk);
							if (iceBlock.getBlock().getType() == Material.AIR)
							{
								iceBlock.getBlock().setType(Material.ICE);
								removeIce.add(iceBlock.getBlock());
							}
							else
							{
								impactLocation = iceBlock;
								explode = true;
								break;
							}
						}	
						if (explode == true)
						{
							for (Block removeBlock : removeIce)
							{
								removeBlock.setType(Material.AIR);
							}
							Location centerImpact = new Location(player.getWorld(), targetBlockLocation.getX(), impactLocation.getY(), targetBlockLocation.getZ());
							
							player.getWorld().createExplosion(targetBlockLocation.getX(), impactLocation.getY(), targetBlockLocation.getZ(), strength, false, false);
							player.getWorld().createExplosion(targetBlockLocation.getX()+1, impactLocation.getY(), targetBlockLocation.getZ(), strength-1, false, false);
							player.getWorld().createExplosion(targetBlockLocation.getX(), impactLocation.getY(), targetBlockLocation.getZ()+1, strength-1, false, false);
							player.getWorld().createExplosion(targetBlockLocation.getX()-1, impactLocation.getY(), targetBlockLocation.getZ(), strength-1, false, false);
							player.getWorld().createExplosion(targetBlockLocation.getX(), impactLocation.getY(), targetBlockLocation.getZ()-1, strength-1, false, false);
							
							List<Entity> nearbyEntities = player.getNearbyEntities(150, 150, 150);
							
							for (Entity entity : nearbyEntities)
							{
								if (entity.getLocation().distanceSquared(centerImpact) <= radiusSquared * 1.5)
								{	
									if (entity instanceof LivingEntity)
									{
//										if (entity instanceof Player)
//										{
//											check if player is in party
//										}
										int amplifier = (int) Math.floor(strength / 2);
										((LivingEntity) entity).addPotionEffect(PotionEffectType.SLOW.createEffect(strength * 20, amplifier));
									}
								}
							}
							this.cancel();
						}
						y++;
					}
				}.runTaskTimer(plugin, 0, 4);				
			}
		}
	}
}
