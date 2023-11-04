package dev.sylus.HungerGamesCore.Commands;

import dev.sylus.HungerGamesCore.Files.Databases;
import dev.sylus.HungerGamesCore.HungerGamesCore;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GetAllData implements CommandExecutor {
    Databases databases;
    HungerGamesCore hungerGamesCore;

    public GetAllData(Databases databasesInstance, HungerGamesCore hungerGamesCoreInstance){
        databases = databasesInstance;
        hungerGamesCore = hungerGamesCoreInstance;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] strings) {
        Player player = (Player) sender;
        player.sendMessage(ChatColor.RED + String.valueOf(databases.getPlayerData(player.getUniqueId())));
        player.sendMessage(ChatColor.RED + String.valueOf(databases.isInDatabase(player.getUniqueId())));
        return true;
    }
}
