package dev.sylus.HungerGamesCore.Commands;

import dev.sylus.HungerGamesCore.Game.Game;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.logging.Level;

public class GameStart implements CommandExecutor {

    Game game;
    public GameStart(Game gameInstance){
        game = gameInstance;
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (!(sender instanceof Player)) {
            Bukkit.getLogger().log(Level.WARNING, "Non player tried to execute a player only command");
            return true;
        }
        game.startGame();
        return true;
    }

}
