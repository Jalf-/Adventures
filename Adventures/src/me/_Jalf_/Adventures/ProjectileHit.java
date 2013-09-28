package me._Jalf_.Adventures;

import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.block.BlockIgniteEvent.IgniteCause;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.potion.PotionEffectType;

public class ProjectileHit implements Listener
{
	public static Main plugin;

	public ProjectileHit (Main plugin)
	{
		ProjectileHit.plugin = plugin;
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}
	@EventHandler
	public void onProjectileHit(EntityDamageByEntityEvent event)
	{
		Entity damager = event.getDamager();		
		Entity entity = event.getEntity();	
		if (damager instanceof Projectile || damager.hasMetadata("IsProjectile"))
		{	
			if (entity instanceof LivingEntity)
			{
				boolean doNegativeEffect = true;
				
				// Check if entity is a player
				if (entity instanceof Player)
				{
					// Check if projectile is shot by a player
					if (damager.hasMetadata("Shooter"))
					{
						String shooter = damager.getMetadata("Shooter").get(0).value().toString();

						// Check if shooter is online
						if (plugin.getServer().getPlayerExact(shooter) != null)
						{
							Player shooterPlayer = plugin.getServer().getPlayerExact(shooter);

							// Check if player is in a party
							if (PartyHandler.getPartyLeader(shooter).length() > 0)
							{
								// Check if entity hit is any in party
								for (int i = 0; i < PartyHandler.getPartyMembers(shooterPlayer, "PartyMembers", plugin).size(); i++)
								{
									if (PartyHandler.getPartyMembers(shooterPlayer, "PartyMembers", plugin).get(i).equals(((Player) entity).getName()))
									{
										doNegativeEffect = false;
										break;
									}
								}
							}
						}
					}
				}
				
				if (doNegativeEffect == true)
				{
					int dmg = 0;
					int exp = 0;
					int fire = 0;
					// Potion Effects
					int slow = 0;
					int weakness = 0;
					int blindness = 0;
					int confusion = 0;
					int hunger = 0;
					int poison = 0;
					int wither = 0;
					
					if (damager.hasMetadata("Damage")) dmg = damager.getMetadata("Damage").get(0).asInt();
					if (damager.hasMetadata("Explosive")) exp = damager.getMetadata("Explosive").get(0).asInt();
					if (damager.hasMetadata("Fire")) fire = damager.getMetadata("Fire").get(0).asInt();
					// Potion Effects
					if (damager.hasMetadata("Slow")) slow = damager.getMetadata("Slow").get(0).asInt();
					if (damager.hasMetadata("Weakness")) weakness = damager.getMetadata("Weakness").get(0).asInt();
					if (damager.hasMetadata("Blindness")) blindness = damager.getMetadata("Blindness").get(0).asInt();
					if (damager.hasMetadata("Confusion")) confusion = damager.getMetadata("Confusion").get(0).asInt();
					if (damager.hasMetadata("Hunger")) hunger = damager.getMetadata("Hunger").get(0).asInt();
					if (damager.hasMetadata("Poison")) poison = damager.getMetadata("Poison").get(0).asInt();
					if (damager.hasMetadata("Wither")) wither = damager.getMetadata("Wither").get(0).asInt();
					
					
//					for (int i = 1;i < 50;i++)
//					{
//						if (damager.hasMetadata("Damage" + i)) dmg = i;
//						if (damager.hasMetadata("Explosive" + i)) exp = i;
//						if (damager.hasMetadata("Fire" + i)) fire = i;
//						// Potion Effects
//						if (damager.hasMetadata("Slow" + i)) slow = i;
//						if (damager.hasMetadata("Weakness" + i)) weakness = i;
//						if (damager.hasMetadata("Blindness" + i)) blindness = i;
//						if (damager.hasMetadata("Confusion" + i)) confusion = i;
//						if (damager.hasMetadata("Hunger" + i)) hunger = i;
//						if (damager.hasMetadata("Poison" + i)) poison = i;
//						if (damager.hasMetadata("Wither" + i)) wither = i;
//					}
					if (dmg != 0) ((LivingEntity) entity).damage((double) dmg);
					if (exp != 0) damager.getLocation().getWorld().createExplosion(damager.getLocation().getX(), damager.getLocation().getY(), damager.getLocation().getZ(), exp, false, false);
					if (fire != 0) entity.setFireTicks(fire*20);
					// Potion Effects
					if (slow != 0) ((LivingEntity) entity).addPotionEffect(PotionEffectType.SLOW.createEffect(200, slow-1));
					if (weakness != 0) ((LivingEntity) entity).addPotionEffect(PotionEffectType.WEAKNESS.createEffect(200, weakness-1));
					if (blindness != 0) ((LivingEntity) entity).addPotionEffect(PotionEffectType.BLINDNESS.createEffect(100 * blindness, 5-1));
					if (confusion != 0) ((LivingEntity) entity).addPotionEffect(PotionEffectType.CONFUSION.createEffect(200 * confusion, 5-1));
					if (hunger != 0) ((LivingEntity) entity).addPotionEffect(PotionEffectType.HUNGER.createEffect(200, hunger-1));
					if (poison != 0) ((LivingEntity) entity).addPotionEffect(PotionEffectType.POISON.createEffect(poison*4*20, 2-1));
					if (wither != 0) ((LivingEntity) entity).addPotionEffect(PotionEffectType.WITHER.createEffect(200, wither-1));
				}
				damager.remove();
			}
		}
	}
	@EventHandler
	 public void noFireballFire(BlockIgniteEvent event)
	{
		// Cancel a fireball from setting fire to a block on impact
	    if(event.getCause().equals(IgniteCause.FIREBALL)) event.setCancelled(true); 
	}
	@EventHandler
	public void projectileHit(ProjectileHitEvent event) 
	{
		Entity entity = event.getEntity();
		
		// Removes projectile if it's equal to arrow
		if (entity instanceof Arrow) entity.remove();
	}
}