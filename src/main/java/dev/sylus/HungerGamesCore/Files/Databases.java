package dev.sylus.HungerGamesCore.Files;

import dev.sylus.HungerGamesCore.Game.Game;
import dev.sylus.HungerGamesCore.HungerGamesCore;
import org.bukkit.entity.Player;

public class Databases {
    // This file will hold all the methods to get data from the players table in the SQLite (Or MySQL) database

    Game game;
    HungerGamesCore main;

    public Databases(Game gameInstance, HungerGamesCore hungerGamesCoreInstance){ // Constructor
        game = gameInstance;
    }

    public int getTotalPoints(Player player){
        return 20; // Gets the players points from the DB and returns it
    }

    public int getGamePoints(Player player){
        return 20;
    }

    public int getKills(Player player){
        return 20; // Returns the players kills from the DB
    }

    public void updatePoints(Player player, int newPoints){
        // This will replace the old points from the database with the new ones
    }

    public void updateKills(Player player, int newKills){
       // This will replace the old kills from the database with the new ones
    }
}
