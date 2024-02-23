package me.djelectro.gamesystem.game.games.kits;

import org.bukkit.entity.Player;

public class Kit {

    protected final Player myPlayer;

    public Kit(Player p){
        myPlayer = p;
    }

    // Override me
    public void giveItems(){
    }
}
