package me._Jalf_.Adventures;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;

public class ScoreboardHandler 
{
	public static Main plugin;

	public ScoreboardHandler(Main plugin)
	{
		ScoreboardHandler.plugin = plugin;
	}
	
	private static Map<String, Scoreboard> playerScoreboards = new HashMap<String, Scoreboard>();

	/**@param player
	 * Player display name
	 * @param resourceName
	 * Players resource name
	 */
	public static void registerBoard(String player, String resourceName)
	{
		Scoreboard scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
		Objective side = scoreboard.registerNewObjective("side", "dummy");

		int resource = plugin.getSaves().getInt(player + ".Resource." + resourceName + ".Now");
		Score score = side.getScore(Bukkit.getOfflinePlayer(resourceName));
		score.setScore(resource);

		side.setDisplaySlot(DisplaySlot.SIDEBAR);
		side.setDisplayName(player);
		
		playerScoreboards.put(player, scoreboard);
		Bukkit.getPlayer(player).setScoreboard(scoreboard);
	}
	
	/**@param player
	 * Player display name
	 */
	public static void unregister(String player) 
	{
		playerScoreboards.remove(player);
	}
	
	/**@param player
	 * Player display name
	 * @param slot
	 * Scoreboard display slot DisplaySlot.(BELOW_NAME, PLAYER_LIST, SIDEBAR)
	 * @param scoreName
	 * Name of score
	 * @param value
	 * New scoreboard value
	 * @param resourceName
	 * Players resource name
	 */
	public static void changeBoardValue(String player, DisplaySlot slot, OfflinePlayer scoreName, int value, String resourceName)
	{
		if(playerScoreboards.containsKey(player)) 
		{
			Scoreboard scoreboard = playerScoreboards.get(player);
			scoreboard.getObjective(slot).getScore(scoreName).setScore(value);
			plugin.getSaves().set(player + ".Resource." + resourceName + ".Now", value);	
		}
	}
	
	/**@param player
	 * Player display name
	 * @param slot
	 * Scoreboard display slot DisplaySlot.(BELOW_NAME, PLAYER_LIST, SIDEBAR)
	 * @param scoreName
	 * Name of score
	 * @return Value of score
	 */
	public static int getScore (String player, DisplaySlot slot, OfflinePlayer scoreName)
	{
		int resourceNow = playerScoreboards.get(player).getObjective(slot).getScore(scoreName).getScore();
		return resourceNow;
	}
}
