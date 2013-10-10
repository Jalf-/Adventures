package me._Jalf_.Adventures;

import java.util.HashMap;
import java.util.Set;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

public class PartyHandler implements Listener
{
	public static Main plugin;
	
	public PartyHandler(Main plugin)
	{
		PartyHandler.plugin = plugin;
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}
	
	public static HashMap<String, String> partyInviteMap = new HashMap<>();
	
	/**@param player
	 * Player leaving
	 * @param board
	 * Scoreboard object
	 */
	public static void partyLeave(Player player, Scoreboard board)
	{
		// Check if party exists
		if (getPlayerParty(player.getName()) != null)
		{
			// Check for party size
			if (getPartyMembers(getPlayerParty(player.getName())).size() <= 2)
			{
				// Remove party
				for (OfflinePlayer member : getPartyMembers(getPlayerParty(player.getName())))
				{
					if (!member.getName().equals(player.getName()))
					{
						member.getPlayer().sendMessage(player.getName() + " left the party!");
						member.getPlayer().sendMessage("Party removed because lack of players!");
					}
				}
				board.getTeam(getPlayerParty(player.getName())).unregister();
			}
			else 
			{
				// Check if quitting player is party leader
				if (player.getName().equals(getPlayerParty(player.getName())))
				{
					String highestLvlPlayer = "";
					int highestLvl = 0;

					Set<OfflinePlayer> members = getPartyMembers(getPlayerParty(player.getName()));

					// Get highest lvl player in party
					for (OfflinePlayer member : members)
					{
						if (!member.getName().equals(player.getName()))
						{
							if (member.getPlayer().getLevel() > highestLvl)
							{
								highestLvl = member.getPlayer().getLevel();
								highestLvlPlayer = member.getName();
							}
						}
					}
					// Creating new team
					Team team = plugin.getServer().getScoreboardManager().getMainScoreboard().registerNewTeam(highestLvlPlayer);
					team.setAllowFriendlyFire(false);

					members.remove(plugin.getServer().getOfflinePlayer(player.getName()));
					board.getTeam(getPlayerParty(player.getName())).unregister();

					plugin.getServer().getPlayerExact(highestLvlPlayer).sendMessage("You are the new party leader!");

					for (OfflinePlayer member : members)
					{
						team.addPlayer(member);
					} 
				}
				else
				{
					// Remove party
					for (OfflinePlayer member : getPartyMembers(getPlayerParty(player.getName())))
					{
						if (!member.getName().equals(player.getName()))
						{
							member.getPlayer().sendMessage(player.getName() + " left the party!");
						}
					}
					board.getTeam(getPlayerParty(player.getName())).removePlayer(player);
				}
			}
		}
		else player.sendMessage("You are not in a party!");
	}
	
	@EventHandler
	public void playerQuit(PlayerQuitEvent event)
	{
		Scoreboard board = plugin.getServer().getScoreboardManager().getMainScoreboard();
		Player player = event.getPlayer();

		partyLeave(player, board);		
	}
	
	@EventHandler
	public static void onPlayerChat(AsyncPlayerChatEvent event)
	{
		Player player = event.getPlayer();
		// Check if player have or is party leader
		if (getPlayerParty(player.getName()) != null)
		{
			// Check if party size is greater than 1
			if (getPartyMembers(getPlayerParty(player.getName())).size() > 1)
			{
				// Check if player want to do party chat
				if (player.getMetadata("PartyChat").get(0).value().toString().equalsIgnoreCase("true"))
				{
					// Printing messages
					for (OfflinePlayer member : getPartyMembers(getPlayerParty(player.getName())))
					{
						member.getPlayer().sendMessage("[P] <" + player.getName()+ "> " + event.getMessage());
					}
					event.setCancelled(true);
				}
			}
		}
	}
	
	/**@param teamName
	 * Name of the scoreboard team
	 * @return Player(s) in the scoreboard team 
	 */
	public static Set<OfflinePlayer> getPartyMembers(String teamName)
	{
		Scoreboard board = plugin.getServer().getScoreboardManager().getMainScoreboard();
		if (board.getTeam(teamName) == null) return null;
		if (board.getTeam(teamName).getPlayers().isEmpty()) return null;
		return board.getTeam(teamName).getPlayers();
	}
	
	/**@param playerName
	 * Name of player that is in a party
	 * @return Name of players party, same as party leader or null if party do not exists
	 */
	public static String getPlayerParty(String playerName)
	{
		Scoreboard board = plugin.getServer().getScoreboardManager().getMainScoreboard();	
		Player player = plugin.getServer().getPlayerExact(playerName);
		
		// Check if team exists and then get the name off it
		if (board.getPlayerTeam(player) != null) return board.getPlayerTeam(player).getName();
		return null;		
	}
	
	/**@param askingPlayer
	 * Player object of the player in a party
	 * @param player
	 * Player object to check if is in askingPlayers party
	 * @return Boolean answer if askingPlayers party contains player then return true else return false
	 */
	public static boolean isPlayerPartOfAskingPlayersParty(Player askingPlayer, Player player)
	{	
		// Check if party exists and if other player is in the party
		if (PartyHandler.getPlayerParty(askingPlayer.getName()) != null 
				&& PartyHandler.getPartyMembers(PartyHandler.getPlayerParty(askingPlayer.getName())).
					contains(plugin.getServer().getOfflinePlayer((player).getName())))
		{
			return true;
		}		
		return false;
	} 
	// Share Party Experience
	// Located in ClassHandler under playerExp (PlayerExpChangeEvent)
}
