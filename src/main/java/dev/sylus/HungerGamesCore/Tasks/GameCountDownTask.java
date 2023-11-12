package dev.sylus.HungerGamesCore.Tasks;

import dev.sylus.HungerGamesCore.Enums.GameState;
import dev.sylus.HungerGamesCore.Files.Databases;
import dev.sylus.HungerGamesCore.Files.Files;
import dev.sylus.HungerGamesCore.Game.Border;
import dev.sylus.HungerGamesCore.Game.ChestManager;
import dev.sylus.HungerGamesCore.Game.Game;
import dev.sylus.HungerGamesCore.Game.Scorebord;
import dev.sylus.HungerGamesCore.HungerGamesCore;
import dev.sylus.HungerGamesCore.Utils.ServerUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.WorldBorder;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;
import java.util.logging.Level;

public class GameCountDownTask extends BukkitRunnable {
    private int time = 20;
    Game game;
    HungerGamesCore main;
    Scorebord scorebord;
    Files files;
    Databases databases;
    ChestManager chestManager;
    Border border;
    ServerUtil serverUtil;

    public GameCountDownTask(Game gameInstance, HungerGamesCore mainInstance, ChestManager chestManagerInstance, Files filesInstance, Border borderInstance, ServerUtil serverUtilInstance, Databases databasesInstance) {
        game = gameInstance;
        main = mainInstance;
        chestManager = chestManagerInstance;
        files = filesInstance;
        border = borderInstance;
        serverUtil = serverUtilInstance;
        databases = databasesInstance;
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
            game.setVunrability(false);
            border.setBorderSize(10000);

            List<Integer> locations = files.getConfig("worldData").getIntegerList("worldData.pedostalLocations");
            List<Player> playersAlive = game.getPlayers();
            int iterator = 0;

            for (int i = 0; i < game.getPlayers().size(); i ++){
               Player player = playersAlive.get(i);

                double x = locations.get(iterator);
                double y = locations.get(iterator + 1);
                double z = locations.get(iterator + 2);
                Location location = new Location(Bukkit.getWorld("world"), x, y, z);
                iterator = iterator + 3;
                player.teleport(location);
            }

            // Remember to set the correct world border size (Just bigger than the arena)
            // Actually im not going to bother with the border thingy, I will just use small maps
            new GameRunTask(game, main, databases, chestManager, serverUtil, border).runTaskTimer(main, 0, 20);
        } else {
            if (time == 15 || time == 10 || time <= 5) {
               Bukkit.broadcastMessage("§aYou will be teleported in §c" + time + " §aseconds");
            }
        }
    }
}
