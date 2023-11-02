package dev.sylus.HungerGamesCore.Game;

import dev.sylus.HungerGamesCore.Enums.GameState;
import dev.sylus.HungerGamesCore.HungerGamesCore;
import dev.sylus.HungerGamesCore.Tasks.GameCountDownTask;
import org.bukkit.*;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;

public class Game {
    private List<Player> playersAlive;
    private List<Player> spectators;
    private List<String> playerData; // The format is ["playerUUID", "gamePoints", "kills"]
    private GameState.gameState gameState = GameState.gameState.TESTING;
    private boolean movement = true; // This controls if people can move or not
    private boolean playerCount = true; // This controls if the player count should go up when someone joins or not
    boolean doDamage = true; // If this is false then players cannot take damage

    HungerGamesCore main;
    GameCountDownTask countDownTask;
    ChestManager chestManager;

    public Game(HungerGamesCore mainInstance, ChestManager chestManagerInstance){ // Constructor
        main = mainInstance;
        countDownTask = main.getGameCountDownTask();
        playersAlive = new ArrayList<Player>();
        spectators = new ArrayList<Player>();
        playerData = new ArrayList<String>();
        chestManager = chestManagerInstance;
    }

    public void startGame(){
        if (!(gameState == GameState.gameState.PREGAME || gameState == GameState.gameState.TESTING)){
            main.logger.log(Level.WARNING, "Game start called outside of pregame");
            main.logger.log(Level.INFO, "Game state is: " + gameState + " Game state from method is: " + this.getState());
            return;
        }
        new GameCountDownTask(this, main, chestManager).runTaskTimer(main, 0, 20);

        this.gameState = GameState.gameState.ACTIVE;
        this.movement = true;
    }
    public void endGame(){ // This is no longer used
        if (this.gameState == GameState.gameState.PREGAME){
            main.logger.log(Level.WARNING, "Game end called during the pregame");
            return;
        }

        if (this.playersAlive.size() < 2){

        }

        for (Player players: playersAlive){
             players.sendTitle(ChatColor.RED + "Game over", "", 1, 5, 3);
        }
    }

    public List getPlayers(){
        return this.playersAlive;
    }
    public int getPlayerNumbers(){
        return this.playersAlive.size();
    }

    public List getSpectators(){
        return this.spectators;
    }

    public boolean getMovement(){
        return this.movement;
    }

    public void setMovement(boolean state){
        this.movement = state;
    }

    public boolean getCount(){
        return this.playerCount;
    }

    public void removePlayer(Player deadPlayer){
        this.playersAlive.remove(deadPlayer);
        this.spectators.add(deadPlayer);
    }

    public void setState(GameState.gameState state, String who){
        Bukkit.getLogger().log(Level.INFO, who + " Changed the game state to: " + state);
        this.gameState = state;
    }

   /* public void broadcastMessage(String message){
        Bukkit.broadcastMessage(message);
    }
    There is no use for this now */


    public GameState.gameState getState(){
        return gameState;
    }

    public Game gaetGame(){
        return this;
    }

    public void addPlayer(Player player){
        if (playerCount) {
            playersAlive.add(player);
        } else {
            Bukkit.getLogger().log(Level.WARNING, "Player tried to be added while player count is false");
        }
    }

    public void setCounting(boolean bool){
        playerCount = bool;
    }

    public boolean getCounting(){
        return playerCount;
    }

    public Player getLastPlayer(){
        return playersAlive.get(0);
    }

    public boolean getDamageState(){
        return doDamage;
    }

    public void setVunrability(Boolean vulnerability){
        this.doDamage = vulnerability;
    }

    public boolean isPlayerAlive(Player player){
        return playersAlive.contains(player);
    }
    public void addPlayerToLocalDataData(UUID uuid){
        playerData.add(uuid.toString()); // UUID
        playerData.add(String.valueOf(0)); // Kills
        playerData.add(String.valueOf(0)); // Game points
    }

    public ArrayList<String> getLocalPlayerData(UUID uuid){
       ArrayList<String> dataToReturn = new ArrayList<String>();

        for (int i = 0; i < playerData.size(); i++) {
            if (playerData.get(i).equals(uuid.toString())){
                dataToReturn.add(playerData.get(i)); // UUID
                dataToReturn.add(playerData.get(i + 1)); // Kills
                dataToReturn.add(playerData.get(i + 2)); // Game points
            }
        }
       return dataToReturn;
    }

    public void addPoints(UUID uuid, int pointsToAdd){
        if (!(playerData.contains(uuid))){
            Bukkit.getLogger().log(Level.SEVERE, "Tried to add data to a player that is not in the local data list");
        }
        String data;
        for (int i = 0; i < playerData.size(); i++) {
            if (playerData.get(i).equals(uuid.toString())){
                playerData.set(i + 2, String.valueOf( Integer.parseInt(playerData.get(i + 2) + pointsToAdd)));
                return;
            }
        }
    }

    public void addKills(UUID uuid, int killsToAdd){
        if (!(playerData.contains(uuid))){
            Bukkit.getLogger().log(Level.WARNING, String.valueOf(playerData) + " From the if statement");
            Bukkit.getLogger().log(Level.SEVERE, "Tried to add data to a player that is not in the local data list");
        }
        String data;
        for (int i = 0; i < playerData.size(); i++) {
            if (playerData.get(i).equals(uuid.toString())){
                playerData.set(i + 2, String.valueOf( Integer.parseInt(playerData.get(i ++) + killsToAdd)));
                return;
            }
        }
    }

    public boolean isInLocalData(UUID uuid){
        return playerData.contains(uuid.toString());
    }
}
