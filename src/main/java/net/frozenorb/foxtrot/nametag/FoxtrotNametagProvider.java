package net.frozenorb.foxtrot.nametag;

import net.frozenorb.foxtrot.Foxtrot;
import net.frozenorb.foxtrot.pvpclasses.pvpclasses.ArcherClass;
import net.frozenorb.foxtrot.server.conditional.staff.ModHandler;
import net.frozenorb.foxtrot.team.Team;
import net.frozenorb.qlib.nametag.NametagInfo;
import net.frozenorb.qlib.nametag.NametagProvider;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class FoxtrotNametagProvider extends NametagProvider {

    public FoxtrotNametagProvider() {
        super("Foxtrot Provider", 5);
    }

    @Override
    public NametagInfo fetchNametag(Player toRefresh, Player refreshFor) {
        if (ModHandler.isModMode(toRefresh) && Foxtrot.getInstance().getServerHandler().getConditionalHandler().isStaffHandler())
            return createNametag(toRefresh, ChatColor.AQUA.toString(), "");

        Team viewerTeam = Foxtrot.getInstance().getTeamHandler().getTeam(refreshFor);
        NametagInfo nametagInfo = null;

        if (viewerTeam != null) {
            if (viewerTeam.isMember(toRefresh.getUniqueId())) {
                nametagInfo = createNametag(toRefresh, Foxtrot.getInstance().getTeamColorMap().getColor(refreshFor.getUniqueId()).toString(), "");
            } else if (viewerTeam.isAlly(toRefresh.getUniqueId())) {
                nametagInfo = createNametag(toRefresh, Foxtrot.getInstance().getAllyColorMap().getColor(refreshFor.getUniqueId()).toString(), "");
            }
        }

        // If we already found something above they override these, otherwise we can do these checks.
        if (nametagInfo == null) {
            if (ArcherClass.getMarkedPlayers().containsKey(toRefresh.getName()) && ArcherClass.getMarkedPlayers().get(toRefresh.getName()) > System.currentTimeMillis()) {
                nametagInfo = createNametag(toRefresh, Foxtrot.getInstance().getArcherTagColorMap().getColor(refreshFor.getUniqueId()).toString(), "");
            } else if (viewerTeam != null && viewerTeam.getFocused() != null && viewerTeam.getFocused().equals(toRefresh.getUniqueId())) {
                nametagInfo = createNametag(toRefresh, Foxtrot.getInstance().getFocusColorMap().getColor(refreshFor.getUniqueId()).toString(), "");
            }
        }

        // You always see yourself as green.
        if (refreshFor == toRefresh) {
            nametagInfo = createNametag(toRefresh, Foxtrot.getInstance().getTeamColorMap().getColor(refreshFor.getUniqueId()).toString(), "");
        }

        // If nothing custom was set, fall back on yellow.
        return (nametagInfo == null ? createNametag(toRefresh, Foxtrot.getInstance().getEnemyColorMap().getColor(refreshFor.getUniqueId()).toString(), "") : nametagInfo);
    }

    private NametagInfo createNametag(Player displayed, String prefix, String suffix) {
        return createNametag(prefix, suffix);
    }

}