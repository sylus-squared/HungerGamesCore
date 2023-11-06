package dev.sylus.HungerGamesCore.Commands;

import dev.sylus.HungerGamesCore.HungerGamesCore;
import dev.sylus.HungerGamesCore.Tasks.GameTimer;
import dev.sylus.HungerGamesCore.Utils.KnockbackCheck;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class KnockbackCheckCommand implements CommandExecutor {
    HungerGamesCore main;


    public KnockbackCheckCommand(HungerGamesCore hungerGamesCore) { // Constructor
        main = hungerGamesCore;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (args.length == 0) {
            sender.sendMessage(ChatColor.RED + "You must include a player");
            return true;
        }

        Player target = Bukkit.getPlayer(args[0]);
        if (target == null){
            sender.sendMessage(ChatColor.RED + "Player not found or is not online");
            return true;
        }
        Player commandSender = (Player) sender;

        new KnockbackCheck(target, commandSender).runTaskTimer(main, 0, 20);
        return true;
    }
}

