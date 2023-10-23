package dev.sylus.HungerGamesCore.Files;

import dev.sylus.HungerGamesCore.HungerGamesCore;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.logging.Level;

public class Files {

    /*
    The structure of the YAML file is as follows:
    worldData:
        spawnLocationX: <location>
        spawnLocationY: <location>
        spawnLocationZ: <location>
        serverCode: GAMESERVER_MAIN<numberThatCountsUp>

     */

    private HungerGamesCore main;
    private FileConfiguration dataConfig = null;
    private File configFile = null;

    public Files(HungerGamesCore mainInstance, String fileName){ // Constructor
        main = mainInstance;
        saveDefultConfig(fileName); // The file name is the name of the YAML file that will be worked with
                                    // I am doing it like this, so I can re-use this code in future projects
                                    // without having to change a thing
    }

    public void reloadConfig(String fileName){
        if (configFile == null){
            configFile = new File(main.getDataFolder(), fileName);
        }
        dataConfig = YamlConfiguration.loadConfiguration(configFile);
        InputStream defaultStream = main.getResource(fileName);
        if (defaultStream != null){
            YamlConfiguration defaultConfig = YamlConfiguration.loadConfiguration(new InputStreamReader(defaultStream));
            dataConfig.setDefaults(defaultConfig);
        } else {
            Bukkit.getLogger().log(Level.SEVERE, "File: " + fileName + " is NULL");
        }
    }

    public FileConfiguration getConfig(String fileName){
        if (dataConfig == null){
            reloadConfig(fileName);
        }
        return dataConfig;
    }

    public void saveConfig(String fileName){
        if (dataConfig == null || configFile == null){
            return;
        }
        try {
            getConfig(fileName).save(configFile);
        } catch (IOException e) {
            Bukkit.getLogger().log(Level.SEVERE, "Could not save config for: " + fileName + " Stack trace: " + e);
        }
    }

    public void saveDefultConfig(String fileName){
        if (configFile == null){
            configFile = new File(main.getDataFolder(), fileName);
        }
        if (!(configFile.exists())){
            main.saveResource(fileName, false);
        }
    }

    public int getPlayerCap(){
        return 20; // Later this will read a YAML file and return the max players for that map
                   // I don't have the file handling yet, so I am just returning 20 for now
    }

    public int getMapRespawnX(){ // Again this will at some point be gotten from a YAML file
       return this.getConfig("worldData").getInt("worldData.spawnLocationX");
    }
    public int getMapRespawnY(){
        return this.getConfig("worldData").getInt("worldData.spawnLocationY");
    }
    public int getMapRespawnZ(){
        return this.getConfig("worldData").getInt("worldData.spawnLocationX");
    }

    public int getDeathmatchSpawnX(){
        return 0;
    }
    public int getDeathmatchSpawnY(){
        return 38;
    }
    public int getDeathmatchSpawnZ(){
        return -3;
    }

    public int getBorderStartSize(){
        return 200;
    }

    public String getUsername(){
        return this.getConfig("config").getString("database.username");
    }

    public String getPassword(){
        return this.getConfig("config").getString("database.password");
    }

    public String getUrl(){
        return this.getConfig("config").getString("database.url");
    }

}
