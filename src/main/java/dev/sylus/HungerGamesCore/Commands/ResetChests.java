package dev.sylus.HungerGamesCore.Commands;

import dev.sylus.HungerGamesCore.Game.ChestManager;
import dev.sylus.HungerGamesCore.Game.Game;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.logging.Level;

public class ResetChests implements CommandExecutor {
    ChestManager chestManager;
    public ResetChests(ChestManager chestManagerInstance){
        chestManager = chestManagerInstance;
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (!(sender instanceof Player)) {
            Bukkit.getLogger().log(Level.WARNING, "Non player tried to execute a player only command");
            return true;
        }
        chestManager.resetChests();
    return true;
    }

}
