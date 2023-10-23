package dev.sylus.HungerGamesCore.Commands;

import dev.sylus.HungerGamesCore.Enums.GameState;
import dev.sylus.HungerGamesCore.Game.Game;
import dev.sylus.HungerGamesCore.Game.Scorebord;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.logging.Level;

public class SetState implements CommandExecutor {
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
}
