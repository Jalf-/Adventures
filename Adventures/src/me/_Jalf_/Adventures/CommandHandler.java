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
import org.bukkit.metadata.FixedMetadataValue;
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
		if (cmd.getName().equalsIgnoreCase("adventures") || cmd.getName().equalsIgnoreCase("a"))
		{
			if (args.length == 0) return false;

			String notPM = "This command can only be used by a player!";
			
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
			//                  "/adventures spell unstuck"
			String spellChangeUsage = "/adventures spell <get, replace, remove> <spellName> <(spellName)>";		
			String reloadUsage = "/adventures reload <config, spells, saves>";
			String classUsage = "/adventures class set <playerName> <className>";
			
			String partyUsage = "/adventures party <create, leave, chat>";
			String partyManageUsage = "/adventures party <invite, join> <playerName>";

			if (args.length == 1)
			{
				if (args[0].equalsIgnoreCase("resource")) sender.sendMessage(resourceUsage);
				
				// Print players class
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
				
				// Print how many points player have in each spell 
				else if (args[0].equalsIgnoreCase("points")) 
				{
					if (sender instanceof Player)
					{
						Player player = (Player) sender;
						for (String spell : plugin.getSaves().getConfigurationSection(player.getName() + ".Spells").getKeys(false))
						{
							int points = plugin.getSaves().getInt(player.getName() + ".Spells." + spell + ".Points");
							int pointsUsed = plugin.getSaves().getInt(player.getName() + ".Spells." + spell + ".PointsUsed");

							if (points == pointsUsed+1)
							{
								player.sendMessage("You have 1 available point for " + spell);
							}
							else if (points > pointsUsed)
							{
								player.sendMessage("You have " + (points - pointsUsed) + " available points for " + spell);
							}
						}
					}
					sender.sendMessage(pointsUsage);
					sender.sendMessage(pointsSelfUsage);
				}
				
				// Change players casting type to the opposite of what it is
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
				
				// Reload all files in datafolder
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
				
				// Print spell in hands description or print all player spells
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
								if (item.hasItemMeta() && item.getItemMeta().hasDisplayName())
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
				
				else if (args[0].equalsIgnoreCase("party"))
				{
					// Check if sender is a player
					if (sender instanceof Player)
					{
						Player player = (Player) sender;
						// Check if player is in a party
						if (player.hasMetadata("PartyLeader"))
						{
							sender.sendMessage("----------Party----------");
							// Check if player is the party leader
							if (PartyHandler.getPartyLeader(player.getName()).equals(player.getName())) sender.sendMessage("You are the party leader!");
							else sender.sendMessage(PartyHandler.getPartyLeader(player.getName()) + " is the party leader!");
							
							// Print members
							for (String member : PartyHandler.getPartyMembers(player, "PartyMembers", plugin))
							{
								sender.sendMessage(member + " is part of the party.");
							}
							sender.sendMessage(partyUsage);
							sender.sendMessage(partyManageUsage);
						}
						else 
						{
							sender.sendMessage("You are not in a party!");
							sender.sendMessage(partyUsage);
							sender.sendMessage(partyManageUsage);
						}
					}
					else 
					{
						sender.sendMessage(notPM);
						sender.sendMessage(partyUsage);
						sender.sendMessage(partyManageUsage);
					}
				}
			}
			else if (args.length == 2)
			{
				if (args[0].equalsIgnoreCase("resource")) sender.sendMessage(resourceUsage);
				
				else if (args[0].equalsIgnoreCase("class")) sender.sendMessage(classUsage);
				
				// Print description and point stats for spell in args[1]
				else if (args[0].equalsIgnoreCase("points"))
				{
					String spell = args[1].replace("_", " ");
					
					if (plugin.getSpells().contains(spell))
					{	
						sender.sendMessage(spell + ": " + plugin.getSpells().getString(spell + ".Description"));
						sender.sendMessage("Attributes | Base    | Increase per Point");
						for (String attribute : plugin.getSpellPoints().getConfigurationSection(spell).getKeys(false))
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
				
				// Reload specified data file 
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
					// Check for items in inventory for the name 'Cooldown' and restart the cooldown if it's stuck
					if (args[1].equalsIgnoreCase("stuck"))
					{
						if (sender instanceof Player)
						{
							final Player player = (Player) sender;

							for (final String spell : plugin.getSaves().getConfigurationSection(player.getName() + ".Spells").getKeys(false))
							{
								for (final ItemStack item : player.getInventory().getContents())
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
					
					// ADMIN Check for items in inventory for the name 'Cooldown' and restart the cooldown if it's stuck 
					else if (args[1].equalsIgnoreCase("unstuck"))
					{
						if (sender instanceof Player)
						{
							final Player player = (Player) sender;

							for (final String spell : plugin.getSaves().getConfigurationSection(player.getName() + ".Spells").getKeys(false))
							{
								for (final ItemStack item : player.getInventory().getContents())
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

													ItemStack spellItem = Methods.spell(player.getName() + ".Spells." + spell, spell);
													item.setType(spellItem.getType());
													if (plugin.getSpells().getInt(spell + ".PlaceHolderDamage") == 0)
													{		
														item.setDurability((short) 0);
													}
													item.setData(spellItem.getData());
													item.setItemMeta(spellItem.getItemMeta());														
												}
											}
										}
									}
								}
							}
						}
						else sender.sendMessage(notPM);
					}
					
					// Print description of spell in args[1] and print if player have target spell
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
				
				else if (args[0].equalsIgnoreCase("party"))
				{
					if (sender instanceof Player)
					{
						Player player = (Player) sender;
						if (args[1].equalsIgnoreCase("create"))
						{
							// Check if party exists
							if (PartyHandler.getPartyLeader(player.getName()).isEmpty())
							{
								sender.sendMessage("You have created you own party!");
								player.setMetadata("PartyLeader", new FixedMetadataValue(plugin, player.getName()));
								player.setMetadata("PartyMembers", new FixedMetadataValue(plugin, player.getName()));
							}
							else sender.sendMessage("You are already in a party with " + PartyHandler.getPartyLeader(player.getName()));
						}
						else if (args[1].equalsIgnoreCase("leave"))
						{
							// Check if party exists
							if (!PartyHandler.getPartyLeader(player.getName()).isEmpty())
							{
								sender.sendMessage("You have left the party!");
								player.removeMetadata("PartyChat", plugin);
								// Check for party size
								if (PartyHandler.getPartyMembers(player, "PartyMembers", plugin).size() != 2)
								{
									// Check if quitting player is party leader
									if (player.getName().equals(PartyHandler.getPartyLeader(player.getName())))
									{
										if (player.hasMetadata("PartyMembers"))
										{
											List<String> members = new ArrayList<>();

											for (String member : PartyHandler.getPartyMembers(player, "PartyMembers", plugin))
											{
												if (!member.equals(player.getName())) members.add(member);
											}

											// Select new leader
											Player newLeader = plugin.getServer().getPlayerExact(PartyHandler.getPartyMembers(player, 
													"PartyMembers", plugin).get(0).toString());
											if (newLeader.equals(player))
											{
												newLeader = plugin.getServer().getPlayerExact(PartyHandler.getPartyMembers(player, "PartyMembers", plugin).get(1).toString());
											}

											// Setting up new party
											for (String member : PartyHandler.getPartyMembers(player, "PartyMembers", plugin))
											{
												PartyHandler.removeParty(member);
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
										
										for (String member : PartyHandler.getPartyMembers(player, "PartyMembers", plugin))
										{
											if (!member.equals(player.getName())) members.add(member);
										}			
										for (String member : PartyHandler.getPartyMembers(player, "PartyMembers", plugin))
										{
											plugin.getServer().getPlayerExact(member).removeMetadata("PartyMembers", plugin);
											if (!member.equals(player.getName()))
											{
												plugin.getServer().getPlayerExact(member).sendMessage(player.getName() + " has left the party!");
												
												plugin.getServer().getPlayerExact(member).setMetadata("PartyMembers", 
														new FixedMetadataValue(plugin, members.remove(member)));
											}
										}
										PartyHandler.removeParty(player.getName());
									}
								}
								else
								{
									for (String playerName : PartyHandler.getPartyMembers(player, "PartyMembers", plugin))
									{
										PartyHandler.removeParty(playerName);
										plugin.getServer().getPlayerExact(playerName).sendMessage("Everyone left your party!");
									}
								}
							}
							else sender.sendMessage("You are not in a party!");
						}
						else if (args[1].equalsIgnoreCase("chat"))
						{
							// Check if player is in party
							if (player.hasMetadata("PartyLeader"))
							{
								// Check if player has used this command before
								if (player.hasMetadata("PartyChat"))
								{
									// Check for value of the metadata
									if (player.getMetadata("PartyChat").get(0).value().toString().equalsIgnoreCase("true"))
									{
										player.removeMetadata("PartyChat", plugin);
										player.setMetadata("PartyChat", new FixedMetadataValue(plugin, "false"));
										player.sendMessage("You have now taken party chat off.");
									}
									else if (player.getMetadata("PartyChat").get(0).value().toString().equalsIgnoreCase("false"))
									{
										player.removeMetadata("PartyChat", plugin);
										player.setMetadata("PartyChat", new FixedMetadataValue(plugin, "true"));
										player.sendMessage("You have now taken party chat on.");
									}
								}
								else
								{
									player.setMetadata("PartyChat", new FixedMetadataValue(plugin, "true"));
									player.sendMessage("You have now taken party chat on.");
								}
							}
							else sender.sendMessage("You are not in a party!");
						}
						else 
						{
							sender.sendMessage(partyUsage);
							sender.sendMessage(partyManageUsage);
						}
					}
					else sender.sendMessage(notPM);
				}
			}
			else if (args.length == 3)
			{
				if (args[0].equalsIgnoreCase("resource")) sender.sendMessage(resourceUsage);
				
				else if (args[0].equalsIgnoreCase("class")) sender.sendMessage(classUsage);

				// Set attribute(args[2]) for spell(args[1]) for player 
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
									if (Math.abs(plugin.getSaves().getInt(player.getName() + ".Spells." + spell + "." + args[2])) < 
											(Math.abs(2 * plugin.getSpellPoints().getInt(spell + "." + args[2]))))
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
					// Give spell in players inventory if player have spell
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
					
					// Remove spell in players inventory if player have spell
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

				else if (args[0].equalsIgnoreCase("party"))
				{
					if (sender instanceof Player)
					{
						Player player = (Player) sender;
						String otherPlayer = args[2];

						// Check if other player is online
						if (plugin.getServer().getPlayerExact(otherPlayer) != null && !otherPlayer.equals(player.getName()))
						{
							if (args[1].equalsIgnoreCase("invite"))
							{
								// Check if player has rights to invite
								if (player.hasMetadata("PartyLeader") && player.getMetadata("PartyLeader").equals(player.getName()))
								{
									// Check if other player is in a party
									if (!plugin.getServer().getPlayerExact(otherPlayer).hasMetadata("PartyLeader"))
									{
										if (PartyHandler.partyInviteMap.containsKey(otherPlayer))
										{
											PartyHandler.partyInviteMap.remove(otherPlayer);
											PartyHandler.partyInviteMap.put(otherPlayer, player.getName());
										}
										else PartyHandler.partyInviteMap.put(otherPlayer, player.getName());
									}
									else sender.sendMessage(otherPlayer + " is already in a party!");
								}
								else sender.sendMessage("You are not party leader!");
							}
							else if (args[1].equalsIgnoreCase("join"))
							{
								// Check if player is not in a party
								if (!player.hasMetadata("PartyLeader"))
								{
									// Check if other player is a party leader
									if (plugin.getServer().getPlayerExact(otherPlayer).hasMetadata("PartyLeader") && 
											plugin.getServer().getPlayerExact(otherPlayer).getMetadata("PartyLeader").equals(otherPlayer))
									{
										if (plugin.getServer().getPlayerExact(otherPlayer).hasMetadata("PartyMembers"))
										{
											// Check if party is full
											if (PartyHandler.getPartyMembers(plugin.getServer().getPlayerExact(otherPlayer), "PartyMembers", plugin).size() < 4)
											{
												// Check if other player had invited player to party
												if (PartyHandler.partyInviteMap.containsKey(otherPlayer) && 
														PartyHandler.partyInviteMap.get(player.getName()).equals(otherPlayer))
												{
													PartyHandler.partyInviteMap.remove(player.getName());
													
													List<String> members = new ArrayList<>();
													
													for (String member : PartyHandler.getPartyMembers(player, "PartyMembers", plugin))
													{
														members.add(member);
													}
													members.add(player.getName());
													player.setMetadata("PartyLeader", new FixedMetadataValue(plugin, otherPlayer));
													
													for (String member : PartyHandler.getPartyMembers(player, "PartyMembers", plugin))
													{
														plugin.getServer().getPlayerExact(member).removeMetadata("PartyMembers", plugin);
														plugin.getServer().getPlayerExact(member).sendMessage(player.getName() + " has joined your party!");

														plugin.getServer().getPlayerExact(member).setMetadata("PartyMembers", 
																new FixedMetadataValue(plugin, members));
													}
												}
												else sender.sendMessage(otherPlayer + " has not invited you to his party!");
											}
											else sender.sendMessage(otherPlayer + "'s party is already ");
										}
										else System.out.println("Error in " + CommandHandler.class.getName() + " under /adventures party join!");
									}
									else sender.sendMessage(otherPlayer + " is not party leader!");
								}
								else sender.sendMessage("You are in a party!");								
							}
							else sender.sendMessage(partyManageUsage);
						}
						else 
						{
							if (otherPlayer.equals(player.getName()))
							{
								sender.sendMessage("You can not invite/join yourself! I feel sorry for you, forever alone.");
							}
							else sender.sendMessage(otherPlayer + " is not online!");
						}
					}
					else sender.sendMessage(notPM);
				}
			}
			else if (args.length == 4)
			{
				if (args[0].equalsIgnoreCase("points")) sender.sendMessage(pointsUsage);
				
				else if (args[0].equalsIgnoreCase("resource")) sender.sendMessage(resourceUsage);
				
				// Set the class of a player
				else if (args[0].equalsIgnoreCase("class"))
				{
					if (args[1].equalsIgnoreCase("set"))
					{
						if (Bukkit.getPlayerExact(args[2]) != null)
						{
							if (Bukkit.getPlayerExact(args[2]).isOnline())
							{
								final String player = args[2];
								Set<String> classes = plugin.getClasses().getKeys(false);
								if (classes.contains(args[3]))
								{	
									boolean switchClass = true;
									if (args[3].equals("Admin") && !Bukkit.getPlayerExact(player).isOp()) switchClass = false;

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

										Bukkit.getPlayerExact(player).sendMessage("You are now a " + args[3]);

										plugin.saveSaves();

										Bukkit.getPlayerExact(player).setLevel(1);
										new BukkitRunnable() 
										{
											@Override
											public void run() 
											{
												Bukkit.getPlayerExact(player).setLevel(0);
												Bukkit.getPlayerExact(player).setExp(0);
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
						else sender.sendMessage("Target player must be online!");
					}
					else sender.sendMessage(classUsage);
				}
				else if (args[0].equalsIgnoreCase("spell"))
				{
					// Replace spell in players inventory with another one from players spell repository
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
				
				// Checking command writer made mistake then printing message
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
				// ADMIN Set attribute(args[3]) for spell(args[2]) for player 
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
				
				// ADMIN add, sub and sets players resource
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

									// Maybe Bug?
									//plugin.getServer().getScheduler().runTaskTimer(plugin, 
									//		new ResourceRegenTask(Bukkit.getPlayer(player), plugin), 0, 20);
									
									plugin.saveSaves();
								}
							}
							else sender.sendMessage(resourceUsage);
						}
						else sender.sendMessage("Target resource don't have that node!");
					}
					else sender.sendMessage("Target player don't have that resource in the save file!");
				}
				
				// Checking command writer made mistake then printing message
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
