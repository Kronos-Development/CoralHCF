package net.frozenorb.foxtrot.commands;

import com.minexd.zoot.util.CC;
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
        player.sendMessage(CC.translate(player.hasMetadata("nostaff") ? "&aYou can see staff" : "&cYou cannot see staff"));
    }
}
