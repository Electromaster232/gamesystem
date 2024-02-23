package me.djelectro.gamesystem.game.utils;


import org.bukkit.entity.Player;

import java.util.HashMap;

// Purpose: Provide a simple scoring system for use in any solo games
public class SimpleSoloScore {

    private HashMap<Player, Integer> scores = new HashMap<>();

    public void addScore(Player p){
        if(!scores.containsKey(p)){
            scores.put(p, 1);
        }
        else {
            int score = scores.get(p);
            scores.remove(p);
            scores.put(p, score+1);
        }
    }

    public Integer getScore(Player p){
        return scores.get(p);
    }

    public HashMap<Player, Integer> getScoreMap(){
        return scores;
    }
}
