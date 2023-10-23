package dev.sylus.HungerGamesCore.Files;

import dev.sylus.HungerGamesCore.Game.Game;
import dev.sylus.HungerGamesCore.HungerGamesCore;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.mariadb.jdbc.Connection;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;

public class Databases {
    // This file will hold all the methods to get data from the players table in the SQLite (Or MySQL) database

    Game game;
    HungerGamesCore main;
    Files files;
    Connection connection;
    String driver;
    String username;
    String password;

    public Databases(Game gameInstance, HungerGamesCore hungerGamesCoreInstance, Files filesInstance){ // Constructor
        game = gameInstance;
        main = hungerGamesCoreInstance;
        files = filesInstance;
        username = files.getConfig("config.yml").getString("database.username");
        password = files.getConfig("config.yml").getString("database.password");
    }

    public void initiliseDatabase(){
        try {
            driver = "org.mariadb.jdbc.Driver";
            Class.forName(this.driver);

            connection = (Connection) DriverManager.getConnection(
                    "jdbc:mariadb://localhost:3306/database_name",
                    username, password
            );
        } catch (SQLException error) {
            Bukkit.getLogger().log(Level.SEVERE, String.valueOf(error));
        } catch (ClassNotFoundException error) {
            Bukkit.getLogger().log(Level.SEVERE, String.valueOf(error));
        }
    }

    public void cloaseConnection(){
        try {
            connection.close();
        } catch (SQLException error) {
            Bukkit.getLogger().log(Level.SEVERE, String.valueOf(error));
        }
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
