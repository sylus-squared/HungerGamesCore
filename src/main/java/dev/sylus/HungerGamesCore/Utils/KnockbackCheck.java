package dev.sylus.HungerGamesCore.Utils;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;



public class KnockbackCheck extends BukkitRunnable{
    double knockbackStrength = 0.2;
    private int knockbackCount;
    Vector initialPosition;
    Player player;
    Player commandSender;
    Vector startingLocation;
    Vector endingLocation;

    public KnockbackCheck(Player tartgetPlayer, Player commandSenderLocal){
        player = tartgetPlayer;
        commandSender = commandSenderLocal;
        initialPosition = player.getLocation().toVector();
        knockbackCount = 0;
        startingLocation = tartgetPlayer.getLocation().toVector();
    }

    // This method applies knockback three times with a one-second interval and checks if the player moved.

    @Override
    public void run() {
        if (knockbackCount < 3) {
            knockbackCount++;
            commandSender.sendMessage(ChatColor.AQUA + "Run the knock-back check " + knockbackCount + "/3");
            // Apply knockback in a specified direction.
            player.setVelocity(player.getLocation().getDirection().multiply(-knockbackStrength));
        } else {
            // Stop the task after three knockbacks.
            endingLocation = player.getLocation().toVector();
            commandSender.sendMessage(ChatColor.AQUA + "Player " + player + " took " + Math.round(initialPosition.distance(endingLocation) * 100.0) / 100.0 + " blocks of knockback");
            if (Math.round(initialPosition.distance(endingLocation) * 100.0) / 100.0 == 0){
                commandSender.sendMessage(ChatColor.RED + "Player is most likely using antiKB");
            }
            cancel();
        }
    }
}
