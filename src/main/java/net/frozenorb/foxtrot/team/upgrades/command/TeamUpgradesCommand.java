package net.frozenorb.foxtrot.team.upgrades.command;

import com.minexd.zoot.util.CC;
import net.frozenorb.foxtrot.Foxtrot;
import net.frozenorb.foxtrot.team.Team;
import net.frozenorb.foxtrot.team.upgrades.menu.TeamUpgradesMenu;
import net.frozenorb.qlib.command.Command;
import org.bukkit.entity.Player;

public class TeamUpgradesCommand {

    @Command(names = {"f upgrades", "t upgrades", "faction upgrades", "team upgrades"}, permission = "")
    public static void onCommand(Player player) {
        Team team = Foxtrot.getInstance().getTeamHandler().getTeam(player.getUniqueId());
        if (team == null) {
            player.sendMessage(CC.translate("&cYou must be on a team to perform this command."));
        }

        if(team.getCaptains().contains(player.getUniqueId())) {
            player.sendMessage(CC.translate("&cYou must be a captain to perform this command"));
            return;
        }

        new TeamUpgradesMenu(team).openMenu(player);


    }
}
