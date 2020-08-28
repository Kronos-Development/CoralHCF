package net.frozenorb.foxtrot.server.conditional.spectator.menus;

import com.minexd.zoot.ZootAPI;
import com.minexd.zoot.profile.Profile;
import com.minexd.zoot.util.CC;
import mkremins.fanciful.FancyMessage;
import net.frozenorb.foxtrot.Foxtrot;
import net.frozenorb.foxtrot.server.conditional.spectator.SpectatorIntent;
import net.frozenorb.foxtrot.team.Team;
import net.frozenorb.qlib.menu.Button;
import net.frozenorb.qlib.menu.Menu;
import net.frozenorb.qlib.qLib;
import net.frozenorb.qlib.util.UUIDUtils;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

import java.util.*;

public class FactionMenu extends Menu {

    @Override
    public String getTitle(Player player) {
        return CC.translate("&bFaction Spectate Menu");
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();
        int x = 0;

        for (Team team : Foxtrot.getInstance().getTeamHandler().getTeams()) {
            if (team.getOnlineMembers().size() > 0) { // we only account for online teams
                buttons.put(x++, new Button() {
                    @Override
                    public String getName(Player player) {
                        return ChatColor.DARK_AQUA + team.getName();
                    }

                    @Override
                    public void clicked(Player player, int slot, ClickType clickType) {
                        Foxtrot.getInstance().getSpectatorHandler().toggleSpectator(player);
                        player.teleport(team.getOnlineMembers().stream().findFirst().get().getLocation());
                    }

                    @Override
                    public List<String> getDescription(Player player) {
                        List<String> lore = new ArrayList<>();

                        String lol = team.getOnlineMembers().toString().replace("CraftPlayer{name=", "").replace("}", "").replace("[", "").replace("]", "");

                        lore.add(CC.MENU_BAR);
                        lore.add(CC.translate(Foxtrot.getInstance().getConfig().getBoolean("tiers") ? "&bTier: " + team.getTierPrefix(team) : "&bPoints: " + team.getPoints()));
                        lore.add(CC.translate("&bLeader: " + Profile.getByUuid(team.getOwner()).getName()));
                        lore.add(CC.translate("&bMembers: " + lol));
                        lore.add(CC.MENU_BAR);

                        return lore;
                    }

                    @Override
                    public byte getDamageValue(Player player) {
                        return 6;
                    }

                    @Override
                    public Material getMaterial(Player player) {
                        return Material.INK_SACK;
                    }
                });
            }
        }
        return buttons;
    }

}
