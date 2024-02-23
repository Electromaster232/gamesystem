package me.djelectro.gamesystem.game.gamecommands;

import dev.jorel.commandapi.annotations.Command;
import dev.jorel.commandapi.annotations.Default;
import me.djelectro.gamesystem.core.utils.CommandClass;
import me.djelectro.gamesystem.core.utils.F;
import me.djelectro.gamesystem.game.GamePlugin;
import org.bukkit.entity.Player;

@Command("v")
@CommandClass
public class VanishCommand {

    @Default
    public static void vanish(Player p){
        if(!GamePlugin.getRootPlugin().getModuleLoader().getVanishControl().isPlayerVanished(p)) {
            GamePlugin.getRootPlugin().getModuleLoader().getVanishControl().vanishPlayer(p);
            p.sendMessage(F.main("Vanish", "You are now vanished."));
        }
        else{
            GamePlugin.getRootPlugin().getModuleLoader().getVanishControl().unvanishPlayer(p);
            p.sendMessage(F.main("Vanish", "You are no longer vanished."));
        }
    }
}
