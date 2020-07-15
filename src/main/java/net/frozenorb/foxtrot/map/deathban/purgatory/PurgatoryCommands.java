package net.frozenorb.foxtrot.map.deathban.purgatory;

import net.frozenorb.foxtrot.Foxtrot;
import net.frozenorb.qlib.command.Command;
import net.frozenorb.qlib.util.ItemUtils;
import net.minecraft.util.org.apache.commons.lang3.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

public class PurgatoryCommands {

    @Command(names = {"purgatory"}, permission = "")
    public static void purgatoryCommand(Player player) {
        if (!Foxtrot.getInstance().getMapHandler().isPurgatory()) {
            player.sendMessage("§cThis command can only be used if Purgatory is enabled.");
            return;
        }

        if (!Foxtrot.getInstance().getDeathbanMap().isDeathbanned(player.getUniqueId())) {
            player.sendMessage(ChatColor.RED + "You are not deathbanned!");
            return;
        }

        player.sendMessage(ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH + StringUtils.repeat('-', 35));
        player.sendMessage(ChatColor.DARK_RED.toString() + ChatColor.BOLD + "Purgatory Price Map");
        player.sendMessage("");
        for (Map.Entry<Material, Integer> entry : Foxtrot.getInstance().getMapHandler().getPurgatoryHandler().getPriceMap().entrySet()) {
            player.sendMessage(ChatColor.RED + ItemUtils.getName(new ItemStack(entry.getKey())) + ChatColor.YELLOW + " is worth " + ChatColor.RED + entry.getValue() + " second" + (entry.getValue() > 1 ? "s" : ""));
        }
        player.sendMessage(ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH + StringUtils.repeat('-', 35));
    }

    @Command(names = {"setpurgatoryspawn"}, permission = "op")
    public static void setPurgatorySpawn(Player player) {
        if (!Foxtrot.getInstance().getMapHandler().isPurgatory()) {
            player.sendMessage("§cThis command can only be used if Purgatory is enabled.");
            return;
        }

        Foxtrot.getInstance().getMapHandler().getPurgatoryHandler().setPurgatoryLocation(player.getLocation());
        Foxtrot.getInstance().getMapHandler().getPurgatoryHandler().save();

        player.sendMessage(ChatColor.GREEN + "Purgatory spawn updated.");
    }

}