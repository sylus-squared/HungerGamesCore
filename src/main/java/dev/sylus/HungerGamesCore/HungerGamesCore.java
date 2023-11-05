package dev.sylus.HungerGamesCore;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import dev.sylus.HungerGamesCore.Commands.*;
import dev.sylus.HungerGamesCore.Enums.GameState;
import dev.sylus.HungerGamesCore.Events.*;
import dev.sylus.HungerGamesCore.Files.Databases;
import dev.sylus.HungerGamesCore.Files.Files;
import dev.sylus.HungerGamesCore.Game.Border;
import dev.sylus.HungerGamesCore.Game.Game;
import dev.sylus.HungerGamesCore.Game.ChestManager;
import dev.sylus.HungerGamesCore.Game.Scorebord;
import dev.sylus.HungerGamesCore.Tasks.GameCountDownTask;
import dev.sylus.HungerGamesCore.Tasks.GameRunTask;
import dev.sylus.HungerGamesCore.Tasks.GameTimer;
import dev.sylus.HungerGamesCore.Utils.ServerUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.PluginMessageListener;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class HungerGamesCore extends JavaPlugin implements PluginMessageListener {

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
    ServerUtil serverUtil;
    boolean canOpenChests = false;


    @Override
    public void onEnable() {
        // Plugin startup logic
        // Initialise everything

        files = new Files(this, "worldData.yml");
        serverUtil = new ServerUtil(this);
        border = new Border(files);
        chestManager = new ChestManager(files, this);
        game = new Game(this, chestManager, files, border, serverUtil, databases);
        databases = new Databases(game, this, files);
        scorebord = new Scorebord(game, files, databases, getGameTimer(), this); // this might not work :/
        gameRunTask = new GameRunTask(game, this, databases, chestManager, serverUtil);
        gameCountDownTask = new GameCountDownTask(game, this, chestManager, files, border, serverUtil, databases);
        gameTimer = new GameTimer(this, game, databases, chestManager, serverUtil);

        JoinAndLeave joinAndLeave = new JoinAndLeave(game, files, scorebord, gameTimer, databases);
        MovementFreeze movementFreeze = new MovementFreeze(game);
        GameCountDownTask task = new GameCountDownTask(game, this, chestManager, files, border, serverUtil, databases);
        PlayerDeathEvent playerDeathEvent = new PlayerDeathEvent(game, files, gameTimer, scorebord, databases);
        Damage damage = new Damage(game);
        NoSleep noSleep = new NoSleep();

        // Register the events
        getServer().getPluginManager().registerEvents(chestManager, this);
        getServer().getPluginManager().registerEvents(joinAndLeave, this);
        getServer().getPluginManager().registerEvents(movementFreeze, this);
        getServer().getPluginManager().registerEvents(scorebord, this);
        getServer().getPluginManager().registerEvents(playerDeathEvent, this);
        getServer().getPluginManager().registerEvents(damage, this);
        getServer().getPluginManager().registerEvents(noSleep, this);

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
        getCommand("join").setExecutor(new JoinServer(serverUtil));
        getCommand("lobby").setExecutor(new Lobby(serverUtil));

        saveDefaultConfig();

        this.getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        this.getServer().getMessenger().registerIncomingPluginChannel(this, "BungeeCord", this);

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
        for (Player players: Bukkit.getOnlinePlayers()){ // Updates all points gotten in the game to the database
            databases.addPointsToDB(players.getUniqueId()); // Updates the database with the local data
        }
        this.getServer().getMessenger().unregisterOutgoingPluginChannel(this);
        this.getServer().getMessenger().unregisterIncomingPluginChannel(this);
    }

    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] message) {
        if (!channel.equals("BungeeCord")) {
            return;
        }
        ByteArrayDataInput in = ByteStreams.newDataInput(message);
        String subchannel = in.readUTF();
        if (subchannel.equals("SomeSubChannel")) {
            // Use the code sample in the 'Response' sections below to read
            // the data.
        }
    }

    public HungerGamesCore getMain(){
        return this;
    }

    public GameCountDownTask getGameCountDownTask(){
        return new GameCountDownTask(game, this, chestManager, files, border, serverUtil, databases);
    }
    public GameTimer getGameTimer(){
        return new GameTimer(this, game, databases, chestManager, serverUtil);
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
