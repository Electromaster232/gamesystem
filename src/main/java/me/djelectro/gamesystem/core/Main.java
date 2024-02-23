package me.djelectro.gamesystem.core;

import me.djelectro.gamesystem.buildserver.BuildServer;
import me.djelectro.gamesystem.game.GamePlugin;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;

public class Main extends JavaPlugin implements Listener {

    private static MiniPlugin loadedMiniPlugin;
    private static ModuleLoader moduleLoader;

    @Override
    public void onLoad(){
        this.saveDefaultConfig();
        FileConfiguration config = this.getConfig();

        config.addDefault("pluginMode", "game");
        config.options().copyDefaults(true);
        saveConfig();

        switch (config.getString("pluginMode")) {
            case "game" -> loadedMiniPlugin = new GamePlugin(this);
            case "build" -> loadedMiniPlugin = new BuildServer(this);
            default -> {
                getLogger().log(Level.SEVERE, "No valid plugin mode in config");
                return;
            }
        }

        loadedMiniPlugin.onLoad();
        moduleLoader = new ModuleLoader(this);
    }

    @Override
    public void onEnable() {
        loadedMiniPlugin.onEnable();
    }

    @Override
    public void onDisable(){
        loadedMiniPlugin.onDisable();
    }

    public ModuleLoader getModuleLoader(){
        return moduleLoader;
    }

    public MiniPlugin getLoadedMiniPlugin(){return loadedMiniPlugin;}
}
