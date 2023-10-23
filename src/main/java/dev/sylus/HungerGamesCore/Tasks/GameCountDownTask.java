package dev.sylus.HungerGamesCore.Tasks;

import dev.sylus.HungerGamesCore.Enums.GameState;
import dev.sylus.HungerGamesCore.Files.Databases;
import dev.sylus.HungerGamesCore.Files.Files;
import dev.sylus.HungerGamesCore.Game.Game;
import dev.sylus.HungerGamesCore.Game.Scorebord;
import dev.sylus.HungerGamesCore.HungerGamesCore;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.util.logging.Level;

public class GameCountDownTask extends BukkitRunnable {
    private int time = 20;
    Game game;
    HungerGamesCore main;
    Scorebord scorebord;
    Files files;
    Databases databases;

    public GameCountDownTask(Game gameInstance, HungerGamesCore mainInstance) {
        game = gameInstance;
        main = mainInstance;
    }

    @Override
    public void run() {
        time -= 1;

        Bukkit.getLogger().log(Level.INFO, "Running the count down");
        game.setState(GameState.gameState.GAMESTART, "Count down task");

        if (time == 0) {
            // Start
            cancel();
            // Remember to set the correct world border size (Just bigger than the arena, it then needs to shrink)
            // Actually im not going to bother with the border thingy, I will just use small maps
            new GameRunTask(game, main).runTaskTimer(main, 0, 20);
        } else {
            if (time == 15 || time == 10 || time <= 5) {
               Bukkit.broadcastMessage("§aYou will be teleported in §c" + time + " §aseconds");
            }
        }
    }
}
