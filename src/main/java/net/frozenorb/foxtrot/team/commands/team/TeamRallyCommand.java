package net.frozenorb.foxtrot.team.commands.team;

import net.frozenorb.foxtrot.Foxtrot;
import net.frozenorb.foxtrot.server.cheatbreaker.WrappedWaypoint;
import net.frozenorb.foxtrot.team.Rally;
import net.frozenorb.foxtrot.team.Team;
import net.frozenorb.qlib.command.Command;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

/**
 * Created by vape on 6/7/2020 at 10:19 AM.
 */
public class TeamRallyCommand {
    @Command(names = {"team rally", "t rally", "f rally", "faction rally", "fac rally", "team rally"}, permission = "")
    public static void rally(final Player sender) {
        if (Foxtrot.getInstance().getDeathbanMap().isDeathbanned(sender.getUniqueId())) {
            sender.sendMessage(ChatColor.RED + "You can't do this while you are deathbanned.");
            return;
        }

        Team team = Foxtrot.getInstance().getTeamHandler().getTeam(sender);

        if (team == null) {
            sender.sendMessage(ChatColor.GRAY + "You are not on a team!");
            return;
        }

        if (!team.isOwner(sender.getUniqueId()) && !team.isCoLeader(sender.getUniqueId())) {
            sender.sendMessage(ChatColor.DARK_AQUA + "Only team co-leaders (and above) can do this.");
            return;
        }

        WrappedWaypoint waypoint = new WrappedWaypoint("Rally", sender.getLocation(), sender.getLocation().getWorld().getUID().toString(), -16776961);
        Rally rally = new Rally(sender.getLocation(), waypoint, System.currentTimeMillis());

        team.setRally(rally);
        team.updateWaypoints();
        team.sendMessage(ChatColor.DARK_AQUA + sender.getName() + " has updated the team rally point!");

        rally.startExpireTask(() -> {
            team.setRally(null);
            team.updateWaypoints();
        });
    }
}