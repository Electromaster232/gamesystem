package me.djelectro.gamesystem.core;

import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;

public class MiniPlugin {


    public String miniPluginName;
    private static Main rootPlugin;

    public MiniPlugin(String miniPluginName, Main myRootPlugin){
        this.miniPluginName = miniPluginName;
        rootPlugin = myRootPlugin;
    }

    public void onEnable(){}

    public void onDisable(){}

    public void onLoad(){}

    public void log(String message, Level level){
        rootPlugin.getLogger().log(level, "["+miniPluginName+"] " + message);
    }

    public static Main getRootPlugin(){return rootPlugin;}

}
