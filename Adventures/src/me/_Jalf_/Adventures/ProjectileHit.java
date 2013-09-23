package me._Jalf_.Adventures;

import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
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
				for (int i = 1;i < 50;i++)
				{
					if (damager.hasMetadata("Damage" + i)) dmg = i;
					if (damager.hasMetadata("Explosive" + i)) exp = i;
					if (damager.hasMetadata("Fire" + i)) fire = i;
					// Potion Effects
					if (damager.hasMetadata("Slow" + i)) slow = i;
					if (damager.hasMetadata("Weakness" + i)) weakness = i;
					if (damager.hasMetadata("Blindness" + i)) blindness = i;
					if (damager.hasMetadata("Confusion" + i)) confusion = i;
					if (damager.hasMetadata("Hunger" + i)) hunger = i;
					if (damager.hasMetadata("Poison" + i)) poison = i;
					if (damager.hasMetadata("Wither" + i)) wither = i;
				}
				if (dmg != 0) ((LivingEntity) entity).damage((double) dmg);
				if (exp != 0) damager.getLocation().getWorld().createExplosion(damager.getLocation().getX(), damager.getLocation().getY(), damager.getLocation().getZ(), exp, false, false);
				if (fire != 0) entity.setFireTicks(fire*20);
				// Potion Effects
				if (slow != 0) ((LivingEntity) entity).addPotionEffect(PotionEffectType.SLOW.createEffect(200, slow));
				if (weakness != 0) ((LivingEntity) entity).addPotionEffect(PotionEffectType.WEAKNESS.createEffect(200, weakness));
				if (blindness != 0) ((LivingEntity) entity).addPotionEffect(PotionEffectType.BLINDNESS.createEffect(100 * blindness, 5));
				if (confusion != 0) ((LivingEntity) entity).addPotionEffect(PotionEffectType.CONFUSION.createEffect(200, confusion));
				if (hunger != 0) ((LivingEntity) entity).addPotionEffect(PotionEffectType.HUNGER.createEffect(200, hunger));
				if (poison != 0) ((LivingEntity) entity).addPotionEffect(PotionEffectType.POISON.createEffect(poison*4*20, 2));
				if (wither != 0) ((LivingEntity) entity).addPotionEffect(PotionEffectType.WITHER.createEffect(200, wither));
				
				damager.remove();
			}
		}
	}
	@EventHandler
	 public void noFireballFire(BlockIgniteEvent event){
	    if(event.getCause().equals(IgniteCause.FIREBALL)) event.setCancelled(true); 
	}
	@EventHandler
	public void projectileHit(ProjectileHitEvent event) 
	{
		Entity entity = event.getEntity();
		
		if (entity instanceof Arrow) entity.remove();
	}
}
