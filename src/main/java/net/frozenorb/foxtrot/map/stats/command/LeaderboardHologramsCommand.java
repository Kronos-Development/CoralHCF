package net.frozenorb.foxtrot.map.stats.command;

import com.google.common.collect.Maps;
import com.minexd.zoot.util.CC;
import lombok.Getter;
import net.frozenorb.foxtrot.Foxtrot;
import net.frozenorb.foxtrot.map.stats.StatsHandler;
import net.frozenorb.qlib.command.Command;
import net.frozenorb.qlib.command.Param;
import net.frozenorb.qlib.qLib;
import net.frozenorb.qlib.serialization.LocationSerializer;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class LeaderboardHologramsCommand {

    @Getter private Map<Location, StatsTopCommand.StatsObjective> leaderboardHolos = Maps.newHashMap();

    @Command(names = {"leaderboard add holo"}, hidden = true, permission = "op")
    public static void onHoloCreate(Player player, @Param(name = "objective")String objectiveName ) {
        StatsTopCommand.StatsObjective objective;
        try {
            objective = StatsTopCommand.StatsObjective.valueOf(objectiveName);
        } catch (Exception ex) {
            player.sendMessage(CC.translate("&cWrong thing headass"));
            return;
        }

        StatsHandler statsHandler = Foxtrot.getInstance().getMapHandler().getStatsHandler();

        statsHandler.setupHologram(player.getLocation(), objective);
    }

    @Command(names = "leaderboard del holos", permission = "op")
    public static void onDelete(Player player) {
        Foxtrot.getInstance().getMapHandler().getStatsHandler().delete();
    }
}
