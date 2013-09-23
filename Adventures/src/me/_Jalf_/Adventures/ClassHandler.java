package me._Jalf_.Adventures;

import java.util.Set;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerExpChangeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLevelChangeEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

public class ClassHandler implements Listener
{
	public static Main plugin;

	public ClassHandler(Main plugin) 
	{
		ClassHandler.plugin = plugin;
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void playerJoin (PlayerJoinEvent event)
	{
		Player player = event.getPlayer();
		
		// Add Player Setup in saves.yml
		if (!plugin.getSaves().isSet(player.getName()))
		{
			plugin.getSaves().set(player.getName() + ".Class", "Default");
		
			plugin.getSaves().set(player.getName() + ".Casting Type.Click", false);
			plugin.getSaves().set(player.getName() + ".Casting Type.Instant", true);
			
			plugin.getSaves().set(player.getName() + ".Resource.Energy.Now", 0);
			plugin.getSaves().set(player.getName() + ".Resource.Energy.Max", 1);
			
			plugin.getSaves().createSection(player.getName() + ".Spells");
			
			plugin.saveSaves();
		}
		
		// Set player health
		player.setMaxHealth(plugin.getClasses().getInt(plugin.getSaves().getString(player.getName() + ".Class") + ".Health.Health Start") +
				plugin.getClasses().getInt(plugin.getSaves().getString(player.getName() + ".Class") + ".Health.Health per Lvl") * player.getLevel());
	}

	@EventHandler
	public void playerRespawn (PlayerRespawnEvent event)
	{
		Player player = event.getPlayer();
		
		// Set player health
				player.setMaxHealth(plugin.getClasses().getInt(plugin.getSaves().getString(player.getName() + ".Class") + ".Health.Health Start") +
						plugin.getClasses().getInt(plugin.getSaves().getString(player.getName() + ".Class") + ".Health.Health per Lvl") * player.getLevel());
	}
	
	@EventHandler
	public void playerExp (PlayerExpChangeEvent event)
	{
		event.setAmount((int) Math.floor(event.getAmount() * (plugin.getConfig().getDouble("Exp Boost") / 100)));
	}
	
	@EventHandler
	public void playerLevel (PlayerLevelChangeEvent event)
	{
		Player player = event.getPlayer();
		int prevLevel = event.getOldLevel();
		int newLevel = event.getNewLevel();
		String playerClass = plugin.getSaves().getString(player.getName() + ".Class");
		
		// Setting Resource
		plugin.getSaves().set(player.getName() + ".Resource." + 
				Methods.getPlayerResourceName(player.getName()) + ".Max", 
				plugin.getClasses().getInt(plugin.getSaves().getString(player.getName() + ".Class") + ".Resource.Resource Start") + 
				plugin.getClasses().getInt(plugin.getSaves().getString(player.getName() + ".Class") + ".Resource.Resource per Lvl"));
		
		// Setting player Health
		player.setMaxHealth(plugin.getClasses().getInt(plugin.getSaves().getString(player.getName() + ".Class") + ".Health.Health Start") +
				plugin.getClasses().getInt(plugin.getSaves().getString(player.getName() + ".Class") + ".Health.Health per Lvl") * player.getLevel());

		if ((newLevel - prevLevel) > 1 && player.getHealth() != 1) player.sendMessage("You gained " + (newLevel - prevLevel) + " levels!");
		else if ((newLevel - prevLevel) == -1 && player.getHealth() != 1) player.sendMessage("You lost a level!");
		else if ((newLevel - prevLevel) < -1 && player.getHealth() != 1) player.sendMessage("You lost " + (prevLevel - newLevel) + " levels!");
		else if ((newLevel - prevLevel) == 1 && player.getHealth() != 1)player.sendMessage("You gained a level!");	
		
		// Checking if player have gained new spell or point to a spell
		if (plugin.getClasses().contains(playerClass))
		{
			Set<String> classSpells = plugin.getClasses().getConfigurationSection(playerClass + ".Spells").getKeys(false);
			int prevLevelNew = prevLevel;
			
			if (prevLevel > newLevel)
			{
				Set<String> playerSpells = plugin.getSaves().getConfigurationSection(player.getName() + ".Spells").getKeys(false);
				for (String playerSpell : playerSpells)
				{
					plugin.getSaves().getConfigurationSection(player.getName() + ".Spells").set(playerSpell, null);
				}
				plugin.saveSaves();
				prevLevelNew = 0;
			}

			for (String classSpell : classSpells)
			{ 
				for (int q = prevLevelNew; q < newLevel; q++)
				{
					for (int i = 0; i < 6; i++)
					{
						if (plugin.getClasses().getInt(playerClass + ".Spells." + classSpell + ".Lvl") + 
								i * plugin.getClasses().getInt(playerClass + ".Spells." + classSpell + ".Points per x Lvl") == q+1)
						{
							if (i > 0 && plugin.getSaves().isSet(player.getName() + ".Spells." + classSpell) && 
									plugin.getSaves().getInt(player.getName() + ".Spells." + classSpell + ".Points") < i)
							{
								player.sendMessage("You gained a point for " + classSpell);
								String path = player.getName() + ".Spells." + classSpell + ".Points";
								plugin.getSaves().set(path, plugin.getSaves().getInt(path) + 1);
							}
							else if (!plugin.getSaves().isSet(player.getName() + ".Spells." + classSpell))
							{
								player.sendMessage("You gained " + classSpell + " Spell");

								Set<String> attributes = plugin.getSpells().getConfigurationSection(classSpell).getKeys(false);

								if (attributes.contains("PlaceHolderName")) attributes.remove("PlaceHolderName");
								if (attributes.contains("PlaceHolderDamage")) attributes.remove("PlaceHolderDamage");
								if (attributes.contains("Lore")) attributes.remove("Lore");
								if (attributes.contains("Description")) attributes.remove("Description");

								for (String attribute : attributes)
								{
									plugin.getSaves().set(player.getName() + ".Spells." + classSpell + "." + attribute, 0);
								}
								plugin.getSaves().set(player.getName() + ".Spells." + classSpell + ".Points", 0);
								plugin.getSaves().set(player.getName() + ".Spells." + classSpell + ".PointsUsed", 0);
							}
						}
					}
				}
			}
		}
		else player.sendMessage("Your class isn't defined in the classes.yml, report to plugin developer/admin!");
		plugin.saveSaves();
	}
}
