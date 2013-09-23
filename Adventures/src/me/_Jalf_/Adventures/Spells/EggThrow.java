package me._Jalf_.Adventures.Spells;

import me._Jalf_.Adventures.Main;

import org.bukkit.block.Block;
import org.bukkit.entity.Egg;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.metadata.FixedMetadataValue;

public class EggThrow 
{
	public static Main plugin;

	public EggThrow(Main plugin) 
	{
		EggThrow.plugin = plugin;
	}
	@SuppressWarnings("deprecation")
	public static void eggThrowSpell(Player player, int damage, int strength, int range) 
	{
		Block targetBlock = player.getTargetBlock(null, range);
		
		if (targetBlock != null)
		{
			Projectile egg = player.launchProjectile(Egg.class);
			egg.setMetadata("Damage" + damage, new FixedMetadataValue(plugin, true));
			egg.setMetadata("Blindness" + strength, new FixedMetadataValue(plugin, true));
			egg.setMetadata("Confussion" + strength, new FixedMetadataValue(plugin, true));
			egg.setMetadata("Weakness" + strength, new FixedMetadataValue(plugin, true));
		}
	}
}
