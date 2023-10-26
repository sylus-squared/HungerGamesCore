package dev.sylus.HungerGamesCore.Commands;

import dev.sylus.HungerGamesCore.Files.Databases;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class DatabaseTest implements CommandExecutor {

    Databases databases;

    public DatabaseTest(Databases databasesInstance){
        databases = databasesInstance;

    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        Player player = (Player) commandSender;
        commandSender.sendMessage(ChatColor.RED + String.valueOf(databases.getPlayerData(player.getUniqueId())));
        return true;
    }
}
