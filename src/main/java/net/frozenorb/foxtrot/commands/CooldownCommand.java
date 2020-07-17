package net.frozenorb.foxtrot.commands;

import net.frozenorb.foxtrot.server.EnderpearlCooldownHandler;
import net.frozenorb.foxtrot.server.SpawnTagHandler;
import net.frozenorb.qlib.command.Command;
import net.frozenorb.qlib.command.Param;
import net.minecraft.util.org.apache.commons.lang3.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.concurrent.TimeUnit;

/**
 * Created by vape on 6/7/2020 at 12:17 PM.
 */
public class CooldownCommand {

    @Command(names = {"cooldown", "timer"}, permission = "foxtrot.cooldown")
    public static void cooldownHelp(CommandSender sender) {
        sender.sendMessage(ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH + StringUtils.repeat('-', 35));
        sender.sendMessage(ChatColor.DARK_AQUA.toString() + ChatColor.BOLD + "Cooldown");
        sender.sendMessage("");
        sender.sendMessage(ChatColor.DARK_AQUA + "Types:");
        sender.sendMessage("ENDERPEARL, COMBAT");
        sender.sendMessage("");
        sender.sendMessage(ChatColor.AQUA + "/cooldown set <player> <type> <seconds>");
        sender.sendMessage(ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH + StringUtils.repeat('-', 35));
    }

    @Command(names = {"cooldown set", "timer set"}, permission = "foxtrot.cooldown.set")
    public static void cooldownSet(CommandSender sender, @Param(name = "player", defaultValue = "self") Player target, @Param(name = "type") String type, @Param(name = "seconds") int seconds) {
        switch (type.toUpperCase().replace("_"," ")) {
            case "ENDERPEARL": {
                if (seconds <= 0)
                    EnderpearlCooldownHandler.getEnderpearlCooldown().remove(target.getName());
                else
                    EnderpearlCooldownHandler.getEnderpearlCooldown().put(target.getName(), System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(seconds));

                sender.sendMessage(ChatColor.YELLOW + "Set" + ChatColor.BLUE + " Enderpearl " + ChatColor.YELLOW + "cooldown of " + target.getDisplayName() + ChatColor.YELLOW + " to " + ChatColor.GOLD + seconds + " second" + (seconds > 1 ? "s" : ""));
                break;
            }

            case "COMBAT": {
                if (seconds <= 0)
                    SpawnTagHandler.removeTag(target);
                else
                    SpawnTagHandler.addOffensiveSeconds(target, seconds);

                sender.sendMessage(ChatColor.YELLOW + "Set" + ChatColor.DARK_RED + " Combat " + ChatColor.YELLOW + "cooldown of " + target.getDisplayName() + ChatColor.YELLOW + " to " + ChatColor.GOLD + seconds + " second" + (seconds > 1 ? "s" : ""));
                break;
            }
        }
    }

}