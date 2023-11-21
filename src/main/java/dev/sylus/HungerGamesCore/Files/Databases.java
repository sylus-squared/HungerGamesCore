package dev.sylus.HungerGamesCore.Files;

import dev.sylus.HungerGamesCore.Game.Game;
import dev.sylus.HungerGamesCore.HungerGamesCore;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.mariadb.jdbc.Connection;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;

public class Databases {
    // This file will hold all the methods to get data from the players table in the SQLite (Or MySQL) database

    HungerGamesCore main;
    Files files;
    Connection connection;
    String driver;
    String username;
    String password;

    Map<UUID, PlayerData> localDataMap = new ConcurrentHashMap<>();

    UUID playerUUID;
    PlayerData playerData;

    public Databases(HungerGamesCore hungerGamesCoreInstance, Files filesInstance){ // Constructor
        main = hungerGamesCoreInstance;
        files = filesInstance;
        username = files.getConfig("config.yml").getString("database.username");
        password = files.getConfig("config.yml").getString("database.password");
    }

    /*
    The MariaDB database structure is as follows:
    playerData (Database)
        dataTable (table duh)
            UUID: varchar(255)
            Name: varchar(255)
            Kills: int(255)  // Not used
            Points: int(255) // Points that save across all games
            Score: int(255) // Not used
     */

    public void addPlayerToLocalData(UUID newUUID){
        this.playerUUID = newUUID;
        playerData = new PlayerData(playerUUID,  Integer.parseInt(getPlayerData(playerUUID).get(2)), 0, 0);
        localDataMap.put(playerUUID, playerData);
    }

    public boolean isPlayerInLocalData(UUID uuid){
        return localDataMap.containsKey(uuid);
    }

    public PlayerData getLocalPlayerData(UUID uuid){
        PlayerData retrivedData = localDataMap.get(uuid);
        if (retrivedData == null){
            Bukkit.getLogger().log(Level.SEVERE, "Retrived data is null");
        }
        return retrivedData;
    }

    public void addPoints(UUID uuid, int pointsToAdd){
        PlayerData oldPoints = getLocalPlayerData(uuid);
        int Points = oldPoints.getGamePoints();

        PlayerData dataPlayer = getLocalPlayerData(uuid);
        dataPlayer.setGamePoints(pointsToAdd + Points);
        dataPlayer.setOverallPoints(pointsToAdd + pointsToAdd);
    }

    public void addKills(UUID uuid, int killsToAdd){
        int oldKills = getLocalPlayerData(uuid).getCurrentKills();
        PlayerData dataPlayer = getLocalPlayerData(uuid);
        dataPlayer.setCurrentKills(killsToAdd + oldKills);
    }

    public void addPointsToDB (UUID uuid){
        updateData(uuid, getLocalPlayerData(uuid).getGamePoints());
    }

    public void resetPoints(UUID uuid){
      PlayerData playersData = getLocalPlayerData(uuid);
      playersData.setOverallPoints(0);
      playersData.setGamePoints(0);
      addPointsToDB(uuid);
    }

    public void initialiseDatabase(){
        try {
            driver = "org.mariadb.jdbc.Driver";
            Class.forName(this.driver);

            connection = (Connection) DriverManager.getConnection(
                    "jdbc:mariadb://localhost:3306/playerData",
                    username, password
            );
        } catch (SQLException error) {
            Bukkit.getLogger().log(Level.SEVERE, String.valueOf(error));
            Logging.log(Level.SEVERE, String.valueOf(error));
        } catch (ClassNotFoundException error) {
            Bukkit.getLogger().log(Level.SEVERE, String.valueOf(error));
            Logging.log(Level.SEVERE, String.valueOf(error));
        }
    }

    public void closeConnection(){
        try {
            Bukkit.getLogger().log(Level.WARNING, "Closed the database connection");
            Logging.log(Level.INFO, "Closing the database connection");
            connection.close();
        } catch (SQLException error) {
            Bukkit.getLogger().log(Level.SEVERE, String.valueOf(error));
            Logging.log(Level.SEVERE, String.valueOf(error));
        }
    }

    /*
    The MariaDB database structure is as follows:
    playerData (Database)
        dataTable (table duh)
            UUID: varchar(255)
            Name: varchar(255)
            Kills: int(255) *not used*
            Points: int(255)
            Score: int(255) *not used*
     */

    public boolean isInDatabase(UUID uuid) {
        if (connection == null){
            initialiseDatabase();
        }

        try {
            String sql = "SELECT UUID FROM dataTable WHERE UUID = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, uuid.toString());
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()){
                resultSet.close();
                preparedStatement.close();
                return true;
            }
            resultSet.close();
            preparedStatement.close();
            return false;
        } catch (SQLException error) {
            Bukkit.getLogger().log(Level.SEVERE, String.valueOf(error));
            Logging.log(Level.SEVERE, String.valueOf(error));
        }
        return false;
    }

    public ArrayList<String> getPlayerData(UUID uuid){
        String name = "error";
        String kills = "error";
        String points = "error";
        String score = "error";
        ArrayList<String> dataToReturn =  new ArrayList<String>();

        if (connection == null){
            initialiseDatabase();
        }

        try {
            String statement = "SELECT * FROM dataTable WHERE UUID = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(statement);
            preparedStatement.setString(1, uuid.toString());
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                name = resultSet.getString("Name");
                kills = resultSet.getString("Kills");
                points = String.valueOf(resultSet.getInt("Points"));
                score = String.valueOf(resultSet.getInt("Score"));
            }

            dataToReturn.add(name);
            dataToReturn.add(kills);
            dataToReturn.add(points);
            dataToReturn.add(score);

            resultSet.close();
            preparedStatement.close();
        } catch (SQLException error){
            Bukkit.getLogger().log(Level.SEVERE, String.valueOf(error));
            Logging.log(Level.SEVERE, String.valueOf(error));
            return null;
        }
        return dataToReturn;
    }

    public void addNewPlayer(UUID UUID, String name){ // Remember to turn the UUID into a string
        if (connection == null){
            initialiseDatabase();
        }
        try {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO dataTable(UUID, Name, Kills, Points, Score) VALUES (?, ?, ?, ?, ?)");
            statement.setString(1, UUID.toString());
            statement.setString(2, name);
            statement.setInt(3, 0);
            statement.setInt(4, 0);
            statement.setInt(5, 0);
            statement.executeUpdate();
            statement.close();
        } catch (SQLException error) {
            Bukkit.getLogger().log(Level.SEVERE, String.valueOf(error));
            Logging.log(Level.SEVERE, String.valueOf(error));
        }
    }

    public void updateData(UUID uuid, int points){
        if (connection == null){
            initialiseDatabase();
        }
        try {
            String statement = "UPDATE dataTable SET Points = ? WHERE UUID = ?;";
            PreparedStatement preparedStatement = connection.prepareStatement(statement);
            preparedStatement.setString(1, String.valueOf(points));
            preparedStatement.setString(2, uuid.toString());
            ResultSet resultSet = preparedStatement.executeQuery();

            resultSet.close();
            preparedStatement.close();
        } catch (SQLException error) {
            Bukkit.getLogger().log(Level.SEVERE, String.valueOf(error));
            Logging.log(Level.SEVERE, String.valueOf(error));
        }
    }

    @Deprecated
    public void addPointstoDB(UUID uuid, int pointsToAdd){
        if (connection == null){
            initialiseDatabase();
        }
        try {
            ArrayList<String> currentPoints = getPlayerData(uuid);
            Bukkit.getLogger().log(Level.WARNING, String.valueOf(currentPoints));
            pointsToAdd = pointsToAdd + Integer.parseInt(currentPoints.get(2));
            String statement = "UPDATE dataTable SET Points = ? WHERE UUID = ?;";
            PreparedStatement preparedStatement = connection.prepareStatement(statement);
            preparedStatement.setString(1, String.valueOf(pointsToAdd));
            preparedStatement.setString(2, uuid.toString());
            ResultSet resultSet = preparedStatement.executeQuery();

            resultSet.close();
            preparedStatement.close();
        } catch (SQLException error) {
            Bukkit.getLogger().log(Level.SEVERE, String.valueOf(error));
            Logging.log(Level.SEVERE, String.valueOf(error));
        }
    }

    @Deprecated
    public void resetPointsDatabase(UUID uuid){ // Not used anymore
        if (connection == null){
            initialiseDatabase();
        }
        try {
            String statement = "UPDATE dataTable SET Points = 0 WHERE UUID = ?;";
            PreparedStatement preparedStatement = connection.prepareStatement(statement);
            preparedStatement.setString(1, String.valueOf(uuid));
            ResultSet resultSet = preparedStatement.executeQuery();

            resultSet.close();
            preparedStatement.close();
        } catch (SQLException error) {
            Bukkit.getLogger().log(Level.SEVERE, String.valueOf(error));
            Logging.log(Level.SEVERE, String.valueOf(error));
        }
    }


    public String getTotalPoints(Player player){
        ArrayList<String> data = getPlayerData(player.getUniqueId()); // Name, Kills, Points, Score What's the difference between points and score?
        return data.get(2);
    }

}
