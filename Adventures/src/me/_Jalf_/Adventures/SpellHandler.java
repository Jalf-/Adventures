package me._Jalf_.Adventures;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import me._Jalf_.Adventures.Spells.*;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.DisplaySlot;

public class SpellHandler extends Main implements Listener 
{	
	public static Main plugin;

	public SpellHandler(Main plugin) 
	{
		SpellHandler.plugin = plugin;
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
		new Methods(plugin);
	}
	
	@EventHandler
	public void playerClickItemHeld(PlayerInteractEvent event)
	{
		Action action = event.getAction();
		Player player = event.getPlayer();

		if (event.hasBlock() || event.hasItem())
		{
			if (action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK)
			{
				int heldSlot = event.getPlayer().getInventory().getHeldItemSlot();
				ItemStack item = event.getPlayer().getItemInHand();
				if (item != null)
				{
					if (item.hasItemMeta())
					{
						String itemMeta = item.getItemMeta().getDisplayName();

						if (plugin.getSaves().getBoolean(player.getName() + ".Casting Type.Click") == true)
						{
							if (itemMeta.equalsIgnoreCase("Cooldown"))
							{
								player.sendMessage(item.getItemMeta().getLore().get(0));
							}
							else 
							{
								makeSpell(player, itemMeta, heldSlot, heldSlot);

								String path = player.getName() + ".Spells." + itemMeta;	
								if (plugin.getSaves().contains(path))
								{
									event.setCancelled(true);
								}
							}
						}
					}
				}
			}
		}
	}
	
	@EventHandler
	public void playerItemHeld(PlayerItemHeldEvent event)
	{
		Player player = event.getPlayer();
		int newSlot = event.getNewSlot();
		int prevSlot = event.getPreviousSlot();
		
		if (player.getInventory().getItem(newSlot) != null)
		{
			if (player.getInventory().getItem(newSlot).hasItemMeta() && player.getInventory().getItem(newSlot).getItemMeta().hasDisplayName())
			{	
				String itemMeta = player.getInventory().getItem(newSlot).getItemMeta().getDisplayName();

				if (plugin.getSaves().getBoolean(player.getName() + ".Casting Type.Instant") == true)
				{
					if (itemMeta.equalsIgnoreCase("Cooldown"))
					{
						player.sendMessage(player.getInventory().getItem(newSlot).getItemMeta().getLore().get(0));
						player.getInventory().setHeldItemSlot(prevSlot);
					} 
					else
					{
						makeSpell(player, itemMeta, newSlot, prevSlot);
					}
				}
			}
		}
	}
	
	public void makeSpell(Player player, String itemMeta, int newSlot, int prevSlot)
	{
		String path = player.getName() + ".Spells." + itemMeta;		

		if (plugin.getSaves().contains(path))
		{
			String resourceName = Methods.getPlayerResourceName(player.getName());
			
			int resourceNow = ScoreboardHandler.getScore(player.getName(), DisplaySlot.SIDEBAR, Bukkit.getOfflinePlayer(resourceName));						
			if (resourceNow >= plugin.getSpells().getInt(itemMeta + ".Resource"))
			{
				resourceNow = resourceNow - plugin.getSpells().getInt(itemMeta + ".Resource");
				ScoreboardHandler.changeBoardValue(player.getName(), DisplaySlot.SIDEBAR, Bukkit.getOfflinePlayer(resourceName), resourceNow, resourceName);

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
				
				// Red Cooldown Wool
				ItemStack cooldownItemRed = new ItemStack(Material.WOOL, 1, (short) 14);
				ItemMeta cooldownMetaRed = cooldownItemRed.getItemMeta();
				cooldownMetaRed.setDisplayName("Cooldown");

				List<String> cooldownItemLoreRed = new ArrayList<>();
				cooldownItemLoreRed.add(itemMeta + " is on cooldown!");
				cooldownMetaRed.setLore(cooldownItemLoreRed);

				cooldownItemRed.setItemMeta(cooldownMetaRed);

				// Green Cooldown Wool
				ItemStack cooldownItemGreen = new ItemStack(Material.WOOL, 1, (short) 5);
				ItemMeta cooldownMetaGreen = cooldownItemGreen.getItemMeta();
				cooldownMetaGreen.setDisplayName("Cooldown");

				List<String> cooldownItemLoreGreen = new ArrayList<>();
				cooldownItemLoreGreen.add(itemMeta + " is on cooldown!");
				cooldownMetaGreen.setLore(cooldownItemLoreGreen);

				cooldownItemGreen.setItemMeta(cooldownMetaGreen);

				ItemStack itemStackSpell = Methods.spell(path, itemMeta);
				
				switch(itemMeta)
				{
				case "Lightning Bolt":
					LightningBolt.lightningBoltSpell(player, damageValue, radiusValue, rangeValue);
					break;
				case "Exploding Arrow":
					ExplodingArrow.explodingArrowSpell(player, countValue);
					break;
				case "Fire Wall":
					FireWall.fireWallSpell(player, rangeValue, lengthValue, widthValue, timeValue);
					break;
				case "Slow Aura":
					SlowAura.slowAuraSpell(player, strengthValue, radiusValue, timeValue);
					break;
				case "Starve":
					Starve.starveSpell(player, strengthValue, radiusValue, rangeValue, timeValue);
					break;
				case "Fire Bolt":
					FireBolt.fireBoltSpell(player, damageValue, strengthValue, rangeValue);
					break;
				case "Fire Ball":
					FireBall.fireBallSpell(player, damageValue, strengthValue, rangeValue);
					break;
				case "Sprint":
					Sprint.sprintSpell(player, strengthValue, timeValue);
					break;
				case "Dash":
					Dash.dashSpell(player, strengthValue);
					break;
				case "Fire Walk":
					FireWalk.fireWalkSpell(player, widthValue, timeValue);
					break;
				case "Shotgun Arrow":
					ShotgunArrow.shotgunArrowSpell(player, strengthValue, countValue);
					break;
				case "Smoke Screen":
					SmokeScreen.smokeScreenSpell(player, radiusValue, timeValue);
					break;
				case "Bombardment":
					Bombardment.bombardmentSpell(player, countValue, rangeValue, radiusValue, strengthValue);
					break;
				case "Iron Prison":
					IronPrison.ironPrisonSpell(player, timeValue, rangeValue, radiusValue);
					break;
				case "Arrow Rain":
					ArrowRain.arrowRainSpell(player, countValue, rangeValue, radiusValue);
					break;
				case "Healing Well":
					HealingWell.healingWellSpell(player, rangeValue, radiusValue, timeValue, healValue);
					break;
				case "Warp Burst":
					WarpBurst.warpBurstSpell(player, strengthValue, rangeValue);
					break;
				case "Stone Wall":
					StoneWall.stoneWallSpell(player, rangeValue, lengthValue, heightValue, timeValue);
					break;
				case "Ice Wall":
					IceWall.iceWallSpell(player, rangeValue, lengthValue, heightValue, timeValue);
					break;
				case "Haste":
					Haste.hasteSpell(player, strengthValue, timeValue);
					break;
				case "Meteor":
					Meteor.meteorSpell(player, strengthValue, radiusValue, rangeValue);
					break;
				case "Comet":
					Comet.cometSpell(player, strengthValue, radiusValue, rangeValue);
					break;
				case "Flash":
					Flash.flashSpell(player, strengthValue, radiusValue, timeValue);
					break;
				case "Arrow Line":
					ArrowLine.arrowLineSpell(player, strengthValue, countValue);
					break;
				case "Hidden Mine":
					HiddenMine.hiddenMineSpell(player, timeValue, radiusValue, strengthValue, delayValue);
					break;
				case "Snow Ball":
					SnowBall.snowBallSpell(player, damageValue, strengthValue, rangeValue);
					break;
				case "Ice Bolt":
					IceBolt.iceBoltSpell(player, damageValue, strengthValue, rangeValue);
					break;
				case "Blinding Arrow":
					BlindingArrow.blindingArrowSpell(player, damageValue, strengthValue);
					break;
				case "Poison Dart":
					PoisonDart.poisonDartSpell(player, damageValue, strengthValue, rangeValue);
					break;
				case "Egg Throw":
					EggThrow.eggThrowSpell(player, damageValue, strengthValue, rangeValue);
					break;
				case "Leap":
					Leap.leapSpell(player, timeValue, strengthValue);
					break;
				case "To The Sky":
					ToTheSky.toTheSkySpell(player, strengthValue, radiusValue);
					break;
				case "Quake":
					Quake.quakeSpell(player, strengthValue, radiusValue, timeValue);
					break;
				case "Ice Ring":
					IceRing.iceRingSpell(player, radiusValue, heightValue, timeValue);
					break;
				case "Fire Ring":
					FireRing.fireRingSpell(player, radiusValue, widthValue, timeValue);
					break;
				case "Ice Sphere":
					IceSphere.iceSphereSpell(player, timeValue, radiusValue);
					break;
				case "Attraction":
					Attraction.attractionSpell(player, radiusValue);
					break;
				case "Push Bubble":
					PushBubble.pushBubbleSpell(player, radiusValue);
					break;
				case "Push":
					Push.pushSpell(player, radiusValue, angleValue);
					break;
				case "Grab":
					Grab.grabSpell(player, radiusValue, angleValue);
					break;
				case "Fire Breath":
					FireBreath.fireBreathSpell(player, radiusValue, angleValue, timeValue);
					break;
				case "Frost Breath":
					FrostBreath.frostBreathSpell(player, radiusValue, angleValue, strengthValue);
					break;
				case "Lightning Storm":
					LightningStorm.lightningStormSpell(player, damageValue, radiusValue, rangeValue, timeValue);
					break;
				case "Lightning Shield":
					LightningShield.lightningShieldSpell(player, damageValue, radiusValue, timeValue);
					break;
				case "Chain Lightning":
					ChainLightning.chainLightningSpell(player, damageValue, radiusValue, rangeValue, strengthValue);
					break;
				case "Push Aura":
					PushAura.pushBubbleSpell(player, radiusValue, timeValue);
					break;
				case "Stone Ring":
					StoneRing.stoneRingSpell(player, radiusValue, heightValue, timeValue);
					break;
				case "Ignite":
					Ignite.igniteSpell(player, timeValue, radiusValue, rangeValue);
					break;
				case "Terrify":
					Terrify.terrifySpell(player, timeValue, rangeValue, radiusValue, strengthValue);
					break;
				case "Blinding Flash":
					BlindingFlash.blindingFlashSpell(player, strengthValue, radiusValue, rangeValue, timeValue);
					break;
				case "Blizzard":
					Blizzard.blizzardSpell(player, damageValue, radiusValue, rangeValue, timeValue, strengthValue);
					break;
				case "Foul Play":
					FoulPlay.foulPlaySpell(player, rangeValue, radiusValue, timeValue, damageValue);
					break;
				case "Switch":
					Switch.switchSpell(player, rangeValue, radiusValue, damageValue);
					break;
				case "Eyes Closed":
					EyesClosed.eyesClosed(player, strengthValue, timeValue);
					break;
				default:
					System.out.println(itemMeta + " isn't found in the code, but is in the save file! Contact author. Error in : " + SpellHandler.class.getName());
					break;
				}
				if (cooldownValue <= 40) 
				{
					player.getInventory().setItem(newSlot, cooldownItemGreen);
					plugin.getServer().getScheduler().runTaskLater(plugin, new CooldownTask(player, newSlot, cooldownValue, itemMeta, itemStackSpell, plugin), cooldownValue);
				}
				else 
				{
					player.getInventory().setItem(newSlot, cooldownItemRed);
					plugin.getServer().getScheduler().runTaskLater(plugin, new CooldownTask(player, newSlot, cooldownValue, itemMeta, itemStackSpell, plugin), cooldownValue - 40);
				}					
				player.sendMessage(itemMeta + " has been activated!");
			} else player.sendMessage(itemMeta + " costs more resource than you have!");
			player.getInventory().setHeldItemSlot(prevSlot);
		}
	}

	@EventHandler
	public void resetJoinSpells (PlayerJoinEvent event)
	{
		final Player player = event.getPlayer();

		if (plugin.getSaves().isSet(player.getName() + ".Spells"))
		{
			Set<String> playerSpells = plugin.getSaves().getConfigurationSection(player.getName() + ".Spells").getKeys(false);

			for (final String spell : playerSpells)
			{
				ItemStack[] items = player.getInventory().getContents();

				for (final ItemStack item : items)
				{
					if (item != null)
					{
						if (item.hasItemMeta() && item.getItemMeta().hasDisplayName())
						{
							if (item.getItemMeta().getDisplayName().equalsIgnoreCase("Cooldown") && item.getItemMeta().hasLore())
							{
								if (item.getItemMeta().getLore().get(0).replace(" is on cooldown!", "").equalsIgnoreCase(spell))
								{
									plugin.getServer().getScheduler().runTaskLater(plugin, new BukkitRunnable() 
									{		
										@Override
										public void run() 
										{
											ItemStack spellItem = Methods.spell(player.getName() + ".Spells." + spell, spell);
											item.setType(spellItem.getType());
											System.out.println();
											if (plugin.getSpells().getInt(spell + ".PlaceHolderDamage") == 0)
											{		
												item.setDurability((short) 0);
											}
											item.setData(spellItem.getData());
											item.setItemMeta(spellItem.getItemMeta());
										}
									}, plugin.getSpells().getLong(spell + ".Cooldown") + plugin.getSaves().getLong(player.getName() + ".Spells." + spell + ".Cooldown"));
								}
							}
						}
					}
				}
			}
		}
	}
}