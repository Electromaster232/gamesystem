package me.djelectro.gamesystem.game.games;

public class GameRules {

    public boolean freezeStart = false;
    public int gameStartTimer = 10;

    // This is to allow for players to move worlds and get prepared.
    public int gamePendingTimer = 5;

    public boolean allowBlockPlace = false;
    public boolean allowBlockBreak = false;

    public boolean hungerEnabled = false;

    public GameRules setFreezeStart(boolean freezeStart) {
        this.freezeStart = freezeStart;
        return this;
    }

    public GameRules setGameStartTimer(int value){
        this.gameStartTimer = value;
        return this;
    }

    public GameRules setAllowBlockPlace(boolean value){
        allowBlockPlace = value;
        return this;
    }

    public GameRules setAllowBlockBreak(boolean value){
        allowBlockBreak = value;
        return this;
    }

    public GameRules setHungerEnabled(boolean value){
        hungerEnabled = value;
        return this;
    }


}
