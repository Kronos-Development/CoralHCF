package net.frozenorb.foxtrot.settings.menu;

import com.google.common.collect.Maps;
import net.frozenorb.foxtrot.settings.Setting;
import net.frozenorb.foxtrot.settings.menu.color.ColorMenu;
import net.frozenorb.qlib.menu.Button;
import net.frozenorb.qlib.menu.Menu;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class SettingsMenu extends Menu {

    @Override
    public String getTitle(Player player) {
        return "Options";
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttons = Maps.newHashMap();
        // I'm going to manually set the positions for now as we only have three options to make it pretty,
        // but once we add more we should probably use a for loop to add the buttons.
        buttons.put(1, Setting.FOUND_DIAMONDS.toButton());
        buttons.put(3, Setting.DEATH_MESSAGES.toButton());
        buttons.put(5, Setting.PUBLIC_CHAT.toButton());
        buttons.put(7, Setting.TAB_LIST.toButton());

        buttons.put(11, Setting.SCOREBOARD_ABILITY_COOLDOWNS.toButton());
        buttons.put(13, Setting.SCOREBOARD_CLASS_COOLDOWNS.toButton());
        buttons.put(15, Setting.AUTOMATICALLY_F_DISPLAY.toButton());

        buttons.put(21, new Button() {
            @Override
            public String getName(Player player) {
                return ChatColor.LIGHT_PURPLE + "Change your Colors";
            }

            @Override
            public List<String> getDescription(Player player) {
                return Arrays.asList("", ChatColor.BLUE + "Click to modify your", ChatColor.BLUE + "nametag colors.", "", ChatColor.GRAY + "[TIP]", ChatColor.GRAY + "- " + ChatColor.ITALIC + "Left Click to go forward.", ChatColor.GRAY + "- " + ChatColor.ITALIC + "Right Click to go backward.");
            }

            @Override
            public Material getMaterial(Player player) {
                return Material.NAME_TAG;
            }

            @Override
            public void clicked(Player player, int slot, ClickType clickType) {
                player.closeInventory();
                new ColorMenu().openMenu(player);
            }
        });

        if(player.hasPermission("zoot.staff")) {
            buttons.put(23, Setting.LFF_MESSAGES.toButton());
            buttons.put(25, Setting.SCOREBOARD_STAFF_BOARD.toButton());
        } else buttons.put(23, Setting.LFF_MESSAGES.toButton());


        return buttons;
    }

}