package me.djelectro.gamesystem.buildserver;

import com.google.gson.Gson;
import me.djelectro.gamesystem.core.utils.F;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class BuildServerManager implements Listener {

    private ArrayList<Player> authPlayers = new ArrayList<>();

    private boolean currentlyProcessing = false;

    private Location pos1;
    private Location pos2;

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event){
        event.getPlayer().sendMessage(F.main("Build", "Welcome to the build server! Please use /pass to authenticate."));
    }

    @EventHandler
    public void blockPlayerMove(PlayerMoveEvent event){
        if(!authPlayers.contains(event.getPlayer())){
            event.setCancelled(true);
            event.getPlayer().sendMessage(F.main("Build", "You must authenticate with /pass"));
        }
    }

    public void allowPlayer(Player player){
        authPlayers.add(player);
        player.setOp(true);
        player.setGameMode(GameMode.CREATIVE);
    }

    public void updatePos1(Location location){
        pos1 = location;
    }

    public void updatePos2(Location location){
        pos2 = location;
    }

    @EventHandler
    public void cancelChangePlace(BlockPlaceEvent event){
        if(currentlyProcessing)
            event.setCancelled(true);
    }

    @EventHandler
    public void cancelChangeBreak(BlockBreakEvent event){
        if(currentlyProcessing)
            event.setCancelled(true);
    }

    public void parseWorld(){
        currentlyProcessing = true;
        Date date = Calendar.getInstance().getTime();
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd-hhmmss");
        String strDate = dateFormat.format(date);
        BuildServer.getRootPlugin().getServer().dispatchCommand(BuildServer.getRootPlugin().getServer().getConsoleSender(), "mv clone " + pos1.getWorld().getName() + " " + pos1.getWorld().getName() + "-"+strDate);
        HashMap<String, Material> spawnLocations = new HashMap<>();
        for(Block l : blocksFromTwoPoints(pos1,pos2)){
            if(l.getType() == Material.LIGHT_WEIGHTED_PRESSURE_PLATE){
                Location locToUse = l.getLocation().subtract(0,1,0);
                spawnLocations.put(String.format("%s,%s,%s", locToUse.getX(), locToUse.getY(), locToUse.getZ()), locToUse.getBlock().getType());
                l.setType(Material.AIR);
                locToUse.getBlock().setType(Material.AIR);
            }
        }
        String json = new Gson().toJson(spawnLocations);
        try {
            new File(pos1.getWorld().getWorldFolder().getAbsolutePath() + "/parsedata.json").delete();
            FileWriter outFile = new FileWriter(pos1.getWorld().getWorldFolder().getAbsolutePath() + "/parsedata.json");
            outFile.write(json);
            outFile.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        currentlyProcessing = false;

    }

    public boolean isParseReady(){
        return (pos1 != null && pos2 != null);
    }


    public static List<Block> blocksFromTwoPoints(Location loc1, Location loc2)
    {
        List<Block> blocks = new ArrayList<Block>();

        int topBlockX = (Math.max(loc1.getBlockX(), loc2.getBlockX()));
        int bottomBlockX = (Math.min(loc1.getBlockX(), loc2.getBlockX()));

        int topBlockY = (Math.max(loc1.getBlockY(), loc2.getBlockY()));
        int bottomBlockY = (Math.min(loc1.getBlockY(), loc2.getBlockY()));

        int topBlockZ = (Math.max(loc1.getBlockZ(), loc2.getBlockZ()));
        int bottomBlockZ = (Math.min(loc1.getBlockZ(), loc2.getBlockZ()));

        for(int x = bottomBlockX; x <= topBlockX; x++)
        {
            for(int z = bottomBlockZ; z <= topBlockZ; z++)
            {
                for(int y = bottomBlockY; y <= topBlockY; y++)
                {
                    Block block = loc1.getWorld().getBlockAt(x, y, z);

                    blocks.add(block);
                }
            }
        }

        return blocks;
    }


}
