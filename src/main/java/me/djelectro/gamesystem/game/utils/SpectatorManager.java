package me.djelectro.gamesystem.game.utils;

import me.djelectro.gamesystem.game.GamePlugin;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

public class SpectatorManager implements Listener {


    public void enableSpecForPlayer(Player p){
        // Get every player on the server and hide the targeted player for

        p.setAllowFlight(true);
        GamePlugin.getRootPlugin().getModuleLoader().getVanishControl().vanishPlayer(p);
    }

    public void disableSpecForPlayer(Player p){
        p.setAllowFlight(false);
        GamePlugin.getRootPlugin().getModuleLoader().getVanishControl().unvanishPlayer(p);
    }




}
