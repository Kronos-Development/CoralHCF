package net.frozenorb.foxtrot.team.commands.team;

import com.google.common.collect.ImmutableMap;

import com.minexd.zoot.util.CC;
import lombok.Getter;
import net.frozenorb.foxtrot.Foxtrot;
import net.frozenorb.foxtrot.team.Team;
import net.frozenorb.foxtrot.team.track.TeamActionTracker;
import net.frozenorb.foxtrot.team.track.TeamActionType;
import net.frozenorb.qlib.command.Command;
import net.frozenorb.qlib.command.Param;
import net.frozenorb.qlib.util.TimeUtils;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

public class TeamShadowMuteCommand {

    @Getter public static Map<UUID, String> teamShadowMutes = new HashMap<>();

    @Command(names={ "team shadowmute", "t shadowmute", "f shadowmute", "faction shadowmute", "fac shadowmute" }, permission="foxtrot.mutefaction")
    public static void teamShadowMute(Player sender, @Param(name = "team") Team team, @Param(name = "time") int time) {
        int timeSeconds = time * 60;

        for (UUID player : team.getMembers()) {
            teamShadowMutes.put(player, team.getName());
        }

        TeamActionTracker.logActionAsync(team, TeamActionType.TEAM_MUTE_CREATED, ImmutableMap.of(
                "shadowMute", "true",
                "mutedById", sender.getUniqueId(),
                "mutedByName", sender.getName(),
                "duration", time
        ));

        new BukkitRunnable() {

            public void run() {
                TeamActionTracker.logActionAsync(team, TeamActionType.TEAM_MUTE_EXPIRED, ImmutableMap.of(
                        "shadowMute", "true"
                ));

                Iterator<java.util.Map.Entry<UUID, String>> mutesIterator = teamShadowMutes.entrySet().iterator();

                while (mutesIterator.hasNext()) {
                    java.util.Map.Entry<UUID, String> mute = mutesIterator.next();

                    if (mute.getValue().equalsIgnoreCase(team.getName())) {
                        mutesIterator.remove();
                    }
                }
            }

        }.runTaskLater(Foxtrot.getInstance(), timeSeconds * 20L);

        sender.sendMessage(CC.translate("§eShadow muted the team §9" + team.getName() +" §efor §7" + TimeUtils.formatIntoMMSS(timeSeconds) + "§e."));
    }

}