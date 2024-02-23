package me.djelectro.gamesystem.core.utils;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;

public class F {

    public static TextComponent main(String module, String text){
        return Component.text(module + "> ").color(NamedTextColor.GREEN).append(Component.text(text).color(NamedTextColor.WHITE));
    }
}
