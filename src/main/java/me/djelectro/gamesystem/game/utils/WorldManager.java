package me.djelectro.gamesystem.game.utils;

import com.google.gson.Gson;
import me.djelectro.gamesystem.game.GamePlugin;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.WorldCreator;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Scanner;

public class WorldManager {
    private World lobby;
    private World arena;

    private World newArena;


    public boolean loadArenaByName(String worldName){
        arena = GamePlugin.getRootPlugin().getServer().createWorld(new WorldCreator(worldName));
        return true;
    }

    public boolean loadLobbyByName(String worldName){
        lobby = GamePlugin.getRootPlugin().getServer().createWorld(new WorldCreator(worldName));
        return true;
    }

    public boolean loadNewArenaByName(String worldName){
        newArena = GamePlugin.getRootPlugin().getServer().createWorld(new WorldCreator(worldName));
        return true;
    }

    public World getArena() {
        return arena;
    }

    public World getLobby(){
        return lobby;
    }

    public World getSecondArena(){
        return newArena;
    }


    public HashMap<Location, Material> getDataLocations(World world){
        File dataFile = new File(world.getWorldFolder().getAbsolutePath() + "/parsedata.json");
        StringBuilder data = new StringBuilder();
        try {
            Scanner myReader = new Scanner(dataFile);
            while (myReader.hasNextLine()) {
                data.append(myReader.nextLine());
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        HashMap<String, String> jsonObject = new Gson().fromJson(data.toString(), HashMap.class);

        HashMap<Location, Material> resultMap = new HashMap<>();
        for(String s : jsonObject.keySet()){
            String[] splitSting = s.split("[, ]", 0);
            Double locX = Double.parseDouble(splitSting[0]);
            Double locY = Double.parseDouble(splitSting[1]);
            Double locZ = Double.parseDouble(splitSting[2]);
            resultMap.put(new Location(world, locX, locY, locZ), Material.valueOf(jsonObject.get(s)));
        }
        return resultMap;
    }

}
