package dev.sylus.HungerGamesCore.Commands;

import dev.sylus.HungerGamesCore.Files.Databases;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AddPoints implements CommandExecutor {
    Databases databases;

    public AddPoints(Databases databasesInstance){
        databases = databasesInstance;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] strings) {
        Player player = (Player) sender;
        databases.updateData(player.getUniqueId(), 5);
        sender.sendMessage(ChatColor.RED + "Updated the data");
        return true;
    }
}
