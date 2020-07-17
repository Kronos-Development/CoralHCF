package net.frozenorb.foxtrot.team.commands.team;

import com.cheatbreaker.api.object.CBWaypoint;
import net.frozenorb.foxtrot.Foxtrot;
import net.frozenorb.foxtrot.team.RallyPoint;
import net.frozenorb.foxtrot.team.Team;
import net.frozenorb.qlib.command.Command;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.concurrent.TimeUnit;

public class TeamRallyCommand {

    @Command(names={ "team rally", "t rally", "f rally", "faction rally", "fac rally", "team setrally", "t setrally", "f setrally", "faction setrally", "fac setrally" }, permission="")
    public static void rally(Player sender) {
        Team senderTeam = Foxtrot.getInstance().getTeamHandler().getTeam(sender);
        if (senderTeam == null) {
            sender.sendMessage(ChatColor.GRAY + "You are not on a team!");
            return;
        }

        if(!senderTeam.hasRallyPermission(sender.getUniqueId())) {
            sender.sendMessage(ChatColor.DARK_AQUA + "Only members with /f perms can do this.");
            return;
        }

        CBWaypoint cbWaypoint = new CBWaypoint("Rally", sender.getLocation().getBlockX(), sender.getLocation().getBlockY(), sender.getLocation().getBlockZ(), sender.getLocation().getWorld().getUID().toString(), -16776961, true, true);
        RallyPoint rallyPoint = new RallyPoint(sender.getLocation(), cbWaypoint, System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(10));

        senderTeam.setRally(rallyPoint);
        senderTeam.sendMessage(ChatColor.DARK_AQUA + sender.getName() + " has updated the team's rally point! This will last for 10 minutes.");

    }

    @Command(names={ "team wiperally", "t wiperally", "f wiperally", "faction wiperally", "fac wiperally" }, permission="")
    public static void wiperally(Player sender) {
        Team senderTeam = Foxtrot.getInstance().getTeamHandler().getTeam(sender);
        if (senderTeam == null) {
            sender.sendMessage(ChatColor.GRAY + "You are not on a team!");
            return;
        }

        if(!senderTeam.hasRallyPermission(sender.getUniqueId())) {
            sender.sendMessage(ChatColor.DARK_AQUA + "Only members with /f perms can do this.");
            return;
        }

        RallyPoint rallyPoint = senderTeam.getRally();
        if(rallyPoint == null) {
            sender.sendMessage(ChatColor.GRAY + "Your team does not have a rally set.");
            return;
        }

        senderTeam.setRally(null);
        senderTeam.sendMessage(ChatColor.DARK_AQUA + sender.getName() + " has wiped the team's rally.");

    }

}
