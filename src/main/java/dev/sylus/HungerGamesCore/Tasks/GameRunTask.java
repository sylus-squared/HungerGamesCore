package dev.sylus.HungerGamesCore.Tasks;

import dev.sylus.HungerGamesCore.Enums.GameState;
import dev.sylus.HungerGamesCore.Files.Databases;
import dev.sylus.HungerGamesCore.Game.Game;
import dev.sylus.HungerGamesCore.Game.Scorebord;
import dev.sylus.HungerGamesCore.HungerGamesCore;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class GameRunTask extends BukkitRunnable {
    private Game game;
    private int startIn = 10; // Seconds

    HungerGamesCore main;
    Databases databases;

    public GameRunTask(Game game, HungerGamesCore mainInstance, Databases databasesInstance) {
        this.game = game;
        // this.game.assignSpawnPositions();
        main = mainInstance;
        databases = databasesInstance;
    }

    @Override
    public void run() {
        game.setMovement(false);
        if (startIn <= 1) {
            this.cancel();
            game.setState(GameState.gameState.ACTIVE, "Game run task");
            Bukkit.broadcastMessage("§a[!] The game has started.");
            game.setMovement(true);
            new GameTimer(main, game, databases).runTaskTimer(main, 0, 20);
        } else {
            main.refreshScorebordAll();
            Bukkit.broadcastMessage("§cThe game will begin in §e" + startIn + " §csecond" + (startIn == 1 ? "" : "s"));
            startIn--;
            for (Player players: Bukkit.getOnlinePlayers()) {
                players.playSound(players.getLocation(), Sound.BLOCK_TRIPWIRE_ATTACH, 1, 1);
            }
        }
    }

}
