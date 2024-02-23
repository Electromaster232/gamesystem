package me.djelectro.gamesystem.buildserver.commands;

import dev.jorel.commandapi.annotations.Command;
import dev.jorel.commandapi.annotations.Default;
import me.djelectro.gamesystem.buildserver.BuildServer;
import me.djelectro.gamesystem.core.utils.CommandClass;
import me.djelectro.gamesystem.core.utils.F;
import org.bukkit.command.CommandSender;

@Command("parse")
@CommandClass
public class ParseCommand {

    @Default
    public static void parseCommand(CommandSender sender){
        if(!BuildServer.getManager().isParseReady()){
            sender.sendMessage(F.main("Build", "You have not set both corners for parsing. Please use the /setpos command!"));
            return;
        }
        sender.sendMessage(F.main("Build", "Starting world parse..."));
        BuildServer.getManager().parseWorld();
        sender.sendMessage(F.main("Build", "Parse completed."));
    }
}
