package me._Jalf_.Adventures;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scoreboard.DisplaySlot;

public class PlayerResource implements Listener
{
	public static Main plugin;

	public PlayerResource(Main plugin)
	{
		PlayerResource.plugin = plugin;
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}
	
	public static ScoreboardHandler scoreboard;

	public PlayerResource(ScoreboardHandler scoreboard)
	{
		PlayerResource.scoreboard = scoreboard;
	}
	// Variable over players that stops regenerating in next regen loop
	public static List<String> playerNoResource = new ArrayList<>();
	
	@EventHandler
	public void givePlayerResource(PlayerJoinEvent event) 
	{
		Player player = event.getPlayer();
		if (plugin.getSaves().isSet(player.getName() + ".Class"))
		{
			if (plugin.getClasses().contains(plugin.getSaves().getString(player.getName() + ".Class")) && 
					!plugin.getSaves().getString(player.getName() + ".Class").equalsIgnoreCase("Default"))
			{
				String resourceName = Methods.getPlayerResourceName(player.getName());
				ScoreboardHandler.registerBoard(player.getName(), resourceName);
				plugin.getServer().getScheduler().runTaskTimer(plugin, new ResourceRegenTask(player, plugin), 0, 20);
			}
		}
	}
	
	@EventHandler
	public void savePlayerResource(PlayerQuitEvent event)
	{
		Player player = event.getPlayer();
		if (!plugin.getSaves().getString(player.getName() + ".Class").equalsIgnoreCase("Default"))
		{
			String resourceName = Methods.getPlayerResourceName(player.getName());      
			int resourceNow = ScoreboardHandler.getScore(player.getName(), DisplaySlot.SIDEBAR, Bukkit.getOfflinePlayer(resourceName)); 
			
//			if (resourceNow != 0)
//			{
				plugin.getSaves().set(player.getName() + ".Resource." + resourceName + ".Now", resourceNow);
				plugin.saveSaves();
//			}
		}
	}
	
	@EventHandler
	public void giveFuryResource(EntityDamageByEntityEvent event)
	{
		Entity damager = event.getDamager();
		Entity entity = event.getEntity();

		if (entity instanceof Player)
		{
			Player player = ((Player) entity).getPlayer();
			if (plugin.getSaves().isSet(player.getName() + ".Resource.Fury.Max"))
			{
				int resourceMax = plugin.getSaves().getInt(player.getName() + ".Resource.Fury.Max");
				if (resourceMax > 0)
				{
					int resourceNow = ScoreboardHandler.getScore(player.getName(), DisplaySlot.SIDEBAR, Bukkit.getOfflinePlayer("Fury"));

					if (resourceNow != resourceMax)
					{
						resourceNow = resourceNow + plugin.getConfig().getInt("Resource.Fury.Regen");

						if (resourceNow > resourceMax)
						{
							resourceNow = resourceMax;
						}
					}        
					ScoreboardHandler.changeBoardValue(player.getName(), DisplaySlot.SIDEBAR, Bukkit.getOfflinePlayer("Fury"), resourceNow, "Fury");
				}
			}
		}
		if (damager instanceof Player)
		{
			Player player = ((Player) damager).getPlayer();
			if (plugin.getSaves().isSet(player.getName() + ".Resource.Fury.Max"))
			{
				int resourceMax = plugin.getSaves().getInt(player.getName() + ".Resource.Fury.Max");
				if (resourceMax > 0)
				{
					int resourceNow = ScoreboardHandler.getScore(player.getName(), DisplaySlot.SIDEBAR, Bukkit.getOfflinePlayer("Fury"));

					if (resourceNow != resourceMax)
					{
						resourceNow = resourceNow + plugin.getConfig().getInt("Resource.Fury.Regen");

						if (resourceNow > resourceMax)
						{
							resourceNow = resourceMax;
						}
					}
					ScoreboardHandler.changeBoardValue(player.getName(), DisplaySlot.SIDEBAR, Bukkit.getOfflinePlayer("Fury"), resourceNow, "Fury");
				}
			}
		}
	}
}

