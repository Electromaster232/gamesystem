package me.djelectro.gamesystem.game.gamecommands;

import dev.jorel.commandapi.annotations.Command;
import dev.jorel.commandapi.annotations.Subcommand;
import dev.jorel.commandapi.annotations.arguments.AStringArgument;
import me.djelectro.gamesystem.game.GameManager;
import me.djelectro.gamesystem.game.GamePlugin;
import me.djelectro.gamesystem.game.games.MurderMystery;
import me.djelectro.gamesystem.core.utils.CommandClass;
import me.djelectro.gamesystem.core.utils.F;
import me.djelectro.gamesystem.game.games.OneInTheQuiver;
import me.djelectro.gamesystem.game.utils.WorldManager;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

@Command("game")
@CommandClass
public class GameCommand {

    @Subcommand("load")
    public static void loadGame(Player player, @AStringArgument String gameName, @AStringArgument String world){
        switch (gameName) {
            case "murder" -> GamePlugin.getManager().loadGame(new MurderMystery(), world);
            case "quiver" -> GamePlugin.getManager().loadGame(new OneInTheQuiver(), world);
        }

    }

    @Subcommand("load")
    public static void loadGame(Player player, @AStringArgument String gameName){
        switch (gameName) {
            case "murder" -> GamePlugin.getManager().loadGame(new MurderMystery(), "headquarters");
            case "quiver" -> GamePlugin.getManager().loadGame(new OneInTheQuiver(), "headquarters");
        }

    }
    @Subcommand("loadArena")
    public static void loadArena(Player player){
        player.sendMessage(Component.text("Arena load begin."));
        GamePlugin.getManager().getWorldManager().loadArenaByName("arena");
        player.sendMessage(Component.text("Arena load finish."));
    }

    @Subcommand("enterArena")
    public static void tpArena(Player player){
        player.sendMessage(Component.text("Sending you to the arena now!"));
        player.teleport(GamePlugin.getManager().getWorldManager().getArena().getSpawnLocation());
    }

    @Subcommand("start")
    public static void startGame(Player player){
        if(!GamePlugin.getManager().isReady()){
            player.sendMessage(F.main("Game", "The game is not loaded or is not ready!"));
            return;
        }
        GamePlugin.getManager().startGame();

    }

    @Subcommand("stop")
    public static void stopGame(Player player){
        GamePlugin.getManager().stopGame();
    }

    @Subcommand("testparse")
    public static void testParse(Player player){
        player.sendMessage(F.main("Game", "Printing parse data for the world..."));
        player.sendMessage(F.main("Game", GamePlugin.getManager().getWorldManager().getDataLocations(player.getWorld()).toString()));
    }

}
