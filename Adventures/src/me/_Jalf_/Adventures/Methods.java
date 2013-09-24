package me._Jalf_.Adventures;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.WordUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;

public class Methods 
{
	public static Main plugin;

	public Methods(Main plugin) 
	{
		Methods.plugin = plugin;
	}
	
	public static List<Location> circle (Location loc, int r, int h, boolean hollow, boolean sphere, int plusY) 
	{
        List<Location> circleblocks = new ArrayList<Location>();
        int cX = loc.getBlockX();
        int cY = loc.getBlockY();
        int cZ = loc.getBlockZ();
        for (int x = cX - r; x <= cX +r; x++)
            for (int z = cZ - r; z <= cZ +r; z++)
                for (int y = (sphere ? cY - r : cY); y < (sphere ? cY + r : cY + h); y++) 
                {
                    double dist = (cX - x) * (cX - x) + (cZ - z) * (cZ - z) + (sphere ? (cY - y) * (cY - y) : 0);
                    if (dist < r*r && !(hollow && dist < (r-1)*(r-1))) 
                    {
                        Location l = new Location(loc.getWorld(), x, y + plusY, z);
                        circleblocks.add(l);
                    }
                }
        return circleblocks;
    }
	
	public static ItemStack spell (String path, String itemMeta)
	{
		// General Spell Attributes
		int damageValue = plugin.getSpells().getInt(itemMeta + ".Damage") + plugin.getSaves().getInt(path + ".Damage");
		double radiusValue = plugin.getSpells().getDouble(itemMeta + ".Radius") + plugin.getSaves().getDouble(path + ".Radius");
		
		// Specific Spell Attributes				
		int countValue = plugin.getSpells().getInt(itemMeta + ".Count") + plugin.getSaves().getInt(path + ".Count");
		int lengthValue = plugin.getSpells().getInt(itemMeta + ".Length") + plugin.getSaves().getInt(path + ".Length");
		int widthValue = plugin.getSpells().getInt(itemMeta + ".Width") + plugin.getSaves().getInt(path + ".Width");
		int heightValue = plugin.getSpells().getInt(itemMeta + ".Height") + plugin.getSaves().getInt(path + ".Height");
		int strengthValue = plugin.getSpells().getInt(itemMeta + ".Strength") + plugin.getSaves().getInt(path + ".Strength");
		int healValue = plugin.getSpells().getInt(itemMeta + ".Heal") + plugin.getSaves().getInt(path + ".Heal");
		
		// Util Spell Attributes
		int angleValue = plugin.getSpells().getInt(itemMeta + ".Angle") + plugin.getSaves().getInt(path + ".Angle");
		int timeValue = plugin.getSpells().getInt(itemMeta + ".Time") + plugin.getSaves().getInt(path + ".Time");
		int delayValue = plugin.getSpells().getInt(itemMeta + ".Delay") + plugin.getSaves().getInt(path + ".Delay");
		int rangeValue = plugin.getSpells().getInt(itemMeta + ".Range") + plugin.getSaves().getInt(path + ".Range");
		long cooldownValue = plugin.getSpells().getLong(itemMeta + ".Cooldown") + plugin.getSaves().getLong(path + ".Cooldown");
		int resourceValue = plugin.getSpells().getInt(itemMeta + ".Resource") + plugin.getSaves().getInt(path + ".Resource");
		
		// ItemStack for spell
		ItemStack itemStackSpell = new ItemStack(Material.getMaterial(plugin.getSpells().getString(itemMeta + ".PlaceHolderName")),
				1, (short) plugin.getSpells().getInt(itemMeta + ".PlaceHolderDamage"));
		ItemMeta itemMetaSpell = itemStackSpell.getItemMeta();
		itemMetaSpell.setDisplayName(itemMeta);

		List<String> itemStackSpellLore = new ArrayList<>();
		
		// General Spell Attributes
		if (plugin.getSpells().contains(itemMeta + ".Lore")) 
			itemStackSpellLore.add(plugin.getSpells().getString(itemMeta + ".Lore"));
		if (plugin.getSpells().contains(itemMeta + ".Damage"))
			itemStackSpellLore.add("Damage: " + String.valueOf(damageValue));
		if (plugin.getSpells().contains(itemMeta + ".Radius"))
			itemStackSpellLore.add("Radius: " + String.valueOf(radiusValue));

		// Specific Spell Attributes
		if (plugin.getSpells().contains(itemMeta + ".Count"))
			itemStackSpellLore.add("Count: " + String.valueOf(countValue));
		if (plugin.getSpells().contains(itemMeta + ".Length"))
			itemStackSpellLore.add("Length: " + String.valueOf(lengthValue));
		if (plugin.getSpells().contains(itemMeta + ".Width"))
			itemStackSpellLore.add("Width: " + String.valueOf(widthValue));
		if (plugin.getSpells().contains(itemMeta + ".Height"))
			itemStackSpellLore.add("Height: " + String.valueOf(heightValue));
		if (plugin.getSpells().contains(itemMeta + ".Strength"))
			itemStackSpellLore.add("Spell Strength: " + String.valueOf(strengthValue));
		if (plugin.getSpells().contains(itemMeta + ".Heal"))
			itemStackSpellLore.add("Heal: " + String.valueOf(healValue));

		// Util Spell Attributes
		if (plugin.getSpells().contains(itemMeta + ".Angle"))
			itemStackSpellLore.add("Angle: " + String.valueOf(angleValue));
		if (plugin.getSpells().contains(itemMeta + ".Time"))
			itemStackSpellLore.add("Time: " + String.valueOf(timeValue / 20));
		if (plugin.getSpells().contains(itemMeta + ".Delay"))
			itemStackSpellLore.add("Delay: " + String.valueOf(delayValue / 20));
		if (plugin.getSpells().contains(itemMeta + ".Range"))
			itemStackSpellLore.add("Range: " + String.valueOf(rangeValue));	
		if (plugin.getSpells().contains(itemMeta + ".Cooldown"))
			itemStackSpellLore.add("Cooldown: " + String.valueOf(cooldownValue / 20));
		if (plugin.getSpells().contains(itemMeta + ".Resource"))
			itemStackSpellLore.add("Resource: " + String.valueOf(resourceValue));
		
		itemMetaSpell.setLore(itemStackSpellLore);							
		itemStackSpell.setItemMeta(itemMetaSpell);
		
		return itemStackSpell;
	}
	
	public static int getF(Player player) 
	{
	    double d = (player.getLocation().getYaw() * 4.0F / 360.0F) + 0.5D;
	    int i = (int) d;
	    return d < i ? i - 1 : i;
	}
	
	public static boolean isInt(String s) 
	{
		try 
		{
			Integer.parseInt(s);
			return true;
		} 
		catch (NumberFormatException e) 
		{
			return false;
		}
	}

	public static String getPlayerResourceName (String player)
	{
		if (plugin.getClasses().contains(plugin.getSaves().getString(player + ".Class")))
		{
			return plugin.getClasses().getString(plugin.getSaves().getString(player + ".Class") + ".Resource.Resource Name");
		}
		else 
		{
			System.out.println("Player have invalid class! Error in Methods.getPlayerResourceName!");
			return "Energy";
		}
	}
	
	public static List<Material> waterDestroy ()
	{
		List<Material> waterDestroyList = new ArrayList<>();
		
		waterDestroyList.add(Material.FLOWER_POT);
		waterDestroyList.add(Material.TORCH);
		waterDestroyList.add(Material.STONE_BUTTON);
		waterDestroyList.add(Material.WOOD_BUTTON);
		waterDestroyList.add(Material.BROWN_MUSHROOM);
		waterDestroyList.add(Material.CROPS);
		waterDestroyList.add(Material.DEAD_BUSH);
		waterDestroyList.add(Material.DETECTOR_RAIL);
		waterDestroyList.add(Material.RAILS);
		waterDestroyList.add(Material.DIODE);
		waterDestroyList.add(Material.DIODE_BLOCK_OFF);
		waterDestroyList.add(Material.DIODE_BLOCK_ON);
		waterDestroyList.add(Material.ITEM_FRAME);
		waterDestroyList.add(Material.LAVA);
		waterDestroyList.add(Material.LEVER);
		waterDestroyList.add(Material.LONG_GRASS);
		waterDestroyList.add(Material.SUGAR_CANE_BLOCK);
		waterDestroyList.add(Material.MELON_STEM);
		waterDestroyList.add(Material.NETHER_STALK);
		waterDestroyList.add(Material.PAINTING);
		waterDestroyList.add(Material.POTATO);
		waterDestroyList.add(Material.PUMPKIN_STEM);
		waterDestroyList.add(Material.RED_MUSHROOM);
		waterDestroyList.add(Material.RED_ROSE);
		waterDestroyList.add(Material.REDSTONE_COMPARATOR);
		waterDestroyList.add(Material.REDSTONE_COMPARATOR_OFF);
		waterDestroyList.add(Material.REDSTONE_COMPARATOR_ON);
		waterDestroyList.add(Material.REDSTONE_TORCH_OFF);
		waterDestroyList.add(Material.REDSTONE_TORCH_ON);
		waterDestroyList.add(Material.REDSTONE_WIRE);
		waterDestroyList.add(Material.SAPLING);
		waterDestroyList.add(Material.SEEDS);
		waterDestroyList.add(Material.SKULL);
		waterDestroyList.add(Material.STATIONARY_LAVA);
		waterDestroyList.add(Material.TORCH);
		waterDestroyList.add(Material.TRIPWIRE);
		waterDestroyList.add(Material.TRIPWIRE_HOOK);
		waterDestroyList.add(Material.VINE);
		waterDestroyList.add(Material.WEB);
		waterDestroyList.add(Material.YELLOW_FLOWER);

		return waterDestroyList;
	}
	//// Taken from https://forums.bukkit.org/threads/challenge-getting-all-livingentity-in-players-field-of-view.60045/
	/** @param entities
	 * List of nearby entities
	 * @param startPos
	 * starting position
	 * @param Radius
	 * distance cone travels
	 * @param Degrees
	 * angle of cone
	 * @param direction
	 * direction of the cone
	 * @return All entities inside the cone */
	public static List<Entity> getEntitiesInCone(List<Entity> entities, Vector startPos, float radius, float degrees, Vector direction) 
	{
		List<Entity> newEntities = new ArrayList<Entity>(); // Returned list
		float squaredRadius = radius * radius; // We don't want to use square root

		for (Entity e : entities) 
		{
			Vector relativePosition = e.getLocation().toVector(); // Position of the entity relative to the cone origin
			relativePosition.subtract(startPos);
			if (relativePosition.lengthSquared() > squaredRadius) continue; // First check : distance
			if (getAngleBetweenVectors(direction, relativePosition) > degrees) continue; // Second check : angle
			newEntities.add(e); // The entity e is in the cone
		}
		return newEntities;
	}

	/** @param startPos
	 * starting position
	 * @param radius
	 * distance cone travels
	 * @param degrees
	 * angle of cone
	 * @param direction
	 * direction of the cone
	 * @return All block positions inside the cone */
	public static List<Vector> getPositionsInCone(Vector startPos, float radius, float degrees, Vector direction) 
	{

		List<Vector> positions = new ArrayList<Vector>(); // Returned list
		float squaredRadius = radius * radius; // We don't want to use square root

		for (float x=startPos.getBlockX()-radius; x<startPos.getBlockX()+radius; x++)
			for (float y=startPos.getBlockY()-radius; y<startPos.getBlockY()+radius; y++)
				for (float z=startPos.getBlockZ()-radius; z<startPos.getBlockZ()+radius; z++) 
				{
					Vector relative = new Vector(x,y,z);
					relative.subtract(startPos);
					if (relative.lengthSquared() > squaredRadius) continue; // First check : distance
					if (getAngleBetweenVectors(direction, relative) > degrees) continue; // Second check : angle
					positions.add(new Vector(x,y,z)); // The position v is in the cone
				}
		return positions;
	}
	
	public static float getAngleBetweenVectors(Vector v1, Vector v2) 
	{
		return Math.abs((float)Math.toDegrees(v1.angle(v2)));
	}
	////^Taken from https://forums.bukkit.org/threads/challenge-getting-all-livingentity-in-players-field-of-view.60045/
	
	public static boolean checkPlayerInventoryForItem (Player player, ItemStack item)
	{
		int amount = item.getAmount();
		
		for (ItemStack itemInInv : player.getInventory().getContents())
		{
			if (itemInInv != null)
			{
				if (itemInInv.isSimilar(item))
				{
					if (amount <= 0) break;
					else amount -= itemInInv.getAmount();
				}
			}
		}
		if (amount <= 0)
		{
			return true;
		}
		else 
		{
			if (item.hasItemMeta() && item.getItemMeta().hasDisplayName())
			{
				player.sendMessage("You don't have " + item.getItemMeta().getDisplayName() + " in your inventory!");
			}
			else 
			{
				player.sendMessage("You don't have " + 
						WordUtils.capitalizeFully(item.getType().toString().replace("_", " ")) + " in your inventory!");
			}
			return false;
		}
	}
	
	public static boolean takeItemFromPlayerInventory (Player player, ItemStack item)
	{
		int amount = item.getAmount();
		
		for (ItemStack itemInInv : player.getInventory().getContents())
		{
			if (itemInInv != null)
			{
				if (itemInInv.isSimilar(item))
				{
					if (amount <= 0) break;
					else amount -= itemInInv.getAmount();
				}
			}
		}
		if (amount <= 0)
		{
			player.getInventory().removeItem(item);
			return true;
		}
		else 
		{
			if (item.hasItemMeta() && item.getItemMeta().hasDisplayName())
			{
				player.sendMessage("You don't have enough " + item.getItemMeta().getDisplayName() + " in your inventory!");
			}
			else 
			{
				player.sendMessage("You don't have enough " + 
						WordUtils.capitalizeFully(item.getType().toString().replace("_", " ")) + " in your inventory!");
			}
			return false;
		}
	}
	
	public static boolean giveItemToPlayerInventory (Player player, ItemStack item, boolean split)
	{
		int freeSpace = 0;
		for (ItemStack itemInInv : player.getInventory()) 
		{
			if (itemInInv == null) 
			{
				freeSpace += item.getType().getMaxStackSize();
			} 
			else if (itemInInv.isSimilar(item))
			{
				if (item.getAmount() <= freeSpace) break;
				else freeSpace += itemInInv.getType().getMaxStackSize() - itemInInv.getAmount();
			}
		}
		if (item.getAmount() <= freeSpace) 
		{
			if (split)
			{
				ItemStack itemClone = item.clone();
				itemClone.setAmount(1);
				for (int i = 0; i != item.getAmount(); i++)
				{
					player.getInventory().addItem(itemClone);
				}
			}
			else player.getInventory().addItem(item);
			return true;
		} 
		else 
		{
			player.sendMessage("Your inventory is full!");
			return false;
		}
	}
	
	public static List<String> getPartyMembers(String name)
	{
		List<String> members = new ArrayList<>();
		
		members.add("Dude");
		members.add("Oi");
		members.add("Bot");
		
		return members;
	}
}
