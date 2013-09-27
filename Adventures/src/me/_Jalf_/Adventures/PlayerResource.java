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
import org.bukkit.scheduler.BukkitRunnable;
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
		final Player player = event.getPlayer();

		// Starts player resource regeneration
		plugin.getServer().getScheduler().runTaskLater(plugin, new BukkitRunnable()
		{
			@Override
			public void run() 
			{
				// Check if player has joined before
				if (plugin.getSaves().isSet(player.getName() + ".Class"))
				{
					// Check if players class is valid
					if (plugin.getClasses().contains(plugin.getSaves().getString(player.getName() + ".Class")))
					{
						// Check if player isn't Default
						if (!plugin.getSaves().getString(player.getName() + ".Class").equalsIgnoreCase("Default"))
						{
							String resourceName = Methods.getPlayerResourceName(player.getName());
							ScoreboardHandler.registerBoard(player.getName(), resourceName);
						}
						plugin.getServer().getScheduler().runTaskTimer(plugin, new ResourceRegenTask(player, plugin), 0, 20);
					}
				}
			}
		}, 5);
	}
	
	@EventHandler
	public void savePlayerResource(PlayerQuitEvent event)
	{
		Player player = event.getPlayer();
		
		// Checks if player isn't Default class and then saves current resource to saves.yml
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

		// Fury Regeneration on hit by another entity
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
		
		// Fury Regeneration on hitting another entity
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

