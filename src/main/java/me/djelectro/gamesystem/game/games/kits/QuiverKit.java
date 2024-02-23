package me.djelectro.gamesystem.game.games.kits;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class QuiverKit extends Kit {

    public QuiverKit(Player p) {
        super(p);
    }

    @Override
    public void giveItems(){
        myPlayer.getInventory().clear();
        myPlayer.getInventory().setItem(0, new ItemStack(Material.BOW, 1));
        myPlayer.getInventory().setItem(1, new ItemStack(Material.ARROW, 1));
        myPlayer.getInventory().setItem(2, new ItemStack(Material.STONE_AXE, 1));
    }
}
