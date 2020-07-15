package net.frozenorb.foxtrot.commands;

import net.frozenorb.qlib.command.Command;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class PluginDisabledClass {

    @Command(names = {"pl", "plugins", "ver", "about"}, permission = "op")
    public static void disablecommands(Player sender){
        sender.sendMessage(ChatColor.RED + "No permission.");
    }
}
