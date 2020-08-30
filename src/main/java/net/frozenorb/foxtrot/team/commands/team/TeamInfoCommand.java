package net.frozenorb.foxtrot.team.commands.team;

import com.cheatbreaker.api.CheatBreakerAPI;
import com.cheatbreaker.api.object.CBWaypoint;
import net.frozenorb.foxtrot.Foxtrot;
import net.frozenorb.foxtrot.settings.Setting;
import net.frozenorb.foxtrot.team.RallyPoint;
import net.frozenorb.foxtrot.team.Team;
import net.frozenorb.qlib.command.Command;
import net.frozenorb.qlib.command.Param;
import net.frozenorb.qlib.util.UUIDUtils;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.concurrent.TimeUnit;

public class TeamInfoCommand {

    @Command(names={ "team info", "t info", "f info", "faction info", "fac info", "team who", "t who", "f who", "faction who", "fac who", "team show", "t show", "f show", "faction show", "fac show", "team i", "t i", "f i", "faction i", "fac i" }, permission="")
    public static void teamInfo(Player sender, @Param(name="team", defaultValue="self", tabCompleteFlags={ "noteams", "players" }) Team team) {
        new BukkitRunnable() {

            public void run() {
                Team exactPlayerTeam = Foxtrot.getInstance().getTeamHandler().getTeam(UUIDUtils.uuid(team.getName()));

                if (exactPlayerTeam != null && exactPlayerTeam != team) {
                    exactPlayerTeam.sendTeamInfo(sender);
                }

                team.sendTeamInfo(sender);
                if (team.getMembers().contains(sender.getUniqueId())) return;

                team.getOnlineMembers().forEach(player -> {
                    if(Foxtrot.getInstance().getFDisplayMap().isToggled(player.getUniqueId())) {
                        CBWaypoint cbWaypoint = new CBWaypoint(team.getName(), team.getHQ().getBlockX(), team.getHQ().getBlockY(), team.getHQ().getBlockZ(), team.getHQ().getWorld().getUID().toString(), -16776961, true, true);
                        team.setFactionHQRally(cbWaypoint);
                    } else {
                        return;
                    }
                });
            }

        }.runTaskAsynchronously(Foxtrot.getInstance());

        new BukkitRunnable() {

            public void run() {
                    team.setFactionHQRally(null);
            }
        }.runTaskLater(Foxtrot.getInstance(), 20*60*5);
    }

}