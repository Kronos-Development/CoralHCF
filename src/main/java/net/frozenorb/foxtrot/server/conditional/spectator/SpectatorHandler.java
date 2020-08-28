package net.frozenorb.foxtrot.server.conditional.spectator;

import net.frozenorb.foxtrot.Foxtrot;
import net.frozenorb.foxtrot.events.Event;
import net.frozenorb.foxtrot.events.EventType;
import net.frozenorb.foxtrot.events.koth.KOTH;
import net.frozenorb.foxtrot.server.conditional.spectator.menus.FactionMenu;
import net.frozenorb.foxtrot.server.conditional.spectator.menus.FightsMenu;
import net.frozenorb.foxtrot.team.Team;
import org.bukkit.entity.Player;
import org.junit.internal.runners.statements.FailOnTimeout;

import javax.swing.plaf.PanelUI;

import static net.frozenorb.foxtrot.server.conditional.spectator.SpectatorIntent.*;

public class SpectatorHandler {

    public void SpectatorAdd(Player player, SpectatorIntent intent) {
            if (intent == SPECTATE_KOTH) {
                for (Event event : Foxtrot.getInstance().getEventHandler().getEvents()) {
                    if (event.isActive()) {
                        toggleSpectator(player);
                        if (event.getType() == EventType.KOTH) {
                            player.teleport(((KOTH) event).getCapLocation().toLocation(Foxtrot.getInstance().getServer().getWorld(((KOTH) event).getWorld())));
                        }
                    }

                }
            }

        if (intent == SPECTATE_TEAMFIGHT) {
                new FightsMenu().openMenu(player);
        }

         if (intent == SPECTATE_FACTION) {
             if (Foxtrot.getInstance().getTeamHandler().getTeams().size() > 0) {
                 new FactionMenu().openMenu(player);
             }
         }

        if (intent == GENERAL_SPECTATE) {
                toggleSpectator(player);
        }

    }

    public void toggleSpectator(Player player) {
        boolean mode = !isSpec(player);


    }

    public static boolean isSpec(Player player) {
        return false;
    }

}
