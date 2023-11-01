package dev.sylus.HungerGamesCore.Game;

import dev.sylus.HungerGamesCore.Files.Databases;
import dev.sylus.HungerGamesCore.Files.Files;
import dev.sylus.HungerGamesCore.HungerGamesCore;
import dev.sylus.HungerGamesCore.Tasks.GameRunTask;
import dev.sylus.HungerGamesCore.Tasks.GameTimer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scoreboard.*;


public class Scorebord implements Listener {
    Game game;
    Files files;
    Databases databases;
    String serverCode;
    String numberString;
    GameRunTask gameRunTask;
    GameTimer gameTimer;
    String formatedTime;
    String currentEvent = "§6Not started";
    HungerGamesCore main;

    /*
    The scoreboard will look like this:

    The Hunger Games (Gold)
    ----------------------------------(White)
    Game(Aqua): (White) <what game number this is (based off the server code) for example: 2/3> (White)
    Players alive(Aqua): (White) <Players alive> (Green)
    Kills(Gold): (White) <Players kills> (Yellow)
    Chest refil in (White)
    <Time untill refil> (Red)
    ----------------------------------(White)
    Games score(Green): (White) <The score that the player got this game> (Aqua)
    Total score(Green): (White) <The players overall socre> (Aqua)
    ----------------------------------(White)
    <Server Code> (White)
     */

    public Scorebord(Game gameInstance, Files filesInstance, Databases databasesInstance, GameTimer gameTimerInstance, HungerGamesCore mainInstance){ // Constructor
        game = gameInstance;
        files = filesInstance;
        databases = databasesInstance;
        gameTimer = gameTimerInstance;
        serverCode = files.getConfig("worldData").getString("worldData.serverCode");
        main = mainInstance;
        String input = serverCode;

        // Find the index of the last digit in the string to get the game number (from the server code)
        int lastIndex = input.length() - 1;
        while (lastIndex >= 0 && Character.isDigit(input.charAt(lastIndex))) {
            lastIndex--;
        }

        numberString = input.substring(lastIndex + 1);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        this.refreshScorebordAll();
        createBoard(event.getPlayer());
    }

    public void createBoard(Player player) {
        ScoreboardManager manager = Bukkit.getScoreboardManager();
        Scoreboard board = manager.getNewScoreboard();
        Objective obj = board.registerNewObjective("HungerGamesScorebord-1", "dummy", ChatColor.translateAlternateColorCodes('&', ChatColor.BOLD + "&6The Hunger Games"));
        // obj.setDisplayName("");

        switch (game.getState()) {
            case ACTIVE:
                int seconds = Integer.parseInt(gameTimer.getTimeLeft());
                int minutes = seconds / 60;
                seconds = seconds % 60;

                if (minutes == 0) {
                    this.formatedTime = String.format("%02d", seconds); // No minutes, formatted as 0:seconds
                } else {
                    this.formatedTime = String.format("%d:%02d", minutes, seconds); // Formated as minutes:seconds
                }
                this.currentEvent = "§cChest refil in: §e" + formatedTime;
                break;

            case PREGAME:
                this.currentEvent = "§6Game starting, please wait";
                break;

            case TESTING:
                this.currentEvent = "§cTESTING";
                break;

            case SECONDHALF:
                if (!(gameTimer.getTimeLeft().equals("Not started") || gameTimer.getTimeLeft().equals("Refil over"))) {
                    seconds = Integer.parseInt(gameTimer.getTimeLeft());
                    minutes = seconds / 60;
                    seconds = seconds % 60;

                    if (minutes == 0) {
                        this.formatedTime = String.format("%02d", seconds); // No minutes, formatted as 0:seconds
                    } else {
                        this.formatedTime = String.format("%d:%02d", minutes, seconds); // Formated as minutes:seconds
                    }
                }
                this.currentEvent = "§cDeathmatch begins: §e" + formatedTime;
                break;

            case DEATHMATCH:
                this.currentEvent = "Deathmatch: ";
                break;

            case GAMESTART:
                this.currentEvent = "§6Game is starting, please wait";
                break;

        }


            Score score = obj.getScore(ChatColor.WHITE + "━━━━━━━━━━━━━━━━━━§7");
            score.setScore(10);

            Score score1 = obj.getScore("§bGame§f: " + numberString + "/§b3");
            score1.setScore(9);

            Score score2 = obj.getScore("§bPlayers alive§f: " + ChatColor.GREEN + game.getPlayerNumbers());
            score2.setScore(8);

            Score score3 = obj.getScore("§6Kills§f: " + game.getLocalPlayerData(player.getUniqueId()).get(1));
            score3.setScore(7);

            Score score4 = obj.getScore("§fNext event");
            score4.setScore(6);

            Score score5 = obj.getScore(ChatColor.RED + currentEvent);
            score5.setScore(5);

            Score score6 = obj.getScore(ChatColor.WHITE + "━━━━━━━━━━━━━━━━━━");
            score6.setScore(4);

            Score score7 = obj.getScore("§aGames score§f: " + game.getLocalPlayerData(player.getUniqueId()).get(2));
            score7.setScore(3);

            Score score8 = obj.getScore("§aTotal score§f: " + databases.getTotalPoints(player));
            score8.setScore(2);

            Score score9 = obj.getScore(ChatColor.WHITE + "━━━━━━━━━━━━━━━━━━§c");
            score9.setScore(1);

            Score score10 = obj.getScore(ChatColor.BLUE + serverCode);
            score10.setScore(0);

            player.setScoreboard(board);
            refreshScorebordAll();
        }


    public void refreshScorebordAll(){

        if (databases == null){
            databases = main.getDatabases();
        }

        for (Player players: Bukkit.getOnlinePlayers()){
            switch (game.getState()){
                case ACTIVE:
                    int seconds = Integer.parseInt(gameTimer.getTimeLeft());
                    int minutes = seconds / 60;
                    seconds = seconds % 60;

                    if (minutes == 0) {
                        this.formatedTime = String.format("%02d", seconds); // No minutes, formatted as 0:seconds
                    } else {
                        this.formatedTime = String.format("%d:%02d", minutes, seconds); // Formated as minutes:seconds
                    }
                    this.currentEvent = "§cChest refil in: §e" + formatedTime;
                    break;

                case PREGAME:
                    this.currentEvent = "§6Game starting, please wait";
                    break;

                case TESTING:
                    this.currentEvent = "§cTESTING";
                    break;

                case SECONDHALF:
                   // Bukkit.getLogger().log(Level.INFO, secondHalfTimer.getTimeLeft() + " This is the time left");
                    if (!(gameTimer.getTimeLeft().equals("Not started") || gameTimer.getTimeLeft().equals("Deathmatch time"))) {
                        seconds = Integer.parseInt(gameTimer.getTimeLeft());
                        minutes = seconds / 60;
                        seconds = seconds % 60;

                        if (minutes == 0) {
                            this.formatedTime = String.format("%02d", seconds); // No minutes, formatted as 0:seconds
                        } else {
                            this.formatedTime = String.format("%d:%02d", minutes, seconds); // Formated as minutes:seconds
                        }
                    }
                    this.currentEvent = "§cDeathmatch: §e" + formatedTime;
                    break;

                case DEATHMATCH:
                    if (!(gameTimer.getTimeLeft().equals("Not started") || gameTimer.getTimeLeft().equals("Deathmatch time"))) {
                        seconds = Integer.parseInt(gameTimer.getTimeLeft());
                        minutes = seconds / 60;
                        seconds = seconds % 60;

                        if (minutes == 0) {
                            this.formatedTime = String.format("%02d", seconds); // No minutes, formatted as 0:seconds
                        } else {
                            this.formatedTime = String.format("%d:%02d", minutes, seconds); // Formated as minutes:seconds
                        }
                    }

                    this.currentEvent = "§6Game end: §e" + formatedTime ;
                    break;

                case GAMESTART:
                    this.currentEvent = "§6Game starting";
                    break;

                case ENDING:
                    this.currentEvent = "§6Game over!";

            }

            ScoreboardManager manager = Bukkit.getScoreboardManager();
            Scoreboard board = manager.getNewScoreboard();
            Objective obj = board.registerNewObjective("HungerGamesScorebord-1", "dummy", ChatColor.translateAlternateColorCodes('&', "&6The Hunger Games"));
            // obj.setDisplayName("");
            obj.setDisplaySlot(DisplaySlot.SIDEBAR);

            Score score = obj.getScore(ChatColor.WHITE + "━━━━━━━━━━━━━━━━━━━━━§7");
            score.setScore(10);

            Score score1 = obj.getScore("§bGame§f: " + numberString + "/§b3");
            score1.setScore(9);

            Score score2 = obj.getScore("§bPlayers alive§f: " + ChatColor.GREEN + game.getPlayerNumbers());
            score2.setScore(8);

            Score score3 = obj.getScore("§6Kills§f: " + game.getLocalPlayerData(players.getUniqueId()).get(1));
            score3.setScore(7);

            Score score4 = obj.getScore("§fNext event");
            score4.setScore(6);

            Score score5 = obj.getScore(ChatColor.RED + currentEvent);
            score5.setScore(5);

            Score score6 = obj.getScore(ChatColor.WHITE + "━━━━━━━━━━━━━━━━━━━━━"); // This is not showing for some reason
            score6.setScore(4);

            Score score7 = obj.getScore("§aGames score§f: " + game.getLocalPlayerData(players.getUniqueId()).get(2));
            score7.setScore(3);

            Score score8 = obj.getScore("§aTotal score§f: " + databases.getTotalPoints(players));
            score8.setScore(2);

            Score score9 = obj.getScore(ChatColor.WHITE + "━━━━━━━━━━━━━━━━━━━━━§c");
            score9.setScore(1);

            Score score10 = obj.getScore(ChatColor.BLUE + serverCode);
            score10.setScore(0);

            players.setScoreboard(board);
        }

    }

    public void refreshScorebordPlayer(Player player){

    }

}