package dev.sylus.HungerGamesCore.Tasks;

import dev.sylus.HungerGamesCore.Game.Game;
import dev.sylus.HungerGamesCore.Game.Scorebord;
import dev.sylus.HungerGamesCore.HungerGamesCore;

public class TasksManager {
    GameCountDownTask gameCountDownTask;
    EndingTimer endingTimer;
    GameRunTask gameRunTask;
    GameTimer gameTimer;

    // Dependencies
    Game game;
    HungerGamesCore main;
    Scorebord scorebord;

    public TasksManager(Game gameInstance, HungerGamesCore mainInstance, Scorebord scorebordInstance){ // Constructor
        // Initialise all the task stuffs
        game = gameInstance;
        main = mainInstance;
        scorebord = scorebordInstance;

        gameRunTask = new GameRunTask(game, main);
        endingTimer = new EndingTimer(game, main, false);
        gameTimer = new GameTimer(main, game);
        gameCountDownTask = new GameCountDownTask(game, main);
    }

    public GameRunTask getGameRunTask(){
        return gameRunTask;
    }
    public EndingTimer getEndingTimer(){
        return endingTimer;
    }

    public GameTimer getGameTimer(){
        return gameTimer;
    }

    public GameCountDownTask getGameCountDownTask(){
        return gameCountDownTask;
    }

    public void stopAll(){
        // Need to check if the timer is actually running on these before stopping them
    }
}
