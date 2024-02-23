package me.djelectro.gamesystem.game.events;

import me.djelectro.gamesystem.game.games.Game;
import me.djelectro.gamesystem.game.utils.GameState;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class GameStateChangeEvent extends Event {

    private GameState newState;
    private Game _game;

    public GameStateChangeEvent(GameState newState, Game game){
        this.newState = newState;
        this._game = game;
    }

    private static final HandlerList HANDLER_LIST = new HandlerList();

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    public GameState getNewState() {
        return newState;
    }

    public Game getGame(){
        return _game;
    }
}
