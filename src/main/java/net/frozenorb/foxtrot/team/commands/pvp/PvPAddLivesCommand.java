package net.frozenorb.foxtrot.team.commands.pvp;

import net.frozenorb.foxtrot.Foxtrot;
import net.frozenorb.qlib.command.Command;
import net.frozenorb.qlib.command.Param;
import net.frozenorb.qlib.util.UUIDUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class PvPAddLivesCommand {

    @Command(names = {"pvp addlives", "addlives"}, permission = "foxtrot.addlives")
    public static void pvpSetLives(CommandSender sender, @Param(name = "player") UUID player, @Param(name = "life type") String lifeType, @Param(name = "amount") int amount) {
        lifeType = lifeType.toLowerCase();

        if (!lifeType.equalsIgnoreCase("soulbound")
            && !lifeType.equalsIgnoreCase("friend")) {
            sender.sendMessage(ChatColor.RED + "Invalid life type '" + lifeType + "'. Valid types: soulbound, friend");
            return;
        }

        Player target = Bukkit.getPlayer(player);

        switch (lifeType) {
            case "soulbound": {
                Foxtrot.getInstance().getSoulboundLivesMap().addLives(player, amount);
                break;
            }

            case "friend": {
                Foxtrot.getInstance().getFriendLivesMap().addLives(player, amount);
                break;
            }
        }

        sender.sendMessage(ChatColor.GREEN + "Gave " + ChatColor.YELLOW + UUIDUtils.name(player) + ChatColor.GREEN + " " + amount + " " + lifeType.toLowerCase() + " lives.");

        String suffix = sender instanceof Player ? " from " + sender.getName() : "";
        if (target != null)
            target.sendMessage(ChatColor.GREEN + "You have received " + amount + " " + lifeType.toLowerCase() + " lives" + suffix);
    }

}
