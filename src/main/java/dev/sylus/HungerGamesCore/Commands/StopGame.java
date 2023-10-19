package dev.sylus.HungerGamesCore.Commands;

import dev.sylus.HungerGamesCore.Enums.GameState;
import dev.sylus.HungerGamesCore.Game.Game;
import dev.sylus.HungerGamesCore.Tasks.GameTimer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.logging.Level;

public class StopGame implements CommandExecutor {

    GameTimer gameTimer;
    Game game;
    public StopGame(GameTimer gameTimerInstance, Game gameInstance){
        gameTimer = gameTimerInstance;
        game = gameInstance;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (game.getState() == GameState.gameState.PREGAME || game.getState() == GameState.gameState.ENDING){
            commandSender.sendMessage(ChatColor.RED + "The game has not started yet idiot >:(");
            return true;
        }
        gameTimer.stopGame(true);
        Bukkit.getLogger().log(Level.WARNING, "Game stopped");
        return true;
    }
}
