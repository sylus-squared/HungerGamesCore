package dev.sylus.HungerGamesCore;

import dev.sylus.HungerGamesCore.Commands.*;
import dev.sylus.HungerGamesCore.Enums.GameState;
import dev.sylus.HungerGamesCore.Events.Damage;
import dev.sylus.HungerGamesCore.Events.JoinAndLeave;
import dev.sylus.HungerGamesCore.Events.MovementFreeze;
import dev.sylus.HungerGamesCore.Events.PlayerDeathEvent;
import dev.sylus.HungerGamesCore.Files.Databases;
import dev.sylus.HungerGamesCore.Files.Files;
import dev.sylus.HungerGamesCore.Game.Border;
import dev.sylus.HungerGamesCore.Game.Game;
import dev.sylus.HungerGamesCore.Game.ChestManager;
import dev.sylus.HungerGamesCore.Game.Scorebord;
import dev.sylus.HungerGamesCore.Tasks.GameCountDownTask;
import dev.sylus.HungerGamesCore.Tasks.GameRunTask;
import dev.sylus.HungerGamesCore.Tasks.GameTimer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class HungerGamesCore extends JavaPlugin {

    /*
    -----------------------------------------------------------------------
                                Hunger games core
    This plugin is made specifically for a Hunger games tournament that I am hosting
    It most likely won't work on other servers so use it at your own risk :)

    TODO:
    * Create the deathmatch system
    * Create the logic to teleport people to the correct spots (Gets the coordinates from the world data.yaml file)
    * Create the lobby plugin, that means armour stands :(
    * Create the API
    * Remember to actually update this
    * yaml death
    * CHEST RARITY (Fix the stupid NBT editor class not found exception)
    -----------------------------------------------------------------------
     */

    public Logger logger = Bukkit.getLogger();
    Game game;
    Files files;
    Databases databases;
    GameRunTask gameRunTask;

    GameCountDownTask gameCountDownTask;
    Scorebord scorebord;
    GameTimer gameTimer;
    ChestManager chestManager;
    Border border;
    boolean canOpenChests = false;


    @Override
    public void onEnable() {
        // Plugin startup logic
        // Initialise everything

        files = new Files(this, "worldData.yml");
        border = new Border(files);
        chestManager = new ChestManager(files, this);
        game = new Game(this, chestManager, files, border);
        databases = new Databases(game, this, files);
        scorebord = new Scorebord(game, files, databases, getGameTimer(), this); // this might not work :/
        gameRunTask = new GameRunTask(game, this, databases, chestManager);
        gameCountDownTask = new GameCountDownTask(game, this, chestManager, files, border);
        gameTimer = new GameTimer(this, game, databases, chestManager);

        JoinAndLeave joinAndLeave = new JoinAndLeave(game, files, scorebord, gameTimer, databases);
        MovementFreeze movementFreeze = new MovementFreeze(game);
        GameCountDownTask task = new GameCountDownTask(game, this, chestManager, files, border);
        PlayerDeathEvent playerDeathEvent = new PlayerDeathEvent(game, files, gameTimer, scorebord, databases);
        Damage damage = new Damage(game);

        // Register the events
        getServer().getPluginManager().registerEvents(chestManager, this);
        getServer().getPluginManager().registerEvents(joinAndLeave, this);
        getServer().getPluginManager().registerEvents(movementFreeze, this);
        getServer().getPluginManager().registerEvents(scorebord, this);
        getServer().getPluginManager().registerEvents(playerDeathEvent, this);
        getServer().getPluginManager().registerEvents(damage, this);

        // Initialise the commands
        getCommand("gameStart").setExecutor(new GameStart(game));
        getCommand("setState").setExecutor(new SetState(game, scorebord));
        getCommand("resetChests").setExecutor(new ResetChests(chestManager));
        getCommand("stopGame").setExecutor(new StopGame(gameTimer, game));
        getCommand("togglePlayerCount").setExecutor(new TogglePlayerCount(game));
        getCommand("giveChest").setExecutor(new GiveChest());
        getCommand("databaseTest").setExecutor(new DatabaseTest(databases)); // Remove this eventually
        getCommand("addPoints").setExecutor(new AddPoints(databases, scorebord));
        getCommand("resetPoints").setExecutor(new ResetPoints(databases, scorebord));

        saveDefaultConfig();

        game.setState(GameState.gameState.TESTING, "Main class");
        logger.log(Level.INFO, "CoreLoaded");
        logger.log(Level.INFO, "Game state changed to: " + game.getState());
        game.setVunrability(false); // Stop people being able to damage each other before the game starts

        try {
            File myObj = new File("filename.txt");
            if (myObj.createNewFile()) {
                System.out.println("File created: " + myObj.getName());
            } else {
                System.out.println("File already exists.");
            }
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        border.initialiseBorder();

        scorebord.refreshScorebordAll();
        databases.initialiseDatabase();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        databases.closeConnection();
    }

    public HungerGamesCore getMain(){
        return this;
    }

    public GameCountDownTask getGameCountDownTask(){
        return new GameCountDownTask(game, this, chestManager, files, border);
    }
    public GameTimer getGameTimer(){
        return new GameTimer(this, game, databases, chestManager);
    } // I should not have done this, it caused so many issues :(

    public void refreshScorebordAll(){
        scorebord.refreshScorebordAll();
    }

    public Databases getDatabases(){
        return databases;
    }

    public void setCanOpenChests(boolean newState){
        canOpenChests  = newState;
    }
    public boolean getCanOpenChests(){
        return canOpenChests;
    }

}
