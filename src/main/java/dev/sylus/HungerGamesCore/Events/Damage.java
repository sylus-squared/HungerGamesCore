package dev.sylus.HungerGamesCore.Events;

import dev.sylus.HungerGamesCore.Game.Game;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;

import java.util.EventListener;

public class Damage implements EventListener {
    Game game;
    public Damage(Game gameInstance){
        game = gameInstance;
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event){
        if (event.getEntity() instanceof Player && !(game.getDamageState())){ // Entity is a player and damage is false
            event.setCancelled(true);
        }
    }
}
