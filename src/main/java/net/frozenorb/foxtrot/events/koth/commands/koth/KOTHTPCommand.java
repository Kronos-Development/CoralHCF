package net.frozenorb.foxtrot.events.koth.commands.koth;

import net.frozenorb.foxtrot.Foxtrot;
import net.frozenorb.foxtrot.events.Event;
import net.frozenorb.foxtrot.events.dtc.DTC;
import net.frozenorb.foxtrot.events.koth.KOTH;
import net.frozenorb.qlib.command.Command;
import net.frozenorb.qlib.command.Param;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class KOTHTPCommand {

    @Command(names={ "KOTH TP", "KOTHTP", "events tp", "event tp" }, permission="foxtrot.koth")
    public static void kothTP(Player sender, @Param(name="koth", defaultValue="active") Event event) {

        switch (event.getType()) {
            case KOTH: {
                sender.teleport(((KOTH) event).getCapLocation().toLocation(Foxtrot.getInstance().getServer().getWorld(((KOTH) event).getWorld())));
                sender.sendMessage(ChatColor.GRAY + "Teleported to the " + event.getName() + " KOTH.");
                break;
            }
            case DTC: {
                sender.teleport(((DTC) event).getCapLocation().toLocation(Foxtrot.getInstance().getServer().getWorld(((DTC) event).getWorld())));
                sender.sendMessage(ChatColor.GRAY + "Teleported to the " + event.getName() + " DTC.");
                break;
            }
            default: {
                sender.sendMessage(ChatColor.RED + "You can't TP to an event that doesn't have a location.");
            }
        }
    }

}