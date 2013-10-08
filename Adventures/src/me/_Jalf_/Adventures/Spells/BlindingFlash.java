package me._Jalf_.Adventures.Spells;

import java.util.List;

import me._Jalf_.Adventures.FireworkEffectPlayer;
import me._Jalf_.Adventures.Main;
import me._Jalf_.Adventures.PartyHandler;

import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

public class BlindingFlash 
{
	public static Main plugin;
	
	public BlindingFlash (Main plugin)
	{
		BlindingFlash.plugin = plugin;
	}
	
	@SuppressWarnings("deprecation")
	public static void blindingFlashSpell(Player player, double radius, int range, int time) 
	{
		Block targetBlock = player.getTargetBlock(null, range);
		Location targetBlockLocation = targetBlock.getLocation();
		double radiusSquared = radius * radius;
		List<Entity> nearbyEntities = player.getNearbyEntities(150, 150, 150);

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
								{
									((LivingEntity) entity).addPotionEffect(PotionEffectType.BLINDNESS.createEffect(time, 5));
									try {
										FireworkEffectPlayer.playFirework(entity.getLocation().getWorld(), entity.getLocation(), FireworkEffect.builder().with(Type.BURST).withColor(Color.YELLOW).build());
									} catch (Exception e1) {
										e1.printStackTrace();
									}
								}							
							}
							else
							{
								try {
									FireworkEffectPlayer.playFirework(entity.getLocation().getWorld(), entity.getLocation(), FireworkEffect.builder().with(Type.BURST).withColor(Color.YELLOW).build());
								} catch (Exception e1) {
									e1.printStackTrace();
								}
								((LivingEntity) entity).addPotionEffect(PotionEffectType.SLOW.createEffect(time, 2));
							}
						}
					}
				}				
			}
		}
	}
}
