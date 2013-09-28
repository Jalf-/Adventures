package me._Jalf_.Adventures;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;

public class PartyHandler implements Listener
{
	public static Main plugin;
	
	public PartyHandler(Main plugin)
	{
		PartyHandler.plugin = plugin;
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}
	
	public static HashMap<String, String> partyInviteMap = new HashMap<>();

	@EventHandler
	public void playerQuit(PlayerQuitEvent event)
	{	
		Player player = event.getPlayer();

		// Check if party exists
		if (!getPartyLeader(player.getName()).isEmpty())
		{
			// Check for party size
			if (getPartyMembers(player, "PartyMembers", plugin).size() != 2)
			{
				// Check if quitting player is party leader
				if (player.getName().equals(getPartyLeader(player.getName())))
				{
					if (player.hasMetadata("PartyMembers"))
					{
						List<String> members = new ArrayList<>();

						for (String member : getPartyMembers(player, "PartyMembers", plugin))
						{
							if (!member.equals(player.getName())) members.add(member);
						}

						// Select new leader
						Player newLeader = plugin.getServer().getPlayerExact(getPartyMembers(player, "PartyMembers", plugin).get(0).toString());
						if (newLeader.equals(player))
						{
							newLeader = plugin.getServer().getPlayerExact(getPartyMembers(player, "PartyMembers", plugin).get(1).toString());
						}

						// Setting up new party
						for (String member : getPartyMembers(player, "PartyMembers", plugin))
						{
							removeParty(member);
							if (!member.equals(player.getName()))
							{
								plugin.getServer().getPlayerExact(member).sendMessage(player.getName() + " has left the party!");

								plugin.getServer().getPlayerExact(member).setMetadata("PartyLeader", new FixedMetadataValue(plugin, newLeader));
								plugin.getServer().getPlayerExact(member).setMetadata("PartyMembers", 
										new FixedMetadataValue(plugin, members.remove(member)));

								if (newLeader.getName().equals(member))
								{
									plugin.getServer().getPlayerExact(member).sendMessage("You are now the new leader for this party!");
								}
								else plugin.getServer().getPlayerExact(member).sendMessage(newLeader + " is now the new leader for this party!");
							}
						}
					}
					else
					{
						player.removeMetadata("PartyLeader", plugin);
					}
				}
				else
				{
					List<String> members = new ArrayList<>();
					
					for (String member : getPartyMembers(player, "PartyMembers", plugin))
					{
						if (!member.equals(player.getName())) members.add(member);
					}			
					for (String member : getPartyMembers(player, "PartyMembers", plugin))
					{
						plugin.getServer().getPlayerExact(member).removeMetadata("PartyMembers", plugin);
						if (!member.equals(player.getName()))
						{
							plugin.getServer().getPlayerExact(member).sendMessage(player.getName() + " has left your party!");
							
							plugin.getServer().getPlayerExact(member).setMetadata("PartyMembers", 
									new FixedMetadataValue(plugin, members.remove(member)));
						}
					}
					removeParty(player.getName());
				}
			}
			else
			{
				for (String playerName : getPartyMembers(player, "PartyMembers", plugin))
				{
					removeParty(playerName);
					plugin.getServer().getPlayerExact(playerName).sendMessage("Everyone left your party!");
				}
			}
		}
	}
	
	@EventHandler
	public static void onPlayerChat(AsyncPlayerChatEvent event)
	{
		Player player = event.getPlayer();
		
		// Check if player have or is party leader
		if (getPartyLeader(player.getName()) != null)
		{
			// Check if party size is greater than 1
			if (getPartyMembers(player, "PartyMembers", plugin).size() > 1)
			{
				// Check if player want to do party chat
				if (player.getMetadata("PartyChat").equals("true"))
				{
					// Printing messages
					for (String member : getPartyMembers(player, "PartyMembers", plugin))
					{
						plugin.getServer().getPlayerExact(member).sendMessage(event.getFormat() + "[P]" + event.getMessage());
					}
					event.setCancelled(true);
				}
			}
		}
	}
	
	/**@param player
	 * Player bbject
	 * @param key
	 * Metadata string wanted to be checked
	 * @param plugin
	 * Plugin instance
	 * @return Value of metadata string if it exists else it will return empty space
	 */
	public static List<String> getPartyMembers(Player player, String key, Plugin plugin)
	{
		List<String> members = new ArrayList<>();
		
		if (player.hasMetadata(key))
		{
			List<MetadataValue> values = player.getMetadata(key);  
			for(MetadataValue value : values)
			{
				if(value.getOwningPlugin().getDescription().getName().equals(plugin.getDescription().getName()))
				{
					members.add(value.asString());
				}
			}
			if (!members.isEmpty())
			{
				return members;
			}
		}
		return new ArrayList<>();
	}

	/**@param name
	 * Name of player that will get his party removed
	 */
	public static void removeParty(String name) 
	{
		Player player = plugin.getServer().getPlayerExact(name);
		
		player.removeMetadata("PartyMembers", plugin);
		player.removeMetadata("PartyLeader", plugin);
	}
	
	/**@param name
	 * Name of player that is in a party
	 * @return Party Leader
	 */
	public static String getPartyLeader(String name)
	{
		Player player = plugin.getServer().getPlayerExact(name);
		
		return Methods.getMetadata(player, "PartyLeader", plugin).toString();
	}
}
