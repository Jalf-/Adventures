package me._Jalf_.Adventures.Spells;

import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

public class EyesClosed 
{
	public static void eyesClosed (Player player, int strength, int time)
	{
		player.addPotionEffect(PotionEffectType.BLINDNESS.createEffect(time*2, 4));
		player.addPotionEffect(PotionEffectType.DAMAGE_RESISTANCE.createEffect(time, strength-1));
		player.addPotionEffect(PotionEffectType.FIRE_RESISTANCE.createEffect(time, strength-1));
		player.addPotionEffect(PotionEffectType.SLOW.createEffect(time, strength-1));
		player.addPotionEffect(PotionEffectType.WEAKNESS.createEffect(time, strength-1));
		player.addPotionEffect(PotionEffectType.REGENERATION.createEffect(time, strength-1));
		player.addPotionEffect(PotionEffectType.ABSORPTION.createEffect(time, strength-1));
	}
}
