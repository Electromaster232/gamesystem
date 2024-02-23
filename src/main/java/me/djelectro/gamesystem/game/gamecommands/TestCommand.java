package me.djelectro.gamesystem.game.gamecommands;

import dev.jorel.commandapi.annotations.Command;
import dev.jorel.commandapi.annotations.Default;
import me.djelectro.gamesystem.core.utils.CommandClass;
import org.bukkit.command.CommandSender;

@Command("warp")
@CommandClass
public class TestCommand {


    @Default
    public static void warp(CommandSender sender) {
        sender.sendMessage("--- Warp help ---");
        sender.sendMessage("/warp - Show this help");
        sender.sendMessage("/warp <warp> - Teleport to <warp>");
        sender.sendMessage("/warp create <warpname> - Creates a warp at your current location");
    }
}
