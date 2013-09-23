package me._Jalf_.Adventures.Spells;

import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

public class Haste 
{
	public static void hasteSpell(Player player, int potionStrength, int time) 
	{
		//Scoreboard Buff
		player.addPotionEffect(PotionEffectType.SPEED.createEffect(time, potionStrength));
	}
}
