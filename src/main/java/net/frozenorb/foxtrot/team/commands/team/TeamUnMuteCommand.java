package net.frozenorb.foxtrot.team.commands.team;

import com.google.common.collect.ImmutableMap;

import com.minexd.zoot.util.CC;
import net.frozenorb.foxtrot.Foxtrot;
import net.frozenorb.foxtrot.team.Team;
import net.frozenorb.foxtrot.team.track.TeamActionTracker;
import net.frozenorb.foxtrot.team.track.TeamActionType;
import net.frozenorb.qlib.command.Command;
import net.frozenorb.qlib.command.Param;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

public class TeamUnMuteCommand {

    @Command(names={ "team unmute", "t unmute", "f unmute", "faction unmute", "fac unmute" }, permission="foxtrot.mutefaction")
    public static void teamUnMute(Player sender, @Param(name = "team") Team team) {
        TeamActionTracker.logActionAsync(team, TeamActionType.TEAM_MUTE_EXPIRED, ImmutableMap.of(
                "shadowMute", "false"
        ));

        Iterator<Map.Entry<UUID, String>> mutesIterator = TeamMuteCommand.getTeamMutes().entrySet().iterator();

        while (mutesIterator.hasNext()) {
            Map.Entry<UUID, String> mute = mutesIterator.next();

            if (mute.getValue().equalsIgnoreCase(team.getName())) {
                mutesIterator.remove();
            }
        }

        sender.sendMessage(CC.translate("§eUnmuted the team §9" + team.getName() + "§e."));
    }

}