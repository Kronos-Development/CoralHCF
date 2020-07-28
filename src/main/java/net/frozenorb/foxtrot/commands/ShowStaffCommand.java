package net.frozenorb.foxtrot.commands;

import net.frozenorb.foxtrot.Foxtrot;
import net.frozenorb.qlib.command.Command;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;

public class ShowStaffCommand {

    @Command(names = "showstaff", permission = "foxtrot.showstaff")
    public static void onCommand(Player player) {
        if (player.hasMetadata("nostaff")) {
            player.removeMetadata("nostaff", Foxtrot.getInstance());
        } else {
            player.setMetadata("nostaff", new FixedMetadataValue(Foxtrot.getInstance(), true));
        }
    }
}
