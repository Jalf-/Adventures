package me._Jalf_.Adventures;

import java.io.File;
import java.io.IOException;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import me._Jalf_.Adventures.Spells.*;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.DisplaySlot;

public class Main extends JavaPlugin
{
	private Logger log = Logger.getLogger("Minecraft");	
	
	@Override
	public void onDisable()
	{	
		PlayerResource.playerNoResource = null;
		
		Player[] players = getServer().getOnlinePlayers();

		for (Player player : players)
		{
			if (!getSaves().getString(player.getName() + ".Class").equalsIgnoreCase("Default"))
			{
				if (getSaves().getConfigurationSection(player.getName() + ".Spells") != null)
				{
					Set<String> playerSpells = getSaves().getConfigurationSection(player.getName() + ".Spells").getKeys(false);

					for (String spell : playerSpells)
					{
						ItemStack[] items = player.getInventory().getContents();

						for (ItemStack item : items)
						{
							if (item != null)
							{
								if (item.hasItemMeta() && item.getItemMeta().hasDisplayName())
								{
									if (item.getItemMeta().getDisplayName().equalsIgnoreCase("Cooldown") && item.getItemMeta().hasLore())
									{
										if (item.getItemMeta().getLore().get(0).replace(" is on cooldown!", "").equalsIgnoreCase(spell))
										{
											ItemStack spellItem = Methods.spell(player.getName() + ".Spells." + spell, spell);
											item.setType(spellItem.getType());
											if (this.getSpells().getInt(spell + ".PlaceHolderDamage") == 0)
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
					String resourceName = Methods.getPlayerResourceName(player.getName());      
					int resourceNow = ScoreboardHandler.getScore(player.getName(), DisplaySlot.SIDEBAR, Bukkit.getOfflinePlayer(resourceName)); 
					getSaves().set(player.getName() + ".Resource." + resourceName + ".Now", resourceNow);
					saveSaves();
				}
			}
		}
		reloadSaves();
		saveSaves();
		
		getServer().getScheduler().cancelTasks(this);
	}
	
	public void onEnable()
	{
		new SpellHandler(this);  
	    new PlayerResource(this);
	    new ScoreboardHandler(this);
	    new CommandHandler(this);
	    new DontDropSpell(this);
	    new ProjectileHit(this);
	    new ClassHandler(this);
	    new PartyHandler(this);
	    
	    // Spell Register
	    new FireWall(this);
	    new SlowAura(this);
	    new ExplodingArrow(this);
	    new FireWalk(this);
	    new ShotgunArrow(this);
	    new SmokeScreen(this);
	    new Bombardment(this);
	    new IronPrison(this);
	    new ArrowRain(this);
	    new HealingWell(this);
	    new WarpBurst(this);
	    new StoneWall(this);
	    new IceWall(this);
	    new Meteor(this);
	    new Comet(this);
	    new ArrowLine(this);
	    new HiddenMine(this);
	    new FireBolt(this);
	    new FireBall(this);
	    new SnowBall(this);
	    new IceBolt(this);
	    new BlindingArrow(this);
	    new PoisonDart(this);
	    new EggThrow(this);
	    new Leap(this);
	    new Quake(this);
	    new IceRing(this);
	    new FireRing(this);
	    new IceSphere(this);
	    new FireBreath(this);
	    new LightningStorm(this);
	    new LightningShield(this);
	    new ChainLightning(this);
	    new PushAura(this);
	    new StoneRing(this);
	    new Blizzard(this);
	    new FoulPlay(this);
	    
	    // End of Spell Register
	    
	    File configFile = new File(this.getDataFolder(), "config.yml");	    
	    if (!configFile.exists())
	    {
	    	getConfig().options().copyDefaults(true);
	    	saveConfig();
	    }
		reloadSaves();
		reloadSpells();
		
		getCommand("adventures").setExecutor(new CommandHandler(this));
		
		for (Player player : Bukkit.getOnlinePlayers())
		{
			if (getClasses().contains(getSaves().getString(player.getName() + ".Class")))	
			{
				if (!getSaves().getString(player.getName() + ".Class").equalsIgnoreCase("Default"))
				{
					String resourceName = Methods.getPlayerResourceName(player.getName());
					ScoreboardHandler.registerBoard(player.getName(), resourceName);
				}
				getServer().getScheduler().runTaskTimer(this, new ResourceRegenTask(player, this), 0, 20);
			}
		}
	}	
	
    private FileConfiguration savesConfig = null;
    private File savesConfigFile = null;
    
    public void reloadSaves() 
    {
        if (savesConfigFile == null) 
        {
        savesConfigFile = new File(getDataFolder(), "saves.yml");
        }
        savesConfig = YamlConfiguration.loadConfiguration(savesConfigFile);
    } 
    
    public FileConfiguration getSaves() 
    {
        if (savesConfig == null) 
        {
            reloadSaves();
        }
        return savesConfig;
    } 
    
    public void saveSaves() 
    {
        if (savesConfig == null || savesConfigFile == null) 
        {
        	reloadSaves();
        }
        try 
        {
            getSaves().save(savesConfigFile);
        } catch (IOException ex) 
        {
        	log.log(Level.SEVERE, "Could not save config to " + savesConfigFile, ex);
        }
    }    
    private FileConfiguration spellsConfig = null;
    private File spellsConfigFile = null;
    
    public void reloadSpells() 
    {
        if (spellsConfigFile == null) 
        {
        spellsConfigFile = new File(getDataFolder(), "spells.yml");
        }
        spellsConfig = YamlConfiguration.loadConfiguration(spellsConfigFile);
    } 
    
    public FileConfiguration getSpells() 
    {
        if (spellsConfig == null) 
        {
            reloadSpells();
        }
        return spellsConfig;
    }
    
    public void saveSpells() 
    {
        if (spellsConfig == null || spellsConfigFile == null) 
        {
        	reloadSpells();
        }
        try 
        {
            getSpells().save(spellsConfigFile);
        } 
        catch (IOException ex) 
        {
        	log.log(Level.SEVERE, "Could not save config to " + spellsConfigFile, ex);
        }
    }
    private FileConfiguration spellPointsConfig = null;
    private File spellPointsConfigFile = null;
    
    public void reloadSpellPoints() 
    {
        if (spellPointsConfigFile == null) 
        {
        	spellPointsConfigFile = new File(getDataFolder(), "spellPoints.yml");
        }
        spellPointsConfig = YamlConfiguration.loadConfiguration(spellPointsConfigFile);
    } 
    
    public FileConfiguration getSpellPoints() 
    {
        if (spellPointsConfig == null) 
        {
            reloadSpellPoints();
        }
        return spellPointsConfig;
    }
    
    public void saveSpellsPoints() 
    {
        if (spellPointsConfig == null || spellPointsConfigFile == null) 
        {
        	reloadSpellPoints();
        }
        try 
        {
            getSpellPoints().save(spellPointsConfigFile);
        } 
        catch (IOException ex) 
        {
        	log.log(Level.SEVERE, "Could not save config to " + spellPointsConfigFile, ex);
        }
    }
    
    private FileConfiguration classesConfig = null;
    private File classesConfigFile = null;
    
    public void reloadClasses() 
    {
        if (classesConfigFile == null) 
        {
        	classesConfigFile = new File(getDataFolder(), "classes.yml");
        }
        classesConfig = YamlConfiguration.loadConfiguration(classesConfigFile);
    } 
    
    public FileConfiguration getClasses() 
    {
        if (classesConfig == null) 
        {
            reloadClasses();
        }
        return classesConfig;
    }
    
    public void saveClasses() 
    {
        if (classesConfig == null || classesConfig == null) 
        {
        	reloadClasses();
        }
        try 
        {
            getClasses().save(classesConfigFile);
        } 
        catch (IOException ex) 
        {
        	log.log(Level.SEVERE, "Could not save config to " + classesConfigFile, ex);
        }
    }
}
