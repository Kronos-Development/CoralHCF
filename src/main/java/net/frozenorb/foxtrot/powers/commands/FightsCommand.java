package net.frozenorb.foxtrot.powers.commands;

import net.frozenorb.foxtrot.Foxtrot;
import net.frozenorb.foxtrot.powers.PowersHandler;
import net.frozenorb.qlib.command.Command;
import net.frozenorb.qlib.command.Param;
import org.bukkit.entity.Player;

public class FightsCommand {

    @Command(names = "fighting?", permission = "foxtrot.powers")
    public static void onFights(Player player, @Param(name = "factionName") String team) {
        PowersHandler powersHandler = Foxtrot.getInstance().getPowersHandler();
        player.sendMessage(powersHandler.isInFight(team));

    }
}
