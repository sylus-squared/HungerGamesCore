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

import java.util.ArrayList;


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
    String nextEvent = "§6Not started";
    HungerGamesCore main;
    ArrayList<Player> playersJoined = new ArrayList<>();
    ScoreboardManager manager;
    Scoreboard board;
    Objective obj;

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
        if (!(playersJoined.contains(event.getPlayer()))){
            createBoard(event.getPlayer());
        } else {
            playersJoined.add(event.getPlayer());
        }
    }

    public void createBoard(Player player) {
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
                nextEvent = "§fChest refil in:";
                this.currentEvent = " §a" + formatedTime;
                break;

            case PREGAME:
                nextEvent = "§6Starting soon";
                this.currentEvent = "§3";
                break;

            case TESTING:
                nextEvent = "§cTESTING";
                this.currentEvent = "§3";
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
                nextEvent = "§fDeathmatch in:";
                this.currentEvent = "§a" + formatedTime;
                break;

            case DEATHMATCH:
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

                nextEvent = "§fGame ends in:";
                this.currentEvent = "§a" + formatedTime;
                break;

            case GAMESTART:
                nextEvent = "§6Game is starting";
                this.currentEvent = "§3";
                break;

            case ENDING:
                nextEvent = "§fNext event";
                this.currentEvent = "§6Game over";
                break;

        }

        Score score11 = obj.getScore("§7Game " + numberString + "/3");
        score11.setScore(11);

        Score score10 = obj.getScore("§7 "); // New line
        score10.setScore(10);

        Score score9 = obj.getScore(nextEvent);
        score9.setScore(9);

        Score score8 = obj.getScore(currentEvent);// ━━━━━━━━━━━━━━━━━━§7
        score8.setScore(8);

        Score score7 = obj.getScore("§f "); // New line
        score7.setScore(7);

        Score score6 = obj.getScore("§fPlayers alive: §a" + game.getPlayerNumbers() + "/" + "24");
        score6.setScore(6);

        Score score5 = obj.getScore("§b"); // New Line
        score5.setScore(5);

        Score score4 = obj.getScore("§fKills §a" + databases.getLocalPlayerData(player.getUniqueId()).getCurrentKills() + "§7 | §fPoints §a" + databases.getLocalPlayerData(player.getUniqueId()).getGamePoints());
        score4.setScore(4);

        Score score3 = obj.getScore("§1"); // New line
        score3.setScore(3);

        Score score2 = obj.getScore("§fTotal points: §a" + databases.getLocalPlayerData(player.getUniqueId()).getCurrentPoints());
        score2.setScore(2);

        Score score1 = obj.getScore("§2"); // New line
        score1.setScore(1);

        Score score0 = obj.getScore("§6" + serverCode); // Points gained during this game
        score0.setScore(0);

        player.setScoreboard(board);
        refreshScorebordAll();
        }


    public void refreshScorebordAll(){

        if (databases == null){
            databases = main.getDatabases();
        }



        for (Player players: Bukkit.getOnlinePlayers()){
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
                    nextEvent = "§fChest refil in:";
                    this.currentEvent = " §a" + formatedTime;
                    break;

                case PREGAME:
                    nextEvent = "§fNext event";
                    this.currentEvent = "§6Starting soon";
                    break;

                case TESTING:
                    nextEvent = "§fNext event";
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
                    nextEvent = "§fDeathmatch in:";
                    this.currentEvent = "§a" + formatedTime;
                    break;

                case DEATHMATCH:
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

                    nextEvent = "§fGame ends in:";
                    this.currentEvent = "§a" + formatedTime;
                    break;

                case GAMESTART:
                    nextEvent = "§fNext event";
                    this.currentEvent = "§6Game is starting";
                    break;

                case ENDING:
                    nextEvent = "§fNext event";
                    this.currentEvent = "§6Game over";
                    break;
            }

            Score score11 = obj.getScore("§7Game " + numberString + "/3");
            score11.setScore(11);

            Score score10 = obj.getScore("§7 "); // New line
            score10.setScore(10);

            Score score9 = obj.getScore(nextEvent);
            score9.setScore(9);

            Score score8 = obj.getScore(currentEvent);// ━━━━━━━━━━━━━━━━━━§7
            score8.setScore(8);

            Score score7 = obj.getScore("§f "); // New line
            score7.setScore(7);

            Score score6 = obj.getScore("§fPlayers alive: §a" + game.getPlayerNumbers() + "/" + "24");
            score6.setScore(6);

            Score score5 = obj.getScore("§b"); // New Line
            score5.setScore(5);

            Score score4 = obj.getScore("§fKills §a" + databases.getLocalPlayerData(players.getUniqueId()).getCurrentKills() + "§7 | §fPoints §a" + databases.getLocalPlayerData(players.getUniqueId()).getGamePoints());
            score4.setScore(4);

            Score score3 = obj.getScore("§1"); // New line
            score3.setScore(3);

            Score score2 = obj.getScore("§fTotal points: §a" + databases.getLocalPlayerData(players.getUniqueId()).getCurrentPoints());
            score2.setScore(2);

            Score score1 = obj.getScore("§2"); // New line
            score1.setScore(1);

            Score score0 = obj.getScore("§6" + serverCode);
            score0.setScore(0);

            players.setScoreboard(board);

        }

    }

    public void refreshScorebordPlayer(Player player){

    }

}