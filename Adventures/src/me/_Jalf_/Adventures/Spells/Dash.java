package me._Jalf_.Adventures.Spells;

import org.bukkit.entity.Player;

public class Dash 
{
	public static void dashSpell(Player player, int strength) 
	{
		player.setVelocity(player.getLocation().getDirection().normalize().multiply(strength));
	}
}