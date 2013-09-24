package me._Jalf_.Adventures;

//import java.util.HashMap;

//import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PartyHandler implements Listener
{
	public static Main plugin;
	
	public PartyHandler(Main plugin)
	{
		PartyHandler.plugin = plugin;
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}	
	
	@EventHandler
	public void playerQuit(PlayerQuitEvent event)
	{
//		Player player = event.getPlayer();
//
//
//		if (Methods.getPartyMembers(player.getName()).size() > 2)
//		{
//			if (player.getName() == getPartyLeader(player.getName()))
//			{
//				
//			}
//		}
//		else
//		{
//			for (String playerName : Methods.getPartyMembers(player.getName()))
//			{
//				removeParty(playerName);
//			}
//		}
//
		
	}

	public static void removeParty(String name) 
	{
		
		
	}
	
	public static String getPartyLeader(String name)
	{
		String playerName = "Dude";
		
		return playerName;
	}
}
