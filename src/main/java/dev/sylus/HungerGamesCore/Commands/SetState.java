package dev.sylus.HungerGamesCore.Commands;

import dev.sylus.HungerGamesCore.Enums.ChestRarity;
import dev.sylus.HungerGamesCore.Enums.GameState;
import dev.sylus.HungerGamesCore.Game.Game;
import dev.sylus.HungerGamesCore.Game.Scorebord;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

public class SetState implements TabCompleter, CommandExecutor {
    Game game;
    Scorebord scorebord;
    public SetState(Game gameInstance, Scorebord scorebordInstance){
        game = gameInstance;
        scorebord = scorebordInstance;
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (!(sender instanceof Player)) {
            Bukkit.getLogger().log(Level.WARNING, "Non player tried to execute a player only command");
            return true;
        } else if (!(sender.hasPermission("hungergamescore.setstate"))) {
            sender.sendMessage(ChatColor.RED + "You do not have the permission to use this command");
        }

        if (args == null){
            sender.sendMessage(ChatColor.RED + "Please include an argument");
        }
        try {
            game.setState(GameState.gameState.valueOf(args[0]), "Change state command");
            sender.sendMessage(ChatColor.RED + "State set to: " + ChatColor.YELLOW + args[0]);
            scorebord.refreshScorebordAll();
        } catch (IllegalArgumentException exception){
            sender.sendMessage(ChatColor.RED + exception.toString());
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String s, String[] args) {
        List<String> suggestions = new ArrayList<>();
        // Add argument suggestions here
        if (args.length == 1) {
            suggestions.add(String.valueOf(GameState.gameState.TESTING));
            suggestions.add(String.valueOf(GameState.gameState.PREGAME));
            suggestions.add(String.valueOf(GameState.gameState.GAMESTART));
            suggestions.add(String.valueOf(GameState.gameState.ACTIVE));
            suggestions.add(String.valueOf(GameState.gameState.SECONDHALF));
            suggestions.add(String.valueOf(GameState.gameState.DEATHMATCH));
            suggestions.add(String.valueOf(GameState.gameState.ENDING));
        }
        return suggestions;
    }
}
