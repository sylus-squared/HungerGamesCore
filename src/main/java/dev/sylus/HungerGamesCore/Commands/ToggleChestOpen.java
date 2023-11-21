package dev.sylus.HungerGamesCore.Commands;

import dev.sylus.HungerGamesCore.Game.ChestManager;
import dev.sylus.HungerGamesCore.Game.Game;
import dev.sylus.HungerGamesCore.HungerGamesCore;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class ToggleChestOpen implements CommandExecutor {

    HungerGamesCore main;

    public ToggleChestOpen(HungerGamesCore mainInstance){ // Constructor
        main = mainInstance;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        main.setCanOpenChests(!(main.getCanOpenChests()));
        sender.sendMessage(ChatColor.RED + "Set players being able to open chests to: " + String.valueOf(main.getCanOpenChests()));
        return true;
    }
}
