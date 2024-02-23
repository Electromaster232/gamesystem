package me.djelectro.gamesystem.buildserver;

import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandAPIBukkitConfig;
import me.djelectro.gamesystem.core.Main;
import me.djelectro.gamesystem.core.MiniPlugin;
import me.djelectro.gamesystem.core.utils.CommandClass;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.reflections.Reflections;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;

import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;

import static org.reflections.scanners.Scanners.SubTypes;

public final class BuildServer extends MiniPlugin implements Listener {

    private static BuildServerManager manager;

    public BuildServer(Main myRootPlugin){
        super("Build", myRootPlugin);
    }


    @Override
    public void onEnable() {

        log("Build Server Plugin Initialized", Level.INFO);
        CommandAPI.onEnable();

        List<ClassLoader> classLoadersList = new LinkedList<>();
        classLoadersList.add(ClasspathHelper.contextClassLoader());
        classLoadersList.add(ClasspathHelper.staticClassLoader());
        Reflections reflections = new Reflections(new ConfigurationBuilder().setUrls(ClasspathHelper.forClassLoader(classLoadersList.toArray(new ClassLoader[0]))).forPackage("me.djelectro.gamesystem.buildserver").filterInputsBy(new FilterBuilder().includePackage("me.djelectro.gamesystem.buildserver.commands")).setScanners(SubTypes.filterResultsBy(c->true)));
        for(Class<?> classes : reflections.get(SubTypes.of(Object.class).asClass())){
            if(classes.isAnnotationPresent(CommandClass.class)){
                CommandAPI.registerCommand(classes);
            }

        }

        manager = new BuildServerManager();
        getRootPlugin().getServer().getPluginManager().registerEvents(manager, getRootPlugin());

        //rootPlugin.getModuleLoader().loadChatControl();

    }

    @Override
    public void onLoad(){
        log("Build Server Plugin Loaded", Level.INFO);
        CommandAPI.onLoad(new CommandAPIBukkitConfig((JavaPlugin) getRootPlugin()).shouldHookPaperReload(true));
    }


    @Override
    public void onDisable() {
        // Plugin shutdown logic
        CommandAPI.onDisable();
    }

    public static BuildServerManager getManager(){return manager;}
}
