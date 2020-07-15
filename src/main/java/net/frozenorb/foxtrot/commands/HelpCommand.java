package net.frozenorb.foxtrot.commands;

import net.frozenorb.foxtrot.Foxtrot;
import net.frozenorb.qlib.command.Command;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class HelpCommand {

    @Command(names={ "Help" }, permission="")
    public static void help(Player sender) {

        String networkName = ChatColor.translateAlternateColorCodes('&', Foxtrot.getInstance().getServerHandler().getNetworkName());
        String serverName = Foxtrot.getInstance().getServerHandler().getServerName();
        String serverWebsite = Foxtrot.getInstance().getServerHandler().getNetworkWebsite();

        String sectionColor = ChatColor.translateAlternateColorCodes('&', Foxtrot.getInstance().getServerHandler().getTabSectionColor());
        String infoColor = ChatColor.translateAlternateColorCodes('&', Foxtrot.getInstance().getServerHandler().getTabInfoColor());

        if (sectionColor.equalsIgnoreCase(infoColor))
            infoColor = "§r";

        sender.sendMessage(new String[] {

                "§7§m-----------------------------------------------------",
                networkName + " §7❘ §r" + serverName,
                "",

                sectionColor + "Map Kit: " + infoColor + Foxtrot.getInstance().getServerHandler().getEnchants(),
                sectionColor + "Factions: " + infoColor + Foxtrot.getInstance().getMapHandler().getTeamSize() + " §7(" + Foxtrot.getInstance().getMapHandler().getAllyLimit() + " allies)",
                "",

                sectionColor + "Helpful Commands:",
                infoColor + "/report <player> <reason> §7- " + infoColor + "Report Players",
                infoColor + "/request <message> §7- " + infoColor + "Request Staff Assistance",
                infoColor + "/tgc §7- " + infoColor + "Toggle Chat Visibility",
                "",

                sectionColor + "TeamSpeak §7- " + infoColor + "ts." + serverWebsite,
                sectionColor + "Rules §7- " + infoColor + "www." + serverWebsite + "/rules",
                sectionColor + "Store §7- " + infoColor + "www." + serverWebsite + "/store",
                "§7§m-----------------------------------------------------",

        });
    }

}
