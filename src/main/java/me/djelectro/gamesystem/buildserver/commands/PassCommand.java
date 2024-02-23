package me.djelectro.gamesystem.buildserver.commands;

import dev.jorel.commandapi.annotations.Command;
import dev.jorel.commandapi.annotations.Default;
import dev.jorel.commandapi.annotations.arguments.AStringArgument;
import me.djelectro.gamesystem.buildserver.BuildServer;
import me.djelectro.gamesystem.core.utils.CommandClass;
import me.djelectro.gamesystem.core.utils.F;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Objects;

@Command("pass")
@CommandClass
public class PassCommand {


    @Default
    public static void pass(CommandSender sender, @AStringArgument String pass) {
        if(!Objects.equals(pass, BuildServer.getRootPlugin().getConfig().getString("buildServerPass"))){
            sender.sendMessage(F.main("Build", "That password was incorrect!"));
        }
        else{
            BuildServer.getManager().allowPlayer((Player) sender);
            sender.sendMessage(F.main("Build", "Welcome to the build server!"));
        }
    }
}
