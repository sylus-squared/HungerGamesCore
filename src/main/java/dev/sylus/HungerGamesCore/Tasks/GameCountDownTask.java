package dev.sylus.HungerGamesCore.Tasks;

import dev.sylus.HungerGamesCore.Enums.GameState;
import dev.sylus.HungerGamesCore.Files.Databases;
import dev.sylus.HungerGamesCore.Files.Files;
import dev.sylus.HungerGamesCore.Game.ChestManager;
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
    ChestManager chestManager;

    public GameCountDownTask(Game gameInstance, HungerGamesCore mainInstance, ChestManager chestManagerInstance) {
        game = gameInstance;
        main = mainInstance;
        chestManager = chestManagerInstance;
    }

    @Override
    public void run() {
        time -= 1;
        if (!(game.getState() == GameState.gameState.GAMESTART)){
            game.setState(GameState.gameState.GAMESTART, "Count down task");
        }

        if (time == 0) {
            // Start
            cancel();
            // Remember to set the correct world border size (Just bigger than the arena, it then needs to shrink)
            // Actually im not going to bother with the border thingy, I will just use small maps
            new GameRunTask(game, main, databases, chestManager).runTaskTimer(main, 0, 20);
            game.setVunrability(false);
        } else {
            if (time == 15 || time == 10 || time <= 5) {
               Bukkit.broadcastMessage("§aYou will be teleported in §c" + time + " §aseconds");
            }
        }
    }
}
