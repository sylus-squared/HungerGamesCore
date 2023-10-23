package dev.sylus.HungerGamesCore.Tasks;

import dev.sylus.HungerGamesCore.Game.Border;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.logging.Level;

public class BorderShrinkTask extends BukkitRunnable {
    Border border;
    int borderCountdown = 20;
    int moveCountdown = 60;
    boolean stopTask = false;
    public BorderShrinkTask(Border borderInstance){
        border = borderInstance;
        // Need to shrink the border by 10 blocks every 10 seconds

    }

    @Override
    public void run() {
        if (stopTask){
            this.cancel();
            return;
        }

        if (borderCountdown == 0){
            if (moveCountdown == 0){
                borderCountdown = 20;
                moveCountdown = 20;
                return;
            }
            Bukkit.getLogger().log(Level.INFO, "Moving the border");
            border.setNewSize(border.getCurrentSize() - 2);
            moveCountdown --;
            return;
        }
        Bukkit.getLogger().log(Level.INFO, "Running the border loop");
        borderCountdown --;
    }
}
