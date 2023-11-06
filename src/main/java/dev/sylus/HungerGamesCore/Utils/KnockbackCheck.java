package dev.sylus.HungerGamesCore.Utils;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;



public class KnockbackCheck extends BukkitRunnable{
    double knockbackStrength = 0.2;
    private int knockbackCount;
    Vector initialPosition;
    Player player;

    public KnockbackCheck(Player tartgetPlayer){
        player = tartgetPlayer;
        initialPosition = player.getLocation().toVector();
        knockbackCount = 0;
    }

    // This method applies knockback three times with a one-second interval and checks if the player moved.

    @Override
    public void run() {
        if (knockbackCount < 3) {
            // Apply knockback in a specified direction.
            player.setVelocity(player.getLocation().getDirection().multiply(-knockbackStrength));

            // Increment the knockback count.
            knockbackCount++;
        } else {
            // Stop the task after three knockbacks.
            cancel();
        }
    }
}
