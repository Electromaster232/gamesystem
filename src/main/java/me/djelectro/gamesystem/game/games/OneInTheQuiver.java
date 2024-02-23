package me.djelectro.gamesystem.game.games;

import me.djelectro.gamesystem.core.utils.F;
import me.djelectro.gamesystem.game.GamePlugin;
import me.djelectro.gamesystem.game.events.GameStateChangeEvent;
import me.djelectro.gamesystem.game.games.kits.QuiverKit;
import me.djelectro.gamesystem.game.utils.GameState;
import me.djelectro.gamesystem.game.utils.SimpleSoloScore;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.HashMap;
import java.util.Objects;

public class OneInTheQuiver extends Game implements Listener {

    private SimpleSoloScore scores;
    public OneInTheQuiver(){
        super("One in the Quiver", "Bows are one-hit-kill. If you miss your shot, be prepared to fight!", 2, new GameRules().setFreezeStart(true));
    }


    @EventHandler
    public void onStart(GameStateChangeEvent event){
        if(getGameState() != GameState.RUNNING)
            return;

        Scoreboard score = GamePlugin.getRootPlugin().getServer().getScoreboardManager().getMainScoreboard();

        Team t = score.getTeam("nhide");
        if(t == null) {
            t = score.registerNewTeam("nhide");
            t.setOption(Team.Option.NAME_TAG_VISIBILITY, Team.OptionStatus.NEVER);
        }
        for(Player p : getManager().getPlayers(true)){
            t.addEntry(p.getName());
        }

    }

    @EventHandler
    public void onPending(GameStateChangeEvent e){
        if(getGameState() != GameState.PENDING)
            return;

        for(Player p : getManager().getPlayers(false)){
            assignKit(p, new QuiverKit(p));
            p.setBedSpawnLocation(p.getLocation());
        }

        scores = new SimpleSoloScore();

    }

    @EventHandler
    public void onArrowHit(ProjectileHitEvent event){
        if(getGameState() != GameState.RUNNING)
            return;
        event.getEntity().remove();
        if (!(event.getHitEntity() instanceof Player))
            return;

        if(!(event.getEntity().getShooter() instanceof Player)){
            return;
        }

        ((Player) event.getHitEntity()).damage(21);
        event.getHitEntity().sendMessage(F.main("Death", "You were killed by " + ((Player) event.getEntity().getShooter()).getName()));
        ((Player) event.getEntity().getShooter()).sendMessage(F.main("Death", "You killed " + event.getHitEntity().getName()));
        scores.addScore((Player) event.getEntity().getShooter());
        ((Player) event.getEntity().getShooter()).getInventory().setItem(1, new ItemStack(Material.ARROW, 1));


    }

    @EventHandler
    public void playerAxeDeath(PlayerDeathEvent event){
        if(getGameState() != GameState.RUNNING)
            return;

        if(Objects.requireNonNull(event.getEntity().getLastDamageCause()).getCause() == EntityDamageEvent.DamageCause.CUSTOM)
            return;

        ((Player) event.getEntity().getKiller()).sendMessage(F.main("Death", "You killed " + event.getEntity().getName()));
        event.getEntity().sendMessage(F.main("Death", "You were killed by " + event.getEntity().getKiller().getName()));
        scores.addScore(event.getEntity().getKiller());
        if(event.getEntity().getKiller().getInventory().getItem(1) != null) {
            event.getEntity().getKiller().getInventory().setItem(1, new ItemStack(Material.ARROW, event.getEntity().getKiller().getInventory().getItem(1).getAmount()+1));
        }
        else {
            event.getEntity().getKiller().getInventory().setItem(1, new ItemStack(Material.ARROW, 1));
        }


    }


    @EventHandler
    public void checkForWinner(GameStateChangeEvent event){
        if(getGameState() != GameState.RUNNING)
            return;

        new BukkitRunnable(){
            @Override
            public void run(){
                HashMap<Player, Integer> topScores = scores.getScoreMap();
                int topScore = 0;
                Player topPlayer = null;
                for(Player p : topScores.keySet()){
                    int playerScore = topScores.get(p);
                    if(playerScore > topScore){
                        topScore = playerScore;
                        topPlayer = p;
                    }
                }
                
                if(topScore >= 3){
                    getManager().stopGame();
                    this.cancel();
                    for(Player p : getManager().getPlayers(false)){
                        p.sendMessage(F.main("Game", topPlayer.getName() + " has won the game!"));
                    }
                }
                
            }
        }.runTaskTimer(GamePlugin.getRootPlugin(), 1L, 10L);
    }






}
