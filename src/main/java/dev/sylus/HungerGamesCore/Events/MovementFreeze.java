package dev.sylus.HungerGamesCore.Events;

import dev.sylus.HungerGamesCore.Game.Game;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.util.Vector;


public class MovementFreeze implements Listener {

    Game game;

    public MovementFreeze(Game gameInstance){
        game = gameInstance;
    }

    @EventHandler
    public void playerMoveEvent(PlayerMoveEvent event){
        if(!(game.getMovement())) {
            if(event.getTo().getBlockX() > event.getFrom().getBlockX() || event.getTo().getBlockX() < event.getFrom().getBlockX() || event.getTo().getBlockZ() > event.getFrom().getBlockZ() || event.getTo().getBlockZ() < event.getFrom().getBlockZ()) {
                event.getPlayer().teleport(event.getFrom());
            }
        }
/*
        Player player = event.getPlayer();
        if (game.getMovement()){
            if (player.getWalkSpeed() == 0.2F){
                return;
            }
            player.setWalkSpeed(0.2F);
            return;
        }
        event.setCancelled(true);
        player.setVelocity( new Vector(0, 0, 0));
        Location from = event.getFrom();
        player.teleport(from);

 */
    }
}
