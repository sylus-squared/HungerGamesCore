package dev.sylus.HungerGamesCore.Events;

import dev.sylus.HungerGamesCore.Game.Game;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerMoveEvent;


public class NoSleep implements Listener {

    public NoSleep(){
    }

    @EventHandler
    public void playerSleepEvent(PlayerBedEnterEvent event){
        event.setCancelled(true);
    }
}
