package dev.sylus.HungerGamesCore.Tasks;

import dev.sylus.HungerGamesCore.Enums.GameState;
import dev.sylus.HungerGamesCore.Files.Databases;
import dev.sylus.HungerGamesCore.Files.Files;
import dev.sylus.HungerGamesCore.Game.ChestManager;
import dev.sylus.HungerGamesCore.Game.Game;
import dev.sylus.HungerGamesCore.Game.Scorebord;
import dev.sylus.HungerGamesCore.HungerGamesCore;
import org.bukkit.*;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.FireworkEffect.Builder;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.logging.Level;

public class GameTimer extends BukkitRunnable {
    HungerGamesCore main;
    Scorebord scorebord;
    Game game;
    Files files;
    Databases databases;
    Location location;
    World world;
    ChestManager chestManager;
    Player player;
    String playerName;
    int refillTimer = 20; // 300 5 minutes
    int secondHalfTimerCountdown = 30; // 180

    int deathmatchCountdown = 120;

    public GameTimer(HungerGamesCore mainInstance, Game gameInstance, Databases databasesInstance, ChestManager chestManagerInstance){
        main = mainInstance;
        game = gameInstance;
        files = new Files(main, "worldData.yml");
        databases = databasesInstance;
        scorebord = new Scorebord(game, files, databases, this, main);
        chestManager = chestManagerInstance;
    }

    @Override
    public void run() {
        if (game.getState() == GameState.gameState.ENDING){ // Stops the game if the game is over
            this.cancel();
            return;
        }

        if (game.getState() == GameState.gameState.ACTIVE){
            if (refillTimer == 0){
               // this.cancel(); not needed anymore, but I might in the future
                game.setState(GameState.gameState.SECONDHALF, "Game timer");
                Bukkit.broadcastMessage(ChatColor.RED + "Chest refilled");
                chestManager.resetChests();
                refillTimer = -1;
                scorebord.refreshScorebordAll();
            } else if (refillTimer == 60) {
                Bukkit.broadcastMessage("§eChests will be refilled in §c60 §eSeconds");
                refillTimer--;
                scorebord.refreshScorebordAll();
            } else if (refillTimer == 30) {
                Bukkit.broadcastMessage("§eChests will be refilled in §c30 §eseconds");
                refillTimer--;
                scorebord.refreshScorebordAll();
            } else if (refillTimer <= 5 && refillTimer >= 1) {
                Bukkit.broadcastMessage("§eChests will be refilled in §c" + refillTimer + " §eSeconds");
                refillTimer--;
                scorebord.refreshScorebordAll();
                for (Player players: Bukkit.getOnlinePlayers()){
                    players.playSound(players.getLocation(), Sound.BLOCK_TRIPWIRE_ATTACH, 1, 1);
                }
            } else if (refillTimer == -1) {
                // Do nothing

            } else {
                refillTimer--;
                scorebord.refreshScorebordAll();
        }

        } else if (game.getState() == GameState.gameState.SECONDHALF){
            if (secondHalfTimerCountdown == 0){ // Game is over, time for deathmatch
                game.setState(GameState.gameState.DEATHMATCH, "Game timer for the deathmatch");
                game.setVunrability(false);
                scorebord.refreshScorebordAll();

                // Remember to teleport the players here
                double x = files.getDeathmatchSpawnX();
                double y = files.getDeathmatchSpawnY();
                double z = files.getDeathmatchSpawnZ();
                Location location = new Location(Bukkit.getWorld("world"), x, y, z);

                for (Player players: Bukkit.getOnlinePlayers()){
                    players.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 60, 5));
                    players.teleport(location);
                }
                Bukkit.broadcastMessage(ChatColor.RED + "Players will become vulnerable in 10 seconds");
                return;
            }
            secondHalfTimerCountdown--;
            scorebord.refreshScorebordAll();
        } else { // Its deathmatch time
            if (deathmatchCountdown == 0) {
                this.cancel();
                this.stopGame(false);
                return;

            } else if (deathmatchCountdown == 60 || deathmatchCountdown == 30 || deathmatchCountdown == 15 || deathmatchCountdown == 10 || deathmatchCountdown <= 5) {
                Bukkit.broadcastMessage("§eA draw will be called in §c" + deathmatchCountdown + " §eseconds");
            } else if (deathmatchCountdown <= 114 && deathmatchCountdown >= 110) {

                if (deathmatchCountdown == 114){ // I hate doing it like this but I could not think of any other way
                    Bukkit.broadcastMessage("§ePlayers will become vulnerable in §c5 §eseconds");
                } else if (deathmatchCountdown == 113){
                    Bukkit.broadcastMessage("§ePlayers will become vulnerable in §c4 §eseconds");
                } else if (deathmatchCountdown == 112){
                    Bukkit.broadcastMessage("§ePlayers will become vulnerable in §c3 §eseconds");
                } else if (deathmatchCountdown == 111){
                    Bukkit.broadcastMessage("§ePlayers will become vulnerable in §c2 §eseconds");
                } else if (deathmatchCountdown == 110){
                    Bukkit.broadcastMessage("§ePlayers will become vulnerable in §c1 §eseconds");
                }

            }
            if (deathmatchCountdown == 108){
                for (Player players: Bukkit.getOnlinePlayers()){
                    players.playSound(players.getLocation(), Sound.ENTITY_ZOMBIE_HORSE_DEATH, 1, 1);
                }
                Bukkit.broadcastMessage("§a[!] Players are now vulnerable");
                game.setVunrability(true);
            }
            deathmatchCountdown--;
            scorebord.refreshScorebordAll();
        }


    }

    public String getTimeLeft() {
        if (game.getState() == GameState.gameState.SECONDHALF) {
            if (secondHalfTimerCountdown == 180) {
                return "Not started";
            } else if (secondHalfTimerCountdown < 1) {
                return "Deathmatch time";
            }
            return String.valueOf(secondHalfTimerCountdown);
        } else if (game.getState() == GameState.gameState.ACTIVE){
                if (refillTimer == 300) {
                    return "Not started";
                } else if (refillTimer < 1) {
                    return "Refill over";
                }
                return String.valueOf(refillTimer);
        } else { // Deathmatch time
            return String.valueOf(deathmatchCountdown);
        }
    }

    public void stopGame(Boolean stoppedByCommand){
        game.setState(GameState.gameState.ENDING, "Game timer stopped game");
        if (stoppedByCommand){
            for (Player players: Bukkit.getOnlinePlayers()){
                players.sendTitle(ChatColor.RED + "Game stopped", ChatColor.YELLOW + "Please await further instructions", 10, 30, 10);
                players.playSound(players.getLocation(), Sound.ENTITY_BLAZE_DEATH, 1, 1);
                Bukkit.getLogger().log(Level.WARNING, "Game stopped by an admin");
                scorebord.refreshScorebordAll();
                return;
            }
        } else if (game.getPlayerNumbers() == 1){
            playerName = game.getLastPlayer().getName();
            for (Player players: Bukkit.getOnlinePlayers()){
                players.sendTitle(ChatColor.RED + playerName + " WINS", ChatColor.YELLOW + "You will return to the lobby in 20 seconds", 10, 60, 10);
            }
            new EndingTimer(game, main, false).runTaskTimer(main, 0, 20);
            scorebord.refreshScorebordAll();
        } else {
            new EndingTimer(game, main, true).runTaskTimer(main, 0, 20);

            for (Player players : Bukkit.getOnlinePlayers()) {
                players.sendTitle(ChatColor.RED + "Its a DRAW", ChatColor.YELLOW + "You will return to the lobby in 20 seconds", 10, 60, 10);
                players.sendMessage(ChatColor.RED + "Both players will receive 5 points for drawing");
                scorebord.refreshScorebordAll();
            }
        }
    }

    public void startDeathmatch(){
        game.setState(GameState.gameState.DEATHMATCH, "GameTimer Only 2 players left"); // Stops all the timers and starts the deathmatch timer
    }
}
