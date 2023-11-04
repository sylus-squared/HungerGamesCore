package dev.sylus.HungerGamesCore.Commands;

import dev.sylus.HungerGamesCore.Files.Databases;
import dev.sylus.HungerGamesCore.Game.Scorebord;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AddPoints implements CommandExecutor {
    Databases databases;
    Scorebord scorebord;

    public AddPoints(Databases databasesInstance, Scorebord scorebordInstance){
        databases = databasesInstance;
        scorebord = scorebordInstance;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (args.length < 1){
            sender.sendMessage(ChatColor.RED + "The correct format is <PLAYERNAME> <POINTS TO ADD>");
            return true;
        }
        String playerName = args[0];
        Player targetPlayer = Bukkit.getPlayer(playerName);

        if (targetPlayer == null){
            sender.sendMessage(ChatColor.RED + "Player not found or is not online");
            scorebord.refreshScorebordAll();
            return true;
        }
        if (!(databases.isInDatabase(targetPlayer.getUniqueId()))){
            sender.sendMessage(ChatColor.RED + "Player is not in the database");
            scorebord.refreshScorebordAll();
            return true;
        }

        databases.addPoints(targetPlayer.getUniqueId(), Integer.parseInt(args[1]));
        sender.sendMessage(ChatColor.RED + "Updated the data");
        return true;
    }
}
