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
            return createNametag(toRefresh, ChatColor.GRAY.toString(), "");

        Team viewerTeam = Foxtrot.getInstance().getTeamHandler().getTeam(refreshFor);
        NametagInfo nametagInfo = null;

        if (viewerTeam != null) {
            if (viewerTeam.isMember(toRefresh.getUniqueId())) {
                nametagInfo = createNametag(toRefresh, ChatColor.DARK_GREEN.toString(), "");
            } else if (viewerTeam.isAlly(toRefresh.getUniqueId())) {
                nametagInfo = createNametag(toRefresh, Team.ALLY_COLOR.toString(), "");
            }
        }

        // If we already found something above they override these, otherwise we can do these checks.
        if (nametagInfo == null) {
            if (ArcherClass.getMarkedPlayers().containsKey(toRefresh.getName()) && ArcherClass.getMarkedPlayers().get(toRefresh.getName()) > System.currentTimeMillis()) {
                nametagInfo = createNametag(toRefresh, Foxtrot.getInstance().getServerHandler().getArcherTagColor().toString(), "");
            } else if (viewerTeam != null && viewerTeam.getFocused() != null && viewerTeam.getFocused().equals(toRefresh.getUniqueId())) {
                nametagInfo = createNametag(toRefresh, ChatColor.LIGHT_PURPLE.toString(), "");
            }
        }

        // You always see yourself as green.
        if (refreshFor == toRefresh) {
            nametagInfo = createNametag(toRefresh, ChatColor.DARK_GREEN.toString(), "");
        }

        // If nothing custom was set, fall back on yellow.
        return (nametagInfo == null ? createNametag(toRefresh, Foxtrot.getInstance().getServerHandler().getDefaultRelationColor().toString(), "") : nametagInfo);
    }

    private NametagInfo createNametag(Player displayed, String prefix, String suffix) {
        return createNametag(prefix, suffix);

        /*Map<Integer, UUID> placesMap = Foxtrot.getInstance().getMapHandler().getStatsHandler() != null ? Foxtrot.getInstance().getMapHandler().getStatsHandler().getTopKills() : null;
        if (placesMap == null) {
            return createNametag(prefix, suffix);
        }
        
        Integer place = placesMap.size() == 3 ? placesMap.get(1).equals(displayed.getUniqueId()) ? 1 : placesMap.get(2).equals(displayed.getUniqueId()) ? 2 : placesMap.get(3).equals(displayed.getUniqueId()) ? 3 : 99 : 99;
        if (place == 99) {
            return createNametag(prefix, suffix);
        }
        
        String coloredPrefix = ChatColor.translateAlternateColorCodes('&', place == 1 ? "&8[&6#1&8] " : place == 2 ? "&8[&7#2&8] " : "&8[&f#3&8] ");
        
        return createNametag(coloredPrefix + prefix, suffix);*/
    }

}