package net.frozenorb.foxtrot.server.conditional.spectator.menus;

import com.minexd.zoot.util.CC;
import net.frozenorb.foxtrot.Foxtrot;
import net.frozenorb.foxtrot.persist.maps.FocusColorMap;
import net.frozenorb.foxtrot.server.conditional.spectator.SpectatorHandler;
import net.frozenorb.foxtrot.server.conditional.spectator.SpectatorIntent;
import net.frozenorb.qlib.menu.Button;
import net.frozenorb.qlib.menu.Menu;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SpectatorMenu extends Menu {
    @Override
    public String getTitle(Player player) {
        return CC.translate("&bSpectator Settings");
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        buttons.put(1, new Button() {
            @Override
            public String getName(Player player) {
                return CC.translate("&3Spectate an Active Koth");
            }

            @Override
            public void clicked(Player player, int slot, ClickType clickType) {
                Foxtrot.getInstance().getSpectatorHandler().SpectatorAdd(player, SpectatorIntent.SPECTATE_KOTH);
            }

            @Override
            public List<String> getDescription(Player player) {
                List<String> lore = new ArrayList<>();
                lore.add(CC.MENU_BAR);
                lore.add(CC.translate("&bClick to spectate an active koth"));
                lore.add(CC.MENU_BAR);
                return lore;
            }

            @Override
            public Material getMaterial(Player player) {
                return Material.DIAMOND_SWORD;
            }
        });

        buttons.put(3, new Button() {
            @Override
            public String getName(Player player) {
                return CC.translate("&3Spectate an Active Teamfight");
            }

            @Override
            public void clicked(Player player, int slot, ClickType clickType) {
                Foxtrot.getInstance().getSpectatorHandler().SpectatorAdd(player, SpectatorIntent.SPECTATE_TEAMFIGHT);
            }

            @Override
            public List<String> getDescription(Player player) {
                List<String> lore = new ArrayList<>();
                lore.add(CC.MENU_BAR);
                lore.add(CC.translate("&bClick to spectate an active teamfight"));
                lore.add(CC.MENU_BAR);
                return lore;
            }

            @Override
            public Material getMaterial(Player player) {
                return Material.GOLD_SWORD;
            }
        });
        buttons.put(5, new Button() {
            @Override
            public String getName(Player player) {
                return CC.translate("&3Spectate a specific Faction");
            }

            @Override
            public void clicked(Player player, int slot, ClickType clickType) {
                Foxtrot.getInstance().getSpectatorHandler().SpectatorAdd(player, SpectatorIntent.SPECTATE_FACTION);
            }

            @Override
            public List<String> getDescription(Player player) {
                List<String> lore = new ArrayList<>();
                lore.add(CC.MENU_BAR);
                lore.add(CC.translate("&bClick to spectate a specific faction"));
                lore.add(CC.MENU_BAR);
                return lore;
            }

            @Override
            public Material getMaterial(Player player) {
                return Material.IRON_SWORD;
            }
        });
        buttons.put(7, new Button() {
            @Override
            public String getName(Player player) {
                return CC.translate("&3General Spectate");
            }

            @Override
            public void clicked(Player player, int slot, ClickType clickType) {
                Foxtrot.getInstance().getSpectatorHandler().SpectatorAdd(player, SpectatorIntent.GENERAL_SPECTATE);
            }

            @Override
            public List<String> getDescription(Player player) {
                List<String> lore = new ArrayList<>();
                lore.add(CC.MENU_BAR);
                lore.add(CC.translate("&bClick to just rome around the map and see what people are doing on coral."));
                lore.add(CC.MENU_BAR);
                return lore;
            }

            @Override
            public Material getMaterial(Player player) {
                return Material.STONE_SWORD;
            }
        });
        return buttons;
    }
}
