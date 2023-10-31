package dev.sylus.HungerGamesCore.Events;

import dev.sylus.HungerGamesCore.Enums.GameState;
import dev.sylus.HungerGamesCore.Files.Databases;
import dev.sylus.HungerGamesCore.Files.Files;
import dev.sylus.HungerGamesCore.Game.Game;
import dev.sylus.HungerGamesCore.Game.Scorebord;
import dev.sylus.HungerGamesCore.Tasks.GameTimer;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;

import java.util.logging.Level;

public class PlayerDeathEvent implements Listener {

    Game game;
    Files files;
    Entity killer;
    String killerName;
    GameTimer gameTimer;
    Scorebord scorebord;
    Databases databases;

    public PlayerDeathEvent(Game gameInstance, Files filesInstance, GameTimer gameTimerInstance, Scorebord scorebordInstance, Databases databasesInstance){
        game = gameInstance;
        files = filesInstance;
        gameTimer = gameTimerInstance;
        scorebord = scorebordInstance;
        databases = databasesInstance;
    }

    @EventHandler
    public void onDeath(org.bukkit.event.entity.PlayerDeathEvent event) {
        Bukkit.getLogger().log(Level.SEVERE, "PLAYER JUST DIED");
        Player player = event.getEntity();
        GameState.gameState gameState = game.getState(); // Possible states: PREGAME, STARTING, ENDGAME and TESTING
        if (gameState == GameState.gameState.TESTING || gameState == GameState.gameState.PREGAME){
            player.sendMessage(ChatColor.RED + "You died when it was not possible, please report this to an admin");
            return;
        }
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


        String playerName = player.getName();
        Location location = player.getLocation();

        World world = player.getWorld();
        world.strikeLightningEffect(location);

        Location location2 = new Location(world,x, y, z);
        player.setGameMode(GameMode.SPECTATOR);

        if (!(event.getEntity().getKiller() == null)){
            this.killer = event.getEntity().getKiller();
            this.killerName = killer.getName();
        }


        if (event.getDeathMessage().contains("was slain by")) {
            if (killerName != null) {
                switch ((int) (Math.random() * 3)){
                    case 0:
                        event.setDeathMessage("§c" + playerName + " §ewas smacked on the head by " + "§6" + killerName);
                        break;
                    case 1:
                        event.setDeathMessage("§c" + killerName + " §edrop kicked " + "§6" + playerName + " §eInto orbit");
                        break;
                    case 2:
                        event.setDeathMessage("§c" + playerName + " §ewas killed by " + "§6" + killerName);
                        break;
                    case 3:
                        event.setDeathMessage("§c" + playerName + " §ewas unalived by " + "§6" + killerName);
                        break;
                }
            }
        } else if (event.getDeathMessage().contains("went up in flames")) {
            switch ((int) (Math.random() * 3)){
                case 1:
                    event.setDeathMessage("§c" + playerName + " §ewent through the fire and flames");
                    break;
                case 2:
                    event.setDeathMessage("§c" + playerName + " §etoasted themselves");
                    break;
                case 3:
                    event.setDeathMessage("§c" + playerName + " §eliked toast so much they became some");
                    break;
                case 4:
                    event.setDeathMessage("§c" + playerName + " §eburnt till until they were a crisp");
                    break;
            }
        } else if (event.getDeathMessage().contains("tried to swim in lava")) {
            switch ((int) (Math.random() * 3 + 1)){
                case 1:
                    event.setDeathMessage("§c" + playerName + " §etried to use a hardcore jacuzzi");
                    break;
                case 2:
                    event.setDeathMessage("§c" + playerName + " §efell into some forbidden orange juice");
                    break;
                case 3:
                    event.setDeathMessage("§c" + playerName + " §edecided to take the plunge, unfortunately it was into lava");
                    break;
                case 4:
                    event.setDeathMessage("§c" + playerName + " §eslipped into a pit of lava");
                    break;
            }

        } else if (event.getDeathMessage().contains("was shot by")) {
            if (!killerName.equals("")) {
                switch ((int) (Math.random() * 3 + 1)){
                    case 1:
                        event.setDeathMessage("§c" + playerName + " §ewas poked by an arrow that was shot by " + "§6" + killerName);
                        break;
                    case 2:
                        event.setDeathMessage("§c" + killerName + " §efilled " + "§6" + playerName + " §ewith sharp sticks");
                        break;
                    case 3:
                        event.setDeathMessage("§c" + playerName + " §ewas noscoped by" + "§6" + killerName);
                        break;
                    case 4:
                        event.setDeathMessage("§c" + playerName + " §egot sniped by " + "§6" + killerName);
                        break;
                }
            }
        } else if (event.getDeathMessage().contains("fell from a high place")) {
            switch ((int) (Math.random() * 3)){
                case 1:
                    event.setDeathMessage("§c" + playerName + " §etried to fly");
                    break;
                case 2:
                    event.setDeathMessage("§c" + playerName + " §etripped off a cliff");
                    break;
                case 3:
                    event.setDeathMessage("§c" + playerName + " §edecided to take the plunge, unfortunately it was off a cliff");
                    break;
                case 4:
                    event.setDeathMessage("§c" + playerName + " §efell from the sky");
                    break;
            }
            
        } else {
            event.setDeathMessage("§c" + playerName + " §edied");
        }
        scorebord.refreshScorebordAll();
    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent event){
        Player player = event.getPlayer();
        player.sendMessage(ChatColor.RED + "You were killed! RIP!");
        player.sendMessage(ChatColor.YELLOW + "You can spectate the game or go back to the lobby with" + ChatColor.RED + " /lobby"); // Let frontend design this
        player.sendTitle( ChatColor.RED + "YOU DIED", "", 10, 20, 10);

        double x = files.getMapRespawnX();
        double y = files.getMapRespawnY();
        double z = files.getMapRespawnZ();

        World world = player.getWorld();
        Location location = new Location(world,x,y,z);
        event.setRespawnLocation(location);
    }

}

