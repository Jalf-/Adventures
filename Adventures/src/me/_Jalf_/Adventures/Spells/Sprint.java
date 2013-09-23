package me._Jalf_.Adventures.Spells;

import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

public class Sprint 
{
	public static void sprintSpell(Player player, int poTionStrength, int time) 
	{
		//Scoreboard Buff
		player.addPotionEffect(PotionEffectType.SPEED.createEffect(time, poTionStrength));
	}
}
