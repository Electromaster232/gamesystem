package me.djelectro.gamesystem.game;

import me.djelectro.gamesystem.game.events.GameStateChangeEvent;
import me.djelectro.gamesystem.core.utils.F;
import me.djelectro.gamesystem.game.utils.GameState;
import me.djelectro.gamesystem.game.games.Game;
import me.djelectro.gamesystem.game.utils.SpectatorManager;
import me.djelectro.gamesystem.game.utils.WorldManager;
import org.bukkit.GameMode;
import org.bukkit.GameRule;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class GameManager implements Listener {

    private Game currentGame;

    private WorldManager worldManager;

    private SpectatorManager spectatorManager;

    private HashMap<Player, Boolean> playerList = new HashMap<>();

    public GameManager(){
        worldManager = new WorldManager();
        spectatorManager = new SpectatorManager();
        GamePlugin.getRootPlugin().getServer().getPluginManager().registerEvents(spectatorManager, GamePlugin.getRootPlugin());
    }

    private int currentStartTimer;

    private GameState state = GameState.LOADING;


    public GameState getGameState(){return state;}

    public void setGameState(GameState gameState) {
        state = gameState;
        System.out.println("Game state is now " + gameState.name());
        GamePlugin.getRootPlugin().getServer().getPluginManager().callEvent(new GameStateChangeEvent(gameState, currentGame));
    }


    @EventHandler
    public void playerJoin(PlayerJoinEvent e){
        if(currentGame != null && getGameState() == GameState.RECRUITING){
            e.getPlayer().teleport(getWorldManager().getLobby().getSpawnLocation());
        }
        if(getGameState() == GameState.PENDING || getGameState() == GameState.RUNNING) {
            playerList.put(e.getPlayer(), false);
            getSpectatorManager().enableSpecForPlayer(e.getPlayer());
            e.getPlayer().teleport(getWorldManager().getArena().getSpawnLocation());
        }
        else
            playerList.put(e.getPlayer(), true);
        e.getPlayer().setGameMode(GameMode.SURVIVAL);
        e.getPlayer().setFoodLevel(100);
        e.getPlayer().setHealth(20);
        e.getPlayer().getInventory().clear();
    }

    @EventHandler
    public void playerQuit(PlayerQuitEvent e){
        getSpectatorManager().disableSpecForPlayer(e.getPlayer());
        playerList.remove(e.getPlayer());
    }

    public WorldManager getWorldManager() {
        return worldManager;
    }
    public SpectatorManager getSpectatorManager(){return spectatorManager;}

    public Player[] getPlayers(boolean aliveOnly){
        List<Player> res = new ArrayList<>();
        for (Player p: playerList.keySet()) {
            if(aliveOnly){
                if(playerList.get(p))
                    res.add(p);
            }
            else{
                res.add(p);
            }

        }
        return res.toArray(new Player[res.size()]);
    }

    public void loadGame(Game gameClass, String world){
        if(currentGame != null){
            stopGame();
        }
        currentGame = gameClass;
        GamePlugin.getRootPlugin().getServer().getPluginManager().registerEvents(currentGame, GamePlugin.getRootPlugin());
        currentGame.setWorldName(world);
        setGameState(GameState.LOADING);
        // Arena gamerules are set in Game after world loaded in
        // Lobby gamerules
        getWorldManager().getLobby().setGameRule(GameRule.DO_MOB_SPAWNING, false);
        getWorldManager().getLobby().setTime(1400);
        getWorldManager().getLobby().setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false);
        getWorldManager().getLobby().setGameRule(GameRule.DO_WEATHER_CYCLE, false);

    }

    @EventHandler
    public void onLobbyHunger(FoodLevelChangeEvent event){
        if(event.getEntity().getWorld() == getWorldManager().getLobby())
            event.setCancelled(true);
    }


    public boolean isReady(){
        // Return if the manager is ready (Game loaded, world loaded)
        return (currentGame != null && getGameState() == GameState.RECRUITING);

    }

    public void startGame(){

        // The current plan is that once the game is pending we pass off control to the game class to allow for any overrides or anything needed

        if(getGameState() != GameState.RECRUITING){
            return;
        }
        // Begin countdown
        setGameState(GameState.STARTING);
        // Find count, set to current timer
        currentStartTimer = currentGame.rules.gameStartTimer;
        new BukkitRunnable(){
            @Override
            public void run(){
                if(currentStartTimer <= 0){
                    setGameState(GameState.PENDING);
                }
                if(currentStartTimer <= 0 || getGameState() != GameState.STARTING){
                    this.cancel();
                    return;
                }
                for(Player p: getPlayers(false)){
                    p.sendMessage(F.main("Game", "Game starts in " + currentStartTimer + "!"));
                }
                currentStartTimer--;

            }
        }.runTaskTimer(GamePlugin.getRootPlugin(), 0L, 20L);

    }

    public void stopGame(){
        if(getGameState() != GameState.DEAD)
            setGameState(GameState.DEAD);
        HandlerList.unregisterAll(currentGame);
        for(Player p : getPlayers(false)){
            p.teleport(getWorldManager().getLobby().getSpawnLocation());
            getSpectatorManager().disableSpecForPlayer(p);
            p.getInventory().clear();
        }
        currentGame = null;
    }


    public void setDeathStatus(Player p, boolean status){
        playerList.put(p, status);
    }



}
