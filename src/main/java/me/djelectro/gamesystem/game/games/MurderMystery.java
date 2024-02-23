package me.djelectro.gamesystem.game.games;

import me.djelectro.gamesystem.game.events.GameStateChangeEvent;
import me.djelectro.gamesystem.core.utils.F;
import me.djelectro.gamesystem.game.utils.GameState;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class MurderMystery extends Game implements Listener {

    public MurderMystery(){
        super("Murder Mystery", "Sussy imposter amogus", 1, new GameRules().setFreezeStart(true));
    }

    @EventHandler
    public void onLoad(GameStateChangeEvent event){
        if(event.getNewState() != GameState.LOADING)
            return;

        Player[] p = getManager().getPlayers(false);

        for(Player x: p){
            x.sendMessage(F.main("Murder", "Murder Mystery is loading!"));
        }

    }



}
