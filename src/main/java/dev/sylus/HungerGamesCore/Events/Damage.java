package dev.sylus.HungerGamesCore.Events;

import dev.sylus.HungerGamesCore.Game.Game;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.EventListener;

public class Damage implements Listener {

    Game game;

    public Damage(Game gameInstance) {
        game = gameInstance;
    }

    @EventHandler
    public void playerMoveEvent(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player){
            if (!(game.getDamageState())){
                event.setCancelled(true);
            }
        }

    }
}
