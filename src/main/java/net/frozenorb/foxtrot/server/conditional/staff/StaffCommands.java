package net.frozenorb.foxtrot.server.conditional.staff;

import net.frozenorb.qlib.command.Command;
import net.frozenorb.qlib.command.Param;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class StaffCommands {

    @Command(names = {"amivis", "vis?", "v?"}, permission = "foxtrot.modmode")
    public static void amivisCommand(Player sender) {
        boolean modMode = ModHandler.isModMode(sender);
        boolean vanished = ModHandler.isVanished(sender);

        sender.sendMessage(ChatColor.GOLD + "You are " + (modMode ? (ChatColor.GREEN + "in") : (ChatColor.RED + "not in")) + ChatColor.GOLD + " Mod Mode, and are " + (vanished ? (ChatColor.GREEN + "INVISIBLE") : (ChatColor.RED + "VISIBLE")) + ChatColor.GOLD + ".");
    }

    @Command(names = {"mm", "modmode", "staff", "v", "h"}, permission = "foxtrot.modmode")
    public static void modModeCommand(Player sender, @Param(name = "player", defaultValue = "self") Player target) {
        if (sender != target && !sender.hasPermission("foxtrot.modmode.others")) {
            sender.sendMessage(ChatColor.RED + "You don't have permission to modmode others.");
            return;
        }

        boolean newState = ModHandler.toggleModMode(target);

        if (sender != target)
            sender.sendMessage(ChatColor.YELLOW + "Mod Mode has been " + (newState ? (ChatColor.GREEN + "enabled") : (ChatColor.RED + "disabled")) + ChatColor.YELLOW + " for " + target.getDisplayName());
    }

}