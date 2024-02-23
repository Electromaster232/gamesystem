package me.djelectro.gamesystem.game.games;

import me.djelectro.gamesystem.game.GameManager;
import me.djelectro.gamesystem.game.GamePlugin;
import me.djelectro.gamesystem.game.events.GameStateChangeEvent;
import me.djelectro.gamesystem.core.utils.F;
import me.djelectro.gamesystem.game.games.kits.Kit;
import me.djelectro.gamesystem.game.utils.GameState;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.GameRule;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Objects;
import java.util.Random;

public class Game implements Listener {

    private String gameName;
    private String gameDesc;

    private boolean announcementEnabled = false;

    private String worldName;

    private int gameMapId;

    private boolean worldLoaded;

    private int pendingTimer;

    public GameRules rules;

    private HashMap<Player, Kit> selectedKit = new HashMap<>();

    public Game(){}

    public Game(String gameName, String gameDesc, int gameMapId, GameRules rules){
        this.gameName = gameName;
        this.gameDesc = gameDesc;
        this.gameMapId = gameMapId;
        this.rules = rules;
    }


    public GameState getGameState(){
        return getManager().getGameState();
    }

    protected GameManager getManager(){
        return GamePlugin.getManager();
    }

    public void setWorldName(String worldName){
        this.worldName = worldName;
    }

    @EventHandler
    public void onBaseLoad(GameStateChangeEvent event) {

        // Prepare worlds for game
        if (event.getNewState() != GameState.LOADING)
            return;

        if(worldLoaded)
            return;
        
        // Ensure a lobby is loaded into the first slot. This will be null if we are just starting the game management plugin
        if(getManager().getWorldManager().getLobby() == null) {
            GamePlugin.getManager().getWorldManager().loadLobbyByName("lobby");
        }
        //Eventually this will randomly select an arena matching the group ID and copy it. For now it does not.
        if(Objects.equals(worldName, ""))
            GamePlugin.getManager().getWorldManager().loadArenaByName("headquarters");
        else
            GamePlugin.getManager().getWorldManager().loadArenaByName(worldName);


        // Arena gamerules -- required here because Paper stores gamerules per-world
        getManager().getWorldManager().getArena().setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false);
        getManager().getWorldManager().getArena().setGameRule(GameRule.DO_WEATHER_CYCLE, false);
        getManager().getWorldManager().getArena().setGameRule(GameRule.SHOW_DEATH_MESSAGES, false);
        getManager().getWorldManager().getArena().setGameRule(GameRule.DO_ENTITY_DROPS, false);
        getManager().setGameState(GameState.RECRUITING);

        for(Player p : getManager().getPlayers(false)){
            p.teleport(getManager().getWorldManager().getLobby().getSpawnLocation());
        }
        worldLoaded = true;
    }

    @EventHandler
    public void onBaseRecruiting(GameStateChangeEvent event){
        if(getGameState() != GameState.RECRUITING)
            return;
        if(!announcementEnabled) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    if (getGameState() != GameState.RECRUITING) {
                        this.cancel();
                        return;
                    }
                    for (Player p : getManager().getPlayers(false)) {
                        p.sendMessage(F.main("Announcement", "This is a test announcement"));
                    }
                }
            }.runTaskTimer(GamePlugin.getRootPlugin(), 1L, 60L);
        }
        announcementEnabled = true;
    }

    @EventHandler
    public void onBasePending(GameStateChangeEvent event){
        if(getGameState() != GameState.PENDING)
            return;

        // Send all players into arena world
        Location[] spawnLocList = getManager().getWorldManager().getDataLocations(getManager().getWorldManager().getArena()).keySet().toArray(new Location[0]);
        int locNum = 0;
        for(Player p: getManager().getPlayers(false)){
            p.teleport(spawnLocList[locNum]);
            locNum++;
            p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 2f, 1f);
            announceStart(p);
        }

        //Start timer
        pendingTimer = rules.gamePendingTimer;
        new BukkitRunnable(){
            @Override
            public void run(){
                if(pendingTimer <= 0){
                    getManager().setGameState(GameState.RUNNING);
                }
                if(pendingTimer <= 0 || getGameState() != GameState.PENDING){
                    this.cancel();
                    return;
                }
                for(Player p: getManager().getPlayers(false)){
                    //p.sendMessage(F.main("Start", Integer.toString(pendingTimer)));
                    p.sendActionBar(Component.text("Game starts in " + Integer.toString(pendingTimer)));
                    p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_HAT, 2f, 2f);
                }
                pendingTimer--;

            }
        }.runTaskTimer(GamePlugin.getRootPlugin(), 0L, 20L);
    }

    @EventHandler
    public void onBaseRun(GameStateChangeEvent event){
        if(getGameState() != GameState.RUNNING)
            return;

        for(Player p : getManager().getPlayers(false)){
            p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 2f, 2f);
        }
    }

    @EventHandler
    public void freezeStart(PlayerMoveEvent event){
        if(getGameState() != GameState.PENDING)
            return;

        if(rules.freezeStart)
            event.setCancelled(true);
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event){
        if(!rules.allowBlockPlace)
            event.setCancelled(true);
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event){
        if(!rules.allowBlockBreak)
            event.setCancelled(true);
    }

    @EventHandler
    public void onHunger(FoodLevelChangeEvent event){
        if(!rules.hungerEnabled)
            event.setCancelled(true);
    }

    protected void announceStart(Player playerToAnnounceTo){
        // TODO: Fix this
        Component message = Component.text("===============================").color(NamedTextColor.GREEN).appendNewline().append(Component.text(gameName).color(NamedTextColor.GOLD)).appendNewline().append(Component.text(" - ").color(NamedTextColor.WHITE)).appendNewline().append(Component.text(gameDesc)).append(Component.text("===============================").color(NamedTextColor.GREEN));
        playerToAnnounceTo.sendMessage(message);
    }


    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event){
        if(getGameState() != GameState.RUNNING)
            return;

        event.setCancelled(true);
        event.getPlayer().getInventory().clear();
        new BukkitRunnable(){
            @Override
            public void run(){
                getPlayerKit(event.getPlayer()).giveItems();
                Location[] spawnLocList = getManager().getWorldManager().getDataLocations(getManager().getWorldManager().getArena()).keySet().toArray(new Location[0]);
                event.getPlayer().teleport(spawnLocList[new Random().nextInt(spawnLocList.length)]);
                this.cancel();
            }
        }.runTaskTimer(GamePlugin.getRootPlugin(), 1L, 5L);
    }



    public void assignKit(Player p, Kit kit){
        selectedKit.remove(p);
        selectedKit.put(p, kit);
    }

    @EventHandler
    public void baseGiveKit(GameStateChangeEvent event){
        if(getGameState() != GameState.RUNNING){
            return;
        }

        for(Kit k : selectedKit.values()){
            k.giveItems();
        }


    }

    public Kit getPlayerKit(Player p){
        return selectedKit.get(p);
    }



}
