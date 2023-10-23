package dev.sylus.HungerGamesCore.Game;

import dev.sylus.HungerGamesCore.Files.Files;
import org.bukkit.Bukkit;
import org.bukkit.WorldBorder;

public class Border {
    WorldBorder border;
    Files files;
    double currentBorderSize;

    public Border(Files filesInstance){
        files = filesInstance;
        currentBorderSize = filesInstance.getBorderStartSize();
    }

    public void setWorldBorder(WorldBorder input){
        border = input;
    }

    public void initialiseBorder(){
        setWorldBorder(Bukkit.getWorld("World").getWorldBorder());
        getBorder().setCenter(Bukkit.getWorld("world").getSpawnLocation());
        getBorder().setWarningDistance(10);
        getBorder().setWarningTime(5);
        getBorder().setDamageAmount(2.0);
        getBorder().setSize(files.getBorderStartSize());
    }

    public WorldBorder getBorder(){
        return border;
    }

    public void setNewSize(double size){
        getBorder().setSize(size);
        currentBorderSize = size;
    }

    public double getCurrentSize(){
        return currentBorderSize;
    }
}
