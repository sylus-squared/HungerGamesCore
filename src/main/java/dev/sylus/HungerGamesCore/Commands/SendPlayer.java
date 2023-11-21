package dev.sylus.HungerGamesCore.Commands;

import dev.sylus.HungerGamesCore.Files.Databases;
import dev.sylus.HungerGamesCore.Game.Scorebord;
import dev.sylus.HungerGamesCore.Utils.ServerUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.logging.Level;

public class SendPlayer implements CommandExecutor {
    ServerUtil serverUtil;
    public SendPlayer(ServerUtil serverUtilInstance){
        serverUtil = serverUtilInstance;
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (args.length < 1){
            sender.sendMessage(ChatColor.RED + "Incorrect usage. The correct useage is /sendplayer <PLAYERNAME or ALL> <SERVER NAME>");
        }
        if (args[1].isEmpty()){
            sender.sendMessage(ChatColor.RED + "Please include a server name");
        }

        if (args[0].equals("ALL")){
            for (Player players: Bukkit.getOnlinePlayers()){
               serverUtil.sendPlayerToServer(players, args[1]);
            }
            return true;
        }
        String playerName = args[0];
        Player targetPlayer = Bukkit.getPlayer(playerName);

        if (targetPlayer == null){
            sender.sendMessage(ChatColor.RED + "Player not found or is not online");
            return true;
        }
        serverUtil.sendPlayerToServer(targetPlayer, args[1]);
        return true;
    }
}
