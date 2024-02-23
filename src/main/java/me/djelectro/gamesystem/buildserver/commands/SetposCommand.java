package me.djelectro.gamesystem.buildserver.commands;


import dev.jorel.commandapi.annotations.Command;
import dev.jorel.commandapi.annotations.Default;
import dev.jorel.commandapi.annotations.arguments.AIntegerArgument;
import me.djelectro.gamesystem.buildserver.BuildServer;
import me.djelectro.gamesystem.core.utils.CommandClass;
import me.djelectro.gamesystem.core.utils.F;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@Command("setpos")
@CommandClass
public class SetposCommand {

    @Default
    public static void setPos(CommandSender sender, @AIntegerArgument int posVal){
        switch(posVal){
            case 1 -> BuildServer.getManager().updatePos1(((Player) sender).getLocation().subtract(0,1,0));
            case 2 -> BuildServer.getManager().updatePos2(((Player) sender).getLocation().subtract(0,1,0));
            default -> {
                sender.sendMessage(F.main("Build", "You must enter 1 or 2 for the position to set!"));
                return;
            }
        }

        sender.sendMessage(F.main("Build", "Position set."));


    }
}
