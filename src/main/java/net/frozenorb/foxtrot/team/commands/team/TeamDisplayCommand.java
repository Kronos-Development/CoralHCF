package net.frozenorb.foxtrot.team.commands.team;

import net.frozenorb.foxtrot.Foxtrot;
import net.frozenorb.foxtrot.server.cheatbreaker.CBAPIHook;
import net.frozenorb.foxtrot.server.cheatbreaker.WrappedWaypoint;
import net.frozenorb.foxtrot.team.Team;
import net.frozenorb.qlib.command.Command;
import net.frozenorb.qlib.command.Param;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class TeamDisplayCommand {

    @Command(names={ "team display", "t display", "f display", "faction display", "fac display" }, permission="")
    public static void teamDisplay(Player sender, @Param(name="team") Team team) {
        if (Foxtrot.getInstance().getDeathbanMap().isDeathbanned(sender.getUniqueId())) {
            sender.sendMessage(ChatColor.RED + "You can't do this while you are deathbanned.");
            return;
        }

        Team senderTeam = Foxtrot.getInstance().getTeamHandler().getTeam(sender);

        if (senderTeam == null) {
            sender.sendMessage(ChatColor.GRAY + "You are not on a team!");
            return;
        }

        if (!senderTeam.isOwner(sender.getUniqueId()) && !senderTeam.isCoLeader(sender.getUniqueId())) {
            sender.sendMessage(ChatColor.DARK_AQUA + "Only team co-leaders (and above) can do this.");
            return;
        }

        if (senderTeam.equals(team)) {
            sender.sendMessage(ChatColor.YELLOW + "You cannot display your own team!");
            return;
        }

        if (team.getHQ() == null) {
            sender.sendMessage(ChatColor.YELLOW + "This team does not have a HQ set.");
            return;
        }

        WrappedWaypoint waypoint = new WrappedWaypoint(team.getName(), team.getHQ(), team.getHQ().getWorld().getUID().toString(), -16776961);
        for (Player member : senderTeam.getOnlineMembers()) {
            CBAPIHook.sendWaypoint(member, waypoint);
        }

        senderTeam.sendMessage(ChatColor.DARK_AQUA + sender.getName() + " has created a waypoint for " + team.getName() + "'s HQ!");
    }

}