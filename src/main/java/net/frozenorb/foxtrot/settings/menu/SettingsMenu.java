package net.frozenorb.foxtrot.settings.menu;

import com.google.common.collect.Maps;
import net.frozenorb.foxtrot.settings.Setting;
import net.frozenorb.foxtrot.settings.menu.color.ColorMenu;
import net.frozenorb.foxtrot.settings.menu.display.DisplayButton;
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

        buttons.put(1, Setting.AUTOMATICALLY_F_DISPLAY.toButton());
        buttons.put(3, Setting.LFF_MESSAGES.toButton());
        buttons.put(5, new DisplayButton());
        buttons.put(7, Setting.FACTION_INVITES.toButton());


        if(player.hasPermission("zoot.staff")) {
            buttons.put(12, Setting.SCOREBOARD_STAFF_BOARD.toButton());
        }


        return buttons;
    }

}