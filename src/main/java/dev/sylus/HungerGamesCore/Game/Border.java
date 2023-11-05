package dev.sylus.HungerGamesCore.Game;

import dev.sylus.HungerGamesCore.Files.Files;
import org.bukkit.Bukkit;
import org.bukkit.WorldBorder;

public class Border {
    private WorldBorder border;
    int curretBorderSize = 0;

    Files files;
    public Border(Files fileInstance){
        files = fileInstance;
    }

    public void setWorldBorder(WorldBorder input){
        border = input;
    }

    public WorldBorder getWorldBorder(){
        return border;
    }

    public void initialiseBorder(){
        setWorldBorder(Bukkit.getWorld("World").getWorldBorder());
        getWorldBorder().setCenter(Bukkit.getWorld("World").getSpawnLocation());
        getWorldBorder().setWarningDistance(10);
        getWorldBorder().setSize(files.getConfig("worldData").getInt("worldData.startingBorderSize"));
        curretBorderSize = files.getConfig("worldData").getInt("worldData.startingBorderSize");
        getWorldBorder().setWarningTime(5);
        getWorldBorder().setDamageAmount(5.0);
    }

    public void setBorderSize(int newSize){
        getWorldBorder().setSize(newSize);
        curretBorderSize = newSize;
    }

    public int getBorderSize(){
        return curretBorderSize;
    }


}
