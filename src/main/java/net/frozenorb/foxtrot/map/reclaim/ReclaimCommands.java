package net.frozenorb.foxtrot.map.reclaim;

import net.frozenorb.foxtrot.Foxtrot;
import net.frozenorb.qlib.command.Command;
import net.frozenorb.qlib.command.Param;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class ReclaimCommands {

    @Command(names = {"reclaim"}, permission = "")
    public static void reclaimCommand(Player player) {
        Foxtrot.getInstance().getMapHandler().getReclaimHandler().reclaim(player);
    }

    @Command(names = {"resetreclaim"}, permission = "foxtrot.reclaim.reset", description = "Allow someone to reclaim again.")
    public static void resetReclaimCommand(CommandSender sender, @Param(name = "target")UUID target) {
        ReclaimHandler handler = Foxtrot.getInstance().getMapHandler().getReclaimHandler();

        if (!handler.hasReclaimed(target)) {
            sender.sendMessage(ChatColor.RED + "This player has not reclaimed yet.");
            return;
        }

        handler.resetReclaim(target);
        sender.sendMessage(ChatColor.GREEN + "This player is now able to reclaim again.");
    }

}