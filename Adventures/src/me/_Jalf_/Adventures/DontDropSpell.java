package me._Jalf_.Adventures;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;

public class DontDropSpell implements Listener
{
	private final Main plugin;

	public DontDropSpell(Main plugin) 
	{
		this.plugin = plugin;
	}
	@EventHandler
	public void PlayerDrop(PlayerDropItemEvent event)
	{
		Player player = event.getPlayer();
		if (event.getItemDrop().getItemStack().hasItemMeta())
		{		
			String itemMeta = event.getItemDrop().getItemStack().getItemMeta().getDisplayName();
			
			String path = player.getName() + ".Spells." + itemMeta;
			if (plugin.getSaves().contains(path) || itemMeta.equalsIgnoreCase("Cooldown"))
			{
				event.setCancelled(true);
			}
		}
	}
}
