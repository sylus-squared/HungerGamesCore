package dev.sylus.HungerGamesCore.Commands;

import dev.sylus.HungerGamesCore.Game.Border;
import dev.sylus.HungerGamesCore.HungerGamesCore;
import dev.sylus.HungerGamesCore.Tasks.BorderShrinkTask;
import dev.sylus.HungerGamesCore.Tasks.GameRunTask;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class BorderTest implements CommandExecutor {

    Border border;
    HungerGamesCore main;

    public BorderTest(Border borderInstance, HungerGamesCore mainInstance){
        border = borderInstance;
        main = mainInstance;
    }


    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        new BorderShrinkTask(border).runTaskTimer(main, 0, 10);
        commandSender.sendMessage(ChatColor.RED + "Executed successfully");
        return true;
    }
}
