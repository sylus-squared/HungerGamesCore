package dev.sylus.HungerGamesCore.Commands;

import dev.sylus.HungerGamesCore.Enums.GameState;
import dev.sylus.HungerGamesCore.Game.Game;
import dev.sylus.HungerGamesCore.HungerGamesCore;
import dev.sylus.HungerGamesCore.Tasks.GameTimer;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class StartDeathmatch implements CommandExecutor {

    GameTimer gameTimer;
    Game game;

    public StartDeathmatch(GameTimer gameTimerInstance, Game gameInstance){ // Constructor
        gameTimer = gameTimerInstance;
        game = gameInstance;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (game.getState() == GameState.gameState.PREGAME || game.getState() == GameState.gameState.ENDING){
            sender.sendMessage(ChatColor.RED + "Cannot start the deathmatch outside of a game");
            return true;
        }
        sender.sendMessage(ChatColor.RED + "Started the deathmatch");
        gameTimer.startDeathmatch();
        return true;
    }
}
