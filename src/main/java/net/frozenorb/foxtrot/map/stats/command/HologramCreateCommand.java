package net.frozenorb.foxtrot.map.stats.command;

import net.frozenorb.foxtrot.Foxtrot;
import net.frozenorb.foxtrot.map.stats.StatsHandler;
import net.frozenorb.qlib.command.Command;
import net.frozenorb.qlib.command.Param;
import org.bukkit.entity.Player;

public class HologramCreateCommand {

    @Command(names = {"hololeaderboard create", "hololbcreate"}, hidden = true, permission = "op")
    public static void onHoloCreate(Player player, @Param(name = "objective")String objectiveName) {
        StatsTopCommand.StatsObjective objective;
        try {
            objective = StatsTopCommand.StatsObjective.valueOf(objectiveName);
        } catch (Exception ex) {
            objective = StatsTopCommand.StatsObjective.valueOf(objectiveName);
            return;
        }

        StatsHandler statsHandler = Foxtrot.getInstance().getMapHandler().getStatsHandler();

        statsHandler.setupHologram(player.getLocation(), objective);

    }
}
