package dev.sylus.HungerGamesCore.Tasks;

import dev.sylus.HungerGamesCore.Enums.GameState;
import dev.sylus.HungerGamesCore.Files.Databases;
import dev.sylus.HungerGamesCore.Game.ChestManager;
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
    ChestManager chestManager;

    public GameRunTask(Game game, HungerGamesCore mainInstance, Databases databasesInstance, ChestManager chestManagerInstance) {
        this.game = game;
        // this.game.assignSpawnPositions();
        main = mainInstance;
        databases = databasesInstance;
        chestManager = chestManagerInstance;
    }

    @Override
    public void run() {
        game.setMovement(false);
        if (startIn <= 0) {
            this.cancel();
            game.setState(GameState.gameState.ACTIVE, "Game run task");
            Bukkit.broadcastMessage("§a[!] The game has started.");
            game.setMovement(true);
            game.setVunrability(true);
            new GameTimer(main, game, databases, chestManager).runTaskTimer(main, 0, 20);
        } else if (startIn == 10 || startIn <= 5) {
            main.refreshScorebordAll();
            Bukkit.broadcastMessage("§eThe game will begin in §c" + startIn + " §esecond" + (startIn == 1 ? "" : "s"));
            for (Player players: Bukkit.getOnlinePlayers()) {
                players.playSound(players.getLocation(), Sound.BLOCK_TRIPWIRE_ATTACH, 1, 1);
            }
        }
        startIn--;
    }

}
