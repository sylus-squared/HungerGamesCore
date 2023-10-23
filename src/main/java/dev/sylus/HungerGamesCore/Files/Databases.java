package dev.sylus.HungerGamesCore.Files;

import dev.sylus.HungerGamesCore.Game.Game;
import dev.sylus.HungerGamesCore.HungerGamesCore;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import java.sql.Connection;
import java.sql.DriverManager;

import java.sql.*;
import java.util.logging.Level;

public class Databases {
    // This file will hold all the methods to get data from the players table in the SQLite (Or MySQL) database

    Game game;
    HungerGamesCore main;
    Files files;

    final String username;
    final String password;
    final String url;
    static Connection connection;

    public Databases(Game gameInstance, HungerGamesCore hungerGamesCoreInstance, Files filesInstance){ // Constructor
        game = gameInstance;
        files = filesInstance;

        username = files.getUsername();
        password = files.getPassword();
        url = files.getPassword();
    }

    public void initiliseDatabase(String databaseName) {
        try {
            connection = DriverManager.getConnection(
                    "jdbc:mariadb://localhost:3306/" + databaseName,
                    username, password
            );
            Bukkit.getLogger().log(Level.INFO, "Started a connection to the server");
        } catch (SQLException exception){
            Bukkit.getLogger().log(Level.SEVERE, String.valueOf(exception));
        }

    }

    public void closeConnection(){
        try {
            connection.close();
            Bukkit.getLogger().log(Level.INFO, "Closed connection to the DB");
        } catch (SQLException exception) {
            Bukkit.getLogger().log(Level.SEVERE, String.valueOf(exception));
        }
    }

    public String testSelect(){
        try (PreparedStatement statement = connection.prepareStatement("""
            SELECT *
            FROM books
        """)) {
            ResultSet resultSet = statement.executeQuery();
            return String.valueOf(resultSet);
           /* while (resultSet.next()) {
                String val1 = resultSet.getString(1); // by column index
                int val2 = resultSet.getInt("column2"); // by column name
                // ... use val1 and val2 ...
            }

            */
        } catch (SQLException error) {
            Bukkit.getLogger().log(Level.SEVERE, String.valueOf(error));
            return "Error";
        }
    }

    public void getData(){

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
