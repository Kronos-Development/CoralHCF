package net.frozenorb.foxtrot.team.permissions.command;

import net.frozenorb.foxtrot.Foxtrot;
import net.frozenorb.foxtrot.team.Team;
import net.frozenorb.foxtrot.team.permissions.menu.PermissionsMenu;
import net.frozenorb.qlib.command.Command;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class TeamPermissionCommand {

    @Command(names={ "team permissions", "t permissions", "f permissions", "faction permissions", "fac permissions", "team perms", "t perms", "f perms", "faction perms", "fac perms" }, permission="")
    public static void perms(Player sender) {
        Team senderTeam = Foxtrot.getInstance().getTeamHandler().getTeam(sender);

        if (senderTeam == null) {
            sender.sendMessage(ChatColor.GRAY + "You are not on a team!");
            return;
        }

        if (!(senderTeam.isOwner(sender.getUniqueId()) || senderTeam.isCaptain(sender.getUniqueId()) || senderTeam.isCoLeader(sender.getUniqueId()))) {
            sender.sendMessage(ChatColor.DARK_AQUA + "Only team captains can do this.");
            return;
        }

        new PermissionsMenu(senderTeam).openMenu(sender);
    }

}
