package me.djelectro.gamesystem.core.modules;

import me.djelectro.gamesystem.core.Main;
import me.djelectro.gamesystem.game.GamePlugin;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.ArrayList;

public class VanishControl implements Listener {

    private Main mainPlugin;
    private ArrayList<Player> vanishedList = new ArrayList<>();


    public VanishControl(Main main){
        mainPlugin = main;
    }

    public boolean isPlayerVanished(Player p){return vanishedList.contains(p);}

    public void vanishPlayer(Player p){
        vanishedList.add(p);
       if(mainPlugin.getLoadedMiniPlugin() instanceof GamePlugin){
           for(Player x : GamePlugin.getManager().getPlayers(false)){
               x.hidePlayer(mainPlugin, p);
           }
       }
    }

    public void unvanishPlayer(Player p){
        vanishedList.remove(p);
        if(mainPlugin.getLoadedMiniPlugin() instanceof GamePlugin){
            for(Player x : GamePlugin.getManager().getPlayers(false)){
                x.showPlayer(mainPlugin, p);
            }
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event){
        // If a new player joins the server
        for(Player p : vanishedList){
            event.getPlayer().hidePlayer(mainPlugin, p);
        }
    }

}
