package dev.sylus.HungerGamesCore.Commands;

import dev.sylus.HungerGamesCore.Game.Game;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class TogglePlayerCount implements CommandExecutor {

    Game game;

    public TogglePlayerCount(Game gameInstance){ // Constructor
        game = gameInstance;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        game.setCounting(!(game.getCount()));
        sender.sendMessage(ChatColor.RED + "Set the value of player counting to: " + ChatColor.YELLOW + game.getCount());
        return true;
    }
}
