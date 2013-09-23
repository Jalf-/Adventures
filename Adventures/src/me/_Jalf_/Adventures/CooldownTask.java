package me._Jalf_.Adventures;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

public class CooldownTask extends BukkitRunnable
{
	public Player player;
	public int newSlot;
	public long cooldown;
	public String itemMeta;
	public ItemStack itemStackSpell;
	public Main plugin;

	public CooldownTask(Player player, int newSlot, long cooldown, String itemMeta, ItemStack itemStackSpell, Main plugin) 
	{
		this.player = player;
		this.newSlot = newSlot;
		this.cooldown = cooldown;
		this.itemMeta = itemMeta;
		this.itemStackSpell = itemStackSpell;
		this.plugin = plugin;
	}
	@Override
	public void run() 
	{
		if (cooldown <= 40)
		{
			player.getInventory().setItem(newSlot, itemStackSpell);
		}
		else
		{
			ItemStack cooldownItemGreen = new ItemStack(Material.WOOL, 1, (short) 5);
			ItemMeta cooldownMetaGreen = cooldownItemGreen.getItemMeta();
			cooldownMetaGreen.setDisplayName("Cooldown");

			List<String> cooldownItemLoreGreen = new ArrayList<>();
			cooldownItemLoreGreen.add(itemMeta + " is on cooldown!");
			cooldownMetaGreen.setLore(cooldownItemLoreGreen);

			cooldownItemGreen.setItemMeta(cooldownMetaGreen);

			player.getInventory().setItem(newSlot, cooldownItemGreen);

			plugin.getServer().getScheduler().runTaskLater(plugin, new BukkitRunnable() 
			{		
				@Override
				public void run() 
				{
					player.getInventory().setItem(newSlot, itemStackSpell);
				}
			}, 40);
		}
	}
}
