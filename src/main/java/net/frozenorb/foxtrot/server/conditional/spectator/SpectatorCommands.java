package net.frozenorb.foxtrot.server.conditional.spectator;

import net.frozenorb.foxtrot.server.conditional.spectator.menus.SpectatorMenu;
import net.frozenorb.qlib.command.Command;
import org.bukkit.entity.Player;

public class SpectatorCommands {

    @Command(names = "spec", permission = "")
    public static void spec(Player player) {
        new SpectatorMenu().openMenu(player);

    }
}
