package me.djelectro.gamesystem.core.modules;

import io.papermc.paper.event.player.AsyncChatEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class ChatControl extends Module implements Listener {

    private boolean chatSilenced = false;

    @EventHandler
    public void chatHandler(AsyncChatEvent event){
        if(chatSilenced)
            event.setCancelled(true);
    }

    public void silenceChat(){
        chatSilenced = true;
    }

    public void unsilenceChat(){
        chatSilenced = false;
    }



}
