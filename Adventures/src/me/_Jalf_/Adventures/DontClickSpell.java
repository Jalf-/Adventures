package me._Jalf_.Adventures;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType.SlotType;

public class DontClickSpell implements Listener
{
	@SuppressWarnings("deprecation")
	@EventHandler
	public void InventoryClick(InventoryClickEvent event)
	{		
		if (event.getSlotType() != SlotType.OUTSIDE)
		{
			if (event.getCurrentItem().hasItemMeta())
			{
				if (event.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("Spell"))
				{
					Player player = (Player) event.getWhoClicked();
					if (player.isOp())
					{
						player.sendMessage("Server -> " + player.getName() + ": I hope you know what you're doing.");
						player.sendMessage(player.getName() + " -> Server: It's a me Mario!");
					}
					else
					{
						event.setCancelled(true);
						player.updateInventory();
					}
				}
			}	
		}
	}
}
