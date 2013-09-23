package me._Jalf_.Adventures;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

public class CommandHandler implements CommandExecutor
{
	public static Main plugin;

	public CommandHandler(Main plugin)
	{
		CommandHandler.plugin = plugin;
	}
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) 
	{
		String notPM = "This command can only be used by a player!";
		if (cmd.getName().equalsIgnoreCase("adventures"))
		{
			if (args.length == 0) return false;

			String configName = "config.yml";
			String spellsName = "spells.yml";
			String savesName = "saves.yml";
			String classesName = "classes.yml";
			String spellPointsName = "spellPoints.yml";
			
			// Usage strings
			String pointsUsage = "/adventures points <playerName> <spellName> <attribute> <add, sub, set> <numberValue>";
			String pointsSelfUsage = "/adventures points <spellName> <attribute>";
			String resourceUsage = "/adventures resource <playerName> <resourceName> <Max, Now> <add, sub, set> <numberValue>";
			String spellUsage = "/adventures spell <stuck, spellName>";
			String spellChangeUsage = "/adventures spell <get, replace, remove> <spellName> <(spellName)>";		
			String reloadUsage = "/adventures reload <config, spells, saves>";
			String classUsage = "/adventures class set <playerName> <className>";
			
			if (args.length == 1)
			{
				if (args[0].equalsIgnoreCase("resource")) sender.sendMessage(resourceUsage);
				
				else if (args[0].equalsIgnoreCase("class"))
				{
					if (sender instanceof Player)
					{
						Player player = (Player) sender;
						if (plugin.getSaves().getString(player.getName() + ".Class").equalsIgnoreCase("Wizard"))
						{
							player.sendMessage("You're a " + plugin.getSaves().getString(player.getName() + ".Class") + ", Harry!");
						}
						else if (plugin.getSaves().getString(player.getName() + ".Class").equalsIgnoreCase("Default"))
						{
							player.sendMessage("You don't have any class assigned yet!");
						}
						else player.sendMessage("You're a " + plugin.getSaves().getString(player.getName() + ".Class") + "!");
					}
					else
					{
						sender.sendMessage("Sorry, I tried really hard to find it.");
					}
					sender.sendMessage(classUsage);
				}
				
				else if (args[0].equalsIgnoreCase("points")) 
				{
					if (sender instanceof Player)
					{
						Player player = (Player) sender;
						
						Set<String> playerSpells = plugin.getSaves().getConfigurationSection(player.getName() + ".Spells").getKeys(false);
						
						for (String spell : playerSpells)
						{
							int points = plugin.getSaves().getInt(player.getName() + ".Spells." + spell + ".Points");
							int pointsUsed = plugin.getSaves().getInt(player.getName() + ".Spells." + spell + ".PointsUsed");

							if (points == pointsUsed+1)
							{
								player.sendMessage("You have 1 available point for " + spell);
							}
							else if (points > pointsUsed)
							{
								player.sendMessage("You have " + (points - pointsUsed) + " available for " + spell);
							}
						}
					}
					sender.sendMessage(pointsUsage);
					sender.sendMessage(pointsSelfUsage);
				}
				else if (args[0].equalsIgnoreCase("castingType"))
				{
					if (sender instanceof Player)
					{
						Player player = (Player) sender;
						String path = player.getName() + ".Casting Type.";
						
						if (plugin.getSaves().getBoolean(path + "Click") == true)
						{
							plugin.getSaves().set(path + "Click", false);
							plugin.getSaves().set(path + "Instant", true);
							player.sendMessage("Your casting type is now instant cast!");
						}
						else if (plugin.getSaves().getBoolean(path + "Instant") == true)
						{
							plugin.getSaves().set(path + "Click", true);
							plugin.getSaves().set(path + "Instant", false);
							player.sendMessage("Your casting type is now click casting!");
						}
						plugin.saveSaves();
					}
					else
					{
						sender.sendMessage(notPM);
					} 					
				}
				else if (args[0].equalsIgnoreCase("reload"))
				{
					plugin.reloadConfig();
					plugin.reloadSpells();
					plugin.reloadSaves();
					plugin.reloadClasses();
					plugin.reloadSpellPoints();
					sender.sendMessage(configName + ", " + spellsName + ", " + savesName + ", " + classesName + ", " + 
							spellPointsName + " has been reloaded!");
				}
				else if (args[0].equalsIgnoreCase("spell"))
				{
					
					
					
					
					if (sender instanceof Player)
					{
						Player player = (Player) sender;
						Set<String> playerSpells = plugin.getSaves().getConfigurationSection(player.getName() + ".Spells").getKeys(false);
						
						if (plugin.getSaves().getBoolean(player.getName() + ".Casting Type.Click") == true)
						{
							ItemStack item = player.getItemInHand();
							if (item != null)
							{
								if (item.hasItemMeta())
								{
									Set<String> spells = plugin.getSpells().getKeys(false);								
									if (spells.contains(item.getItemMeta().getDisplayName()))
									{
										player.sendMessage(item.getItemMeta().getDisplayName() + ": " + 
												plugin.getSpells().getString(item.getItemMeta().getDisplayName() + ".Description"));
									}
									else 
									{
										for (String playerSpell : playerSpells)
											player.sendMessage("You have " + playerSpell + " in your spell repository.");
										sender.sendMessage(spellUsage);
										sender.sendMessage(spellChangeUsage);
									}
								}
								else 
								{
									for (String playerSpell : playerSpells)
										player.sendMessage("You have " + playerSpell + " in your spell repository.");
									sender.sendMessage(spellUsage);
									sender.sendMessage(spellChangeUsage);
								}
							}
							else 
							{	
								for (String playerSpell : playerSpells)
									player.sendMessage("You have " + playerSpell + " in your spell repository.");
								sender.sendMessage(spellUsage);
								sender.sendMessage(spellChangeUsage);
							}
						}
						else 
						{
							for (String playerSpell : playerSpells)
								player.sendMessage("You have " + playerSpell + " in your spell repository.");
							sender.sendMessage(spellUsage);
							sender.sendMessage(spellChangeUsage);
						}
					}
					else 
					{
						sender.sendMessage(notPM);
						sender.sendMessage(spellUsage);
						sender.sendMessage(spellChangeUsage);
					}
				}
			}
			else if (args.length == 2)
			{
				if (args[0].equalsIgnoreCase("resource")) sender.sendMessage(resourceUsage);
				
				else if (args[0].equalsIgnoreCase("class")) sender.sendMessage(classUsage);
				
				else if (args[0].equalsIgnoreCase("points"))
				{
					String spell = args[1].replace("_", " ");
					
					if (plugin.getSpells().contains(spell))
					{
						Set<String> attributes = plugin.getSpellPoints().getConfigurationSection(spell).getKeys(false);
						sender.sendMessage("Attributes | Base    | Increase per Point");
						for (String attribute : attributes)
						{
							String attributePoint;
							if (plugin.getSpellPoints().get(spell + "." + attribute).equals("0")) 
							{
								attributePoint = "This is worthless to use points on.";
							}
							else 
							{
								attributePoint = plugin.getSpellPoints().get(spell + "." + attribute).toString();
							}
							sender.sendMessage(String.format("%s: %s              | %s ", attribute, plugin.getSpells().get(spell + 
									"." + attribute), attributePoint));
						}
						if (sender instanceof Player)
						{
							Player player = (Player) sender;
							String path = player.getName() + ".Spells." + spell;
							int points = plugin.getSaves().getInt(path + ".Points");
							int pointsUsed = plugin.getSaves().getInt(path + ".PointsUsed");
							
							if (points == pointsUsed)
							{
								player.sendMessage("You don't have any available points for " + spell);
							}
							else if (points == pointsUsed+1)
							{
								player.sendMessage("You have 1 available point for " + spell);
							}
							else
							{
								player.sendMessage("You have " + (points - pointsUsed) + " available points for " + spell);
							}
						}
						else
						{
							sender.sendMessage("You have i available points, Yay imaginary :D");
						}
					}
					else 
					{
						sender.sendMessage("Your typed spell isn't in the spell repository, remember to have the right capitalization and " +
								"replace ' ' with '_' in the spell.");
						sender.sendMessage(pointsSelfUsage);
						sender.sendMessage(pointsUsage);
					}
				} 
				else if (args[0].equalsIgnoreCase("reload"))
				{
					if (args[1].equalsIgnoreCase("config"))
					{
						plugin.reloadConfig();
						sender.sendMessage(configName + " has been reloaded!");
					}
					else if (args[1].equalsIgnoreCase("spells"))
					{
						plugin.reloadSpells();
						sender.sendMessage(spellsName + " has been reloaded!");
					}
					else if (args[1].equalsIgnoreCase("saves"))
					{
						plugin.reloadSaves();
						sender.sendMessage(savesName + " has been reloaded!");
					}
					else if (args[1].equalsIgnoreCase("classes"))
					{
						plugin.reloadClasses();
						sender.sendMessage(classesName + " has been reloaded!");
					}
					else if (args[1].equalsIgnoreCase("spellPoints"))
					{
						plugin.reloadSpellPoints();
						sender.sendMessage(spellPointsName + " has been reloaded!");
					}
					else sender.sendMessage(reloadUsage);
				}
				else if (args[0].equalsIgnoreCase("spell"))
				{
					if (args[1].equalsIgnoreCase("stuck"))
					{
						if (sender instanceof Player)
						{
							final Player player = (Player) sender;
							Set<String> playerSpells = plugin.getSaves().getConfigurationSection(player.getName() + ".Spells").getKeys(false);

							for (final String spell : playerSpells)
							{
								ItemStack[] items = player.getInventory().getContents();

								for (final ItemStack item : items)
								{
									if (item != null)
									{
										if (item.hasItemMeta())
										{
											if (item.getItemMeta().getDisplayName().equalsIgnoreCase("Cooldown") && item.getItemMeta().hasLore())
											{
												if (item.getItemMeta().getLore().get(0).replace(" is on cooldown!", "").equalsIgnoreCase(spell))
												{
													sender.sendMessage(spell + " is now unstuck and is on cooldown!");
													plugin.getServer().getScheduler().runTaskLater(plugin, new BukkitRunnable() 
													{		
														@Override
														public void run() 
														{
															ItemStack spellItem = Methods.spell(player.getName() + ".Spells." + spell, spell);
															item.setType(spellItem.getType());
															if (plugin.getSpells().getInt(spell + ".PlaceHolderDamage") == 0)
															{		
																item.setDurability((short) 0);
															}
															item.setData(spellItem.getData());
															item.setItemMeta(spellItem.getItemMeta());														
														}
													}, plugin.getSpells().getLong(spell + ".Cooldown") + plugin.getSaves().getLong(player.getName() + 
															".Spells." + spell + ".Cooldown"));
												}
											}
										}
									}
								}
							}
						}
						else sender.sendMessage(notPM);
					}
					else if (plugin.getSpells().isSet(args[1].replace("_", " ")))
					{
						String spell = args[1].replace("_", " ");
						sender.sendMessage(spell + ": " + plugin.getSpells().getString(spell + ".Description"));
						
						if (sender instanceof Player)
						{
							if (plugin.getSaves().isSet(sender.getName() + ".Spells." + spell))
							{
								sender.sendMessage("You have " + spell + " in your spell repository.");
							}
						}
						else sender.sendMessage("You have OVER 9000! spells :O");
					}
					else if (args[1].equalsIgnoreCase("get")) sender.sendMessage(spellChangeUsage);
					else if (args[1].equalsIgnoreCase("remove")) sender.sendMessage(spellChangeUsage);
					else if (args[1].equalsIgnoreCase("replace")) sender.sendMessage(spellChangeUsage);
					else 
					{
						sender.sendMessage(spellUsage);
						sender.sendMessage(spellChangeUsage);
					}
				}
			}
			else if (args.length == 3)
			{
				if (args[0].equalsIgnoreCase("resource")) sender.sendMessage(resourceUsage);
				
				else if (args[0].equalsIgnoreCase("class")) sender.sendMessage(classUsage);

				else if (args[0].equalsIgnoreCase("points"))
				{
					if (sender instanceof Player)
					{
						Player player = (Player) sender;
						String spell = args[1].replace("_", " ");
						if (plugin.getSaves().isSet(player.getName() + ".Spells." + spell))
						{
							if (plugin.getSaves().isSet(player.getName() + ".Spells." + spell + "." + args[2]))
							{
								if (plugin.getSaves().getInt(player.getName() + ".Spells." + spell + ".PointsUsed") < 
										plugin.getSaves().getInt(player.getName() + ".Spells." + spell + ".Points"))
								{
									if (plugin.getSaves().getInt(player.getName() + ".Spells." + spell + "." + args[2]) < 
											(2 * plugin.getSpellPoints().getInt(spell + "." + args[2])))
									{
										String path = player.getName() + ".Spells." + spell + "." + args[2];
										plugin.getSaves().set(path, plugin.getSaves().getInt(path) + plugin.getSpellPoints().getInt(spell + "." + args[2]));
										
										String pointPath = player.getName() + ".Spells." + spell + ".PointsUsed";
										plugin.getSaves().set(pointPath, plugin.getSaves().getInt(pointPath) + 1);
										
										plugin.saveSaves();
										player.sendMessage("You have added a point into " + args[2] + " on " + spell + "!");
									}
									else
									{
										player.sendMessage("Sorry, but you can only put two points in one attribute per spell.");
									}
								}
								else
								{
									player.sendMessage("You have used all your available points for " + spell);
								}
							}
							else 
							{
								sender.sendMessage("Your typed attribute isn't a possible attribute for this spell.");
								sender.sendMessage(pointsSelfUsage);
								sender.sendMessage(pointsUsage);
							}
						}
						else 
						{
							sender.sendMessage("Your typed spell isn't in your spell repository, remember to have the right capitalization and " +
									"replace ' ' with '_' in the spell.");
							sender.sendMessage(pointsSelfUsage);
							sender.sendMessage(pointsUsage);
						}
					}
					else
					{
						sender.sendMessage(notPM);
						sender.sendMessage(pointsSelfUsage);
						sender.sendMessage(pointsUsage);
					}
				}
				else if (args[0].equalsIgnoreCase("spell"))
				{
					if (args[1].equalsIgnoreCase("get"))
					{
						if (sender instanceof Player)
						{
							Player player = (Player) sender;
							String spell = args[2].replace("_", " ");
							if (plugin.getSaves().isSet(player.getName() + ".Spells." + spell))
							{
								int spellsInInv = 0;
								boolean hasWantedSpellInInv = false;
								for (ItemStack itemInInv : player.getInventory().getContents())
								{
									if (itemInInv != null)
									{
										if (itemInInv.hasItemMeta() && itemInInv.getItemMeta().hasDisplayName() && itemInInv.getItemMeta().hasLore())
										{
											Set<String> playerSpells = plugin.getSaves().getConfigurationSection(player.getName() + ".Spells").getKeys(false);
											
											if (itemInInv.getItemMeta().getDisplayName().equalsIgnoreCase("Cooldown") && 
													itemInInv.getItemMeta().getLore().get(0).replace(" is on cooldown!", "").equals(spell))
											{
												hasWantedSpellInInv = true;
												break;
											}
											else if (itemInInv.getItemMeta().getDisplayName().equals(spell))
											{
												hasWantedSpellInInv = true;
												break;
											}
											else for (String playerSpell : playerSpells)
											{
												if (itemInInv.getItemMeta().getDisplayName().equals("Cooldown"))
												{
													if (itemInInv.getItemMeta().getLore().get(0).replace(" is on cooldown!", "").equals(playerSpell))
													{
														spellsInInv++;
													}
												}
												else if (itemInInv.getItemMeta().getDisplayName().equals(playerSpell))
												{
													spellsInInv++;
												}
											}
										}
									}
								}
								if (hasWantedSpellInInv)
								{
									player.sendMessage("You already have that spell in your inventory!");
									sender.sendMessage(spellChangeUsage);
								}
								else
								{
									if (spellsInInv < plugin.getClasses().getInt(plugin.getSaves().getString(player.getName() + ".Class") + ".Spells Max"))
									{	
										String path = player.getName() + ".Spells." + spell;	
										ItemStack itemStackSpell = Methods.spell(path, spell);

										player.sendMessage("You now got " + spell + " in your inventory.");
										
										long cooldownValue = 2 *plugin.getSpells().getLong(spell + ".Cooldown") + plugin.getSaves().getLong(path + ".Cooldown");

										// Red Cooldown Wool
										ItemStack cooldownItemRed = new ItemStack(Material.WOOL, 1, (short) 14);
										ItemMeta cooldownMetaRed = cooldownItemRed.getItemMeta();
										cooldownMetaRed.setDisplayName("Cooldown");

										List<String> cooldownItemLoreRed = new ArrayList<>();
										cooldownItemLoreRed.add(spell + " is on cooldown!");
										cooldownMetaRed.setLore(cooldownItemLoreRed);

										cooldownItemRed.setItemMeta(cooldownMetaRed);

										// Green Cooldown Wool
										ItemStack cooldownItemGreen = new ItemStack(Material.WOOL, 1, (short) 5);
										ItemMeta cooldownMetaGreen = cooldownItemGreen.getItemMeta();
										cooldownMetaGreen.setDisplayName("Cooldown");

										List<String> cooldownItemLoreGreen = new ArrayList<>();
										cooldownItemLoreGreen.add(spell + " is on cooldown!");
										cooldownMetaGreen.setLore(cooldownItemLoreGreen);

										cooldownItemGreen.setItemMeta(cooldownMetaGreen);

										int slotNumber = 0;
										
										for (int i = 0; i < player.getInventory().getSize(); i++)
										{
											if (player.getInventory().getItem(i) == null)
											{
												slotNumber = i;
												break;
											}
										}
										
										if (cooldownValue <= 40) 
										{
											player.getInventory().setItem(slotNumber, cooldownItemGreen);
											plugin.getServer().getScheduler().runTaskLater(plugin, new CooldownTask(player, slotNumber, cooldownValue, 
													spell, itemStackSpell, plugin), cooldownValue);
										}
										else 
										{
											player.getInventory().setItem(slotNumber, cooldownItemRed);
											plugin.getServer().getScheduler().runTaskLater(plugin, new CooldownTask(player, slotNumber, cooldownValue, 
													spell, itemStackSpell, plugin), cooldownValue - 40);
										}
									}
									else
									{
										player.sendMessage("You have reached the max number of spells for you class, " +
												"try to remove or replace then instead of others.");
										sender.sendMessage(spellChangeUsage);
									}
								}
							}
							else 
							{
								sender.sendMessage("Your typed spell isn't in your spell repository, remember to have the right capitalization and " +
									"replace ' ' with '_' in the spell.");
								sender.sendMessage(spellChangeUsage);
							}
						}
						else sender.sendMessage(notPM);
					}
					else if (args[1].equalsIgnoreCase("remove"))
					{
						if (sender instanceof Player)
						{
							Player player = (Player) sender;
							String spell = args[2].replace("_", " ");
							if (plugin.getSaves().isSet(player.getName() + ".Spells." + spell))
							{
								String path = player.getName() + ".Spells." + spell;	
								ItemStack itemStackSpell = Methods.spell(path, spell);
								
								if (Methods.takeItemFromPlayerInventory(player, itemStackSpell))
									player.sendMessage(spell + " is now removed from your inventory!");
								else
								{
									player.sendMessage("Your typed spell isn't in your inventory, remember to have the right capitalization and " +
											"replace ' ' with '_' in the spell.");
									player.sendMessage(spellChangeUsage);
								}
							}
							else 
							{
								sender.sendMessage("Your typed spell isn't in your spell repository, remember to have the right capitalization and " +
									"replace ' ' with '_' in the spell.");
								sender.sendMessage(spellChangeUsage);
							}
						}
						else sender.sendMessage(notPM);
					}
				}
			}
			else if (args.length == 4)
			{
				if (args[0].equalsIgnoreCase("points")) sender.sendMessage(pointsUsage);
				
				else if (args[0].equalsIgnoreCase("resource")) sender.sendMessage(resourceUsage);
				
				else if (args[0].equalsIgnoreCase("class"))
				{
					if (args[1].equalsIgnoreCase("set"))
					{
						if (Bukkit.getPlayer(args[2]).isOnline())
						{
							final String player = args[2];
							Set<String> classes = plugin.getClasses().getKeys(false);
							if (classes.contains(args[3]))
							{	
								boolean switchClass = true;
								if (args[3].equals("Admin") && !Bukkit.getPlayer(player).isOp()) switchClass = false;
								
								if (!plugin.getSaves().getString(player + ".Class").equals(args[3]) && switchClass == true)
								{
									PlayerResource.playerNoResource.add(player);
									
									plugin.getSaves().set(player + ".Class", args[3]);

									plugin.getSaves().set(player + ".Resource", null);

									plugin.getSaves().set(player + ".Resource." + 
											plugin.getClasses().getString(args[3] + ".Resource.Resource Name") + ".Max", 
											plugin.getClasses().getInt(args[3] + ".Resource.Resource Start") + 
											plugin.getClasses().getInt(args[3] + ".Resource.Resource per Lvl"));
									
									plugin.getSaves().set(player + ".Resource." + 
											plugin.getClasses().getString(args[3] + ".Resource.Resource Name") + ".Now", 0);
									
									Bukkit.getPlayer(player).sendMessage("You are now a " + args[3]);
									
									plugin.saveSaves();
									
									Bukkit.getPlayer(player).setLevel(1);
									new BukkitRunnable() 
									{
										@Override
										public void run() 
										{
											Bukkit.getPlayer(player).setLevel(0);
											Bukkit.getPlayer(player).setExp(0);
										}
									}.runTaskLater(plugin, 1);
									
									ScoreboardHandler.unregister(player);
									if (!plugin.getSaves().getString(player + ".Class").equalsIgnoreCase("Default"))
									{
										String resourceName = Methods.getPlayerResourceName(player);
										ScoreboardHandler.registerBoard(player, resourceName);
									}
								}
								else 
								{
									if (switchClass == false)
									{
										sender.sendMessage("'Admin' is an only op class!");
									}
									else sender.sendMessage("Target player is already that class!");
								}
							}
							else sender.sendMessage("Specified class isn't in the " + classesName);
						}
						else sender.sendMessage("Target player must be online!");
					}
					else sender.sendMessage(classUsage);
				}
				else if (args[0].equalsIgnoreCase("spell"))
				{
					if (args[1].equalsIgnoreCase("replace"))
					{
						if (sender instanceof Player)
						{
							Player player = (Player) sender;
							String spell = args[2].replace("_", " ");
							String spell2 = args[3].replace("_", " ");
							if (plugin.getSaves().isSet(player.getName() + ".Spells." + spell) && 
									plugin.getSaves().isSet(player.getName() + ".Spells." + spell2))
							{
								boolean hasWantedSpellInInv = false;
								int slotNumber = 0;
								
								for (int i = 0; i < player.getInventory().getSize(); i++)
								{
									ItemStack itemInInv = player.getInventory().getItem(i);
									if (itemInInv != null)
									{
										if (itemInInv.hasItemMeta() && itemInInv.getItemMeta().hasDisplayName() && itemInInv.getItemMeta().hasLore())
										{
											if (itemInInv.getItemMeta().getDisplayName().equalsIgnoreCase("Cooldown") && 
													itemInInv.getItemMeta().getLore().get(0).replace(" is on cooldown!", "").equals(spell))
											{
												break;
											}
											else if (itemInInv.getItemMeta().getDisplayName().equals(spell))
											{
												hasWantedSpellInInv = true;
												slotNumber = i;
												break;
											}
										}
									}
								}
								if (hasWantedSpellInInv)
								{
									String path = player.getName() + ".Spells." + spell2;	
									ItemStack itemStackSpell = Methods.spell(path, spell2);

									player.sendMessage("You now got " + spell2 + " in your inventory.");
									
									long cooldownValue = 2 *plugin.getSpells().getLong(spell2 + ".Cooldown") + plugin.getSaves().getLong(path + ".Cooldown");

									// Red Cooldown Wool
									ItemStack cooldownItemRed = new ItemStack(Material.WOOL, 1, (short) 14);
									ItemMeta cooldownMetaRed = cooldownItemRed.getItemMeta();
									cooldownMetaRed.setDisplayName("Cooldown");

									List<String> cooldownItemLoreRed = new ArrayList<>();
									cooldownItemLoreRed.add(spell2 + " is on cooldown!");
									cooldownMetaRed.setLore(cooldownItemLoreRed);

									cooldownItemRed.setItemMeta(cooldownMetaRed);

									// Green Cooldown Wool
									ItemStack cooldownItemGreen = new ItemStack(Material.WOOL, 1, (short) 5);
									ItemMeta cooldownMetaGreen = cooldownItemGreen.getItemMeta();
									cooldownMetaGreen.setDisplayName("Cooldown");

									List<String> cooldownItemLoreGreen = new ArrayList<>();
									cooldownItemLoreGreen.add(spell2 + " is on cooldown!");
									cooldownMetaGreen.setLore(cooldownItemLoreGreen);

									cooldownItemGreen.setItemMeta(cooldownMetaGreen);

									if (cooldownValue <= 40) 
									{
										player.getInventory().setItem(slotNumber, cooldownItemGreen);
										plugin.getServer().getScheduler().runTaskLater(plugin, new CooldownTask(player, slotNumber, cooldownValue, 
												spell2, itemStackSpell, plugin), cooldownValue);
									}
									else 
									{
										player.getInventory().setItem(slotNumber, cooldownItemRed);
										plugin.getServer().getScheduler().runTaskLater(plugin, new CooldownTask(player, slotNumber, cooldownValue, 
												spell2, itemStackSpell, plugin), cooldownValue - 40);
									}
								}
								else
								{
									sender.sendMessage("Your typed spell ins't in your inventory or is on cooldown!");
									sender.sendMessage(spellChangeUsage);
								}
							}
							else 
							{
								sender.sendMessage("Your typed spell isn't in your spell repository, remember to have the right capitalization and " +
										"replace ' ' with '_' in the spell.");
								sender.sendMessage(spellChangeUsage);
							}
						}
						else sender.sendMessage(notPM);
					}
					else sender.sendMessage(spellChangeUsage);
				}
			}
			else if (args.length == 5)
			{
				if (args[0].equalsIgnoreCase("points")) sender.sendMessage(pointsUsage);
				
				else if (args[0].equalsIgnoreCase("resource")) sender.sendMessage(resourceUsage);
				
				else if (args[0].equalsIgnoreCase("spell"))
				{
					if (args[1].equalsIgnoreCase("get"))
					{
						sender.sendMessage("Your typed spell isn't in your spell repository, remember to have the right capitalization and " +
								"replace ' ' with '_' in the spell.");
						sender.sendMessage(spellChangeUsage);
					}
					else if (args[1].equalsIgnoreCase("remove"))
					{
						sender.sendMessage("Your typed spell isn't in your spell repository, remember to have the right capitalization and " +
								"replace ' ' with '_' in the spell.");
						sender.sendMessage(spellChangeUsage);
					}
					else if (args[1].equalsIgnoreCase("replace"))
					{
						sender.sendMessage("Your typed spell isn't in your spell repository, remember to have the right capitalization and " +
								"replace ' ' with '_' in the spell.");
						sender.sendMessage(spellChangeUsage);
					}
				}
			}
			else if (args.length == 6)
			{				
				if (args[0].equalsIgnoreCase("points"))
				{
					String player = args[1];
					String spell = args[2].replace("_", " ");
					if (plugin.getSaves().isSet(player + ".Spells." + spell))
					{
						if (plugin.getSaves().isSet(player + ".Spells." + spell + "." + args[3]))
						{
							if (Methods.isInt(args[5]) && Integer.parseInt(args[5]) < 100)
							{
								String path = player + ".Spells." + spell + "." + args[3];
								if (args[4].equalsIgnoreCase("add"))
								{
									plugin.getSaves().set(path, plugin.getSaves().getInt(path) + Integer.parseInt(args[5]));
								}
								else if (args[4].equalsIgnoreCase("sub"))
								{
									plugin.getSaves().set(path, plugin.getSaves().getInt(path) - Integer.parseInt(args[5]));
								}
								else if (args[4].equalsIgnoreCase("set"))
								{
									plugin.getSaves().set(path, Integer.parseInt(args[5]));
								}
								else sender.sendMessage(pointsUsage);
								plugin.saveSaves();
							}
							else sender.sendMessage(pointsUsage);
						}
						else sender.sendMessage("Target spell don't have that attribute!");
					}
					else sender.sendMessage("Target player don't have that spell in the save file!");
				}
				else if (args[0].equalsIgnoreCase("resource"))
				{
					String player = args[1];
					String resourceName = args[2];
					if (plugin.getSaves().isSet(player + ".Resource." + resourceName))
					{
						if (plugin.getSaves().isSet(player + ".Resource." + resourceName + "." + args[3]))
						{
							if (Methods.isInt(args[5]) && Integer.parseInt(args[5]) < 10000)
							{
								String path = player + ".Resource." + resourceName + "." + args[3];
								if (args[4].equalsIgnoreCase("add"))
								{
									if (!args[2].equals(Methods.getPlayerResourceName(player)) && args[3].equals("Max")) plugin.getSaves().set(player + 
											".Resource." + Methods.getPlayerResourceName(player) + ".Max", 0);
									plugin.getSaves().set(path, plugin.getSaves().getInt(path) + Integer.parseInt(args[5]));
								}
								else if (args[4].equalsIgnoreCase("sub"))
								{
									if (!args[2].equals(Methods.getPlayerResourceName(player)) && args[3].equals("Max")) plugin.getSaves().set(player + 
											".Resource." + Methods.getPlayerResourceName(player) + ".Max", 0);
									plugin.getSaves().set(path, plugin.getSaves().getInt(path) - Integer.parseInt(args[5]));
								}
								else if (args[4].equalsIgnoreCase("set"))
								{
									if (!args[2].equals(Methods.getPlayerResourceName(player)) && args[3].equals("Max")) plugin.getSaves().set(player + 
											".Resource." + Methods.getPlayerResourceName(player) + ".Max", 0);
									plugin.getSaves().set(path, Integer.parseInt(args[5]));
								}
								else sender.sendMessage(resourceUsage);
								
								if (args[4].equalsIgnoreCase("add") || args[4].equalsIgnoreCase("sub") || args[4].equalsIgnoreCase("set"))
								{
									ScoreboardHandler.unregister(player);
									ScoreboardHandler.registerBoard(player, resourceName);

									plugin.getServer().getScheduler().runTaskTimer(plugin, 
											new ResourceRegenTask(Bukkit.getPlayer(player), plugin), 0, 20);
									
									plugin.saveSaves();
								}
							}
							else sender.sendMessage(resourceUsage);
						}
						else sender.sendMessage("Target resource don't have that node!");
					}
					else sender.sendMessage("Target player don't have that resource in the save file!");
				}
				else if (args[0].equalsIgnoreCase("spell"))
				{
					if (args[1].equalsIgnoreCase("get"))
					{
						sender.sendMessage("Your typed spell isn't in your spell repository, remember to have the right capitalization and " +
								"replace ' ' with '_' in the spell.");
						sender.sendMessage(spellChangeUsage);
					}
					else if (args[1].equalsIgnoreCase("remove"))
					{
						sender.sendMessage("Your typed spell isn't in your spell repository, remember to have the right capitalization and " +
								"replace ' ' with '_' in the spell.");
						sender.sendMessage(spellChangeUsage);
					}
					else if (args[1].equalsIgnoreCase("replace"))
					{
						sender.sendMessage("Your typed spell isn't in your spell repository, remember to have the right capitalization and " +
								"replace ' ' with '_' in the spell.");
						sender.sendMessage(spellChangeUsage);
					}
				}
			}
		}
		return true;
	}
}
