package me.djelectro.gamesystem.core;

import me.djelectro.gamesystem.core.modules.ChatControl;
import me.djelectro.gamesystem.core.modules.VanishControl;

public class ModuleLoader {

    private ChatControl chatControl;
    private VanishControl vanishControl;
    private Main main;

    public ModuleLoader(Main main){
        this.main = main;
    }

    // ========== CHAT CONTROL ==========
    public ModuleLoader loadChatControl(){
            chatControl = new ChatControl();
            main.getServer().getPluginManager().registerEvents(chatControl, main);
            return this;
    }

    public ChatControl getChatControl() {
        if(chatControl == null)
            loadChatControl();
        return chatControl;
    }

    // ========== VANISH CONTROL ==========
    public ModuleLoader loadVanishControl(){
        vanishControl = new VanishControl(main);
        main.getServer().getPluginManager().registerEvents(vanishControl, main);
        return this;
    }

    public VanishControl getVanishControl(){
        if(vanishControl == null)
            loadVanishControl();
        return vanishControl;
    }

}
