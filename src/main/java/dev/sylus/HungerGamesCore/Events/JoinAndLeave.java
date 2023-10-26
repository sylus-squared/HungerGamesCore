package dev.sylus.HungerGamesCore.Events;

import dev.sylus.HungerGamesCore.Enums.GameState;
import dev.sylus.HungerGamesCore.Files.Databases;
import dev.sylus.HungerGamesCore.Files.Files;
import dev.sylus.HungerGamesCore.Game.Game;
import dev.sylus.HungerGamesCore.Game.Scorebord;
import dev.sylus.HungerGamesCore.Tasks.GameTimer;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.logging.Level;

public class JoinAndLeave implements Listener {
    Game game;
    Files files;
    Scorebord scorebord;
    GameTimer gameTimer;
    Databases databases;
    public JoinAndLeave(Game gameInstance, Files filesInstance, Scorebord scorebordInstance, GameTimer gameTimerInstance, Databases databasesInstance){
        game = gameInstance;
        files = filesInstance;
        scorebord = scorebordInstance;
        gameTimer = gameTimerInstance;
        databases = databasesInstance;
    }

    @EventHandler
    public void playerJoin(PlayerJoinEvent event){
        Player player = event.getPlayer();
        System.out.println(game.getPlayerNumbers() + "PLAYERNUMBERS");
        if (game.getPlayerNumbers() >= files.getPlayerCap()){
            player.sendMessage(ChatColor.RED + "The game is full, you have been put into spectator mode");
            player.setGameMode(GameMode.SPECTATOR);
            scorebord.refreshScorebordAll();
            return;
        } else if (!(game.getState().equals(GameState.gameState.PREGAME) || game.getState().equals(GameState.gameState.TESTING))) {
            player.sendMessage(ChatColor.RED + "The game has already started, you have been put into spectator mode");
            player.setGameMode(GameMode.SPECTATOR);
            scorebord.refreshScorebordAll();
            return;
        } else {
            game.addPlayer(player);
            scorebord.refreshScorebordAll();
            player.sendMessage(ChatColor.LIGHT_PURPLE + "The game will start shortly, please wait");
            player.sendMessage(ChatColor.RED + "Currently playing build number: " + ChatColor.YELLOW + "122" + ChatColor.RED + " and testing the database system"); // Change this to colour codes at some point
            if (!(player.hasPermission("hungergamescore.gameModeImmune") || game.getState() == GameState.gameState.TESTING)){
                player.setGameMode(GameMode.ADVENTURE); // The default game mode will be adventure, this is just in case
            }
        }
        if (!(databases.isInDatabase(player.getUniqueId()))){
            databases.addNewPlayer(player.getUniqueId(), player.getName());
        }
        scorebord.refreshScorebordAll();
    }

    @EventHandler
    public void playerLeave(PlayerQuitEvent event){
        if (game.getState() == GameState.gameState.PREGAME || game.getState() == GameState.gameState.TESTING){
            game.removePlayer(event.getPlayer());
            scorebord.refreshScorebordAll();
            return;
        }
        Player player = event.getPlayer();

        game.removePlayer(player); // Sets the players state to dead

        if (game.getPlayerNumbers() == 1){ // There is only one player left, declare them winner
            gameTimer.stopGame(false);
            Bukkit.getLogger().log(Level.INFO, "Game OOOOOOVERRRR");
        } else if (game.getPlayerNumbers() == 2) { // There are 2 players left, its deathmatch time
            gameTimer.startDeathmatch();
        }

        double x = player.getLocation().getX();
        double y = player.getLocation().getY();
        double z = player.getLocation().getZ();

        Location location = player.getLocation();

        World world = player.getWorld();
        world.strikeLightningEffect(location);

        player.setGameMode(GameMode.SPECTATOR);
        scorebord.refreshScorebordAll();
    }
}
