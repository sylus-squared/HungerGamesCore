package dev.sylus.HungerGamesCore.Events;

import org.bukkit.Effect;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;


public class ProjectileKnockback implements Listener {

    public ProjectileKnockback() {
    }

    @EventHandler
    public void onSnowball(ProjectileHitEvent event) {
        Entity target = event.getHitEntity();
        if (target == null)
            return;

        if (!(event.getEntity() instanceof Snowball || event.getEntity() instanceof Egg)){
            return;
        }

        double knockBackStrength = 2;
        Vector knockBack = event.getEntity().getVelocity().normalize().multiply(knockBackStrength);
        target.setVelocity(target.getVelocity().add(knockBack));

        if (event.getEntity().getName().equals("&bSlowball") && target instanceof Player){
            LivingEntity livingEntity = (LivingEntity) target;
            livingEntity.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 5, 1));
        } else if (event.getEntity().getName().equals("&6Hard Boiled Egg") && target instanceof  Player) {
            LivingEntity livingEntity = (LivingEntity) target;
            livingEntity.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 5, 1));
        }
    }
}
