package dev.sylus.HungerGamesCore.Commands;

import dev.sylus.HungerGamesCore.Files.Databases;
import dev.sylus.HungerGamesCore.Game.Scorebord;
import dev.sylus.HungerGamesCore.Utils.ServerUtil;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class JoinServer implements CommandExecutor {
    ServerUtil serverUtil;


    public JoinServer(ServerUtil serverUtilInstance){ // Constructor
        serverUtil = serverUtilInstance;

    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (!(sender instanceof Player)){
            sender.sendMessage(ChatColor.RED + "Only players can use this command");
        }
        if (args.length < 1){
            sender.sendMessage(ChatColor.RED + "Incorrect format, the correct format is /join <SERVERNAME>");
            return true;
        }
        Player player = (Player) sender;
        serverUtil.sendPlayerToServer(player, args[0]);
        return true;
    }
}

