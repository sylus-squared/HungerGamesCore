package dev.sylus.HungerGamesCore.Enums;

public class GameState {
    public enum gameState {
        PREGAME, GAMESTART, ACTIVE, SECONDHALF, DEATHMATCH, ENDING, TESTING
        // SECONDHALF is used to increase the loot amount for the chest refill
        // Opening unopened chests in the second half will give different loot (Still depends on chest rarity)
    }
}
