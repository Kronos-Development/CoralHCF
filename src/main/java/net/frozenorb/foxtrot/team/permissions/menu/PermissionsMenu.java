package net.frozenorb.foxtrot.team.permissions.menu;

import net.frozenorb.foxtrot.team.Team;
import net.frozenorb.foxtrot.team.permissions.menu.type.PermissionTypeMenu;
import net.frozenorb.qlib.menu.Button;
import net.frozenorb.qlib.menu.Menu;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PermissionsMenu extends Menu {

    private Team team;

    public PermissionsMenu(Team team) {
        super("Team Permission nodes");
        this.team = team;
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttonMap = new HashMap<>();

        buttonMap.put(0, new Button() {
            @Override
            public String getName(Player player) {
                return ChatColor.GREEN + "Sign Subclaim";
            }

            @Override
            public List<String> getDescription(Player player) {
                return Collections.singletonList(ChatColor.WHITE + "Who can open ANY sign subclaim?");
            }

            @Override
            public Material getMaterial(Player player) {
                return Material.CHEST;
            }

            @Override
            public void clicked(Player player, int slot, ClickType clickType) {
                player.closeInventory();
                new PermissionTypeMenu(team, "Sign Subclaim").openMenu(player);
            }
        });

        buttonMap.put(1, new Button() {
            @Override
            public String getName(Player player) {
                return ChatColor.GREEN + "Rally";
            }

            @Override
            public List<String> getDescription(Player player) {
                return Collections.singletonList(ChatColor.WHITE + "Who can set the faction rally?");
            }

            @Override
            public Material getMaterial(Player player) {
                return Material.BEACON;
            }

            @Override
            public void clicked(Player player, int slot, ClickType clickType) {
                player.closeInventory();
                new PermissionTypeMenu(team, "Rally").openMenu(player);
            }
        });

        buttonMap.put(2, new Button() {
            @Override
            public String getName(Player player) {
                return ChatColor.GREEN + "Display";
            }

            @Override
            public List<String> getDescription(Player player) {
                return Collections.singletonList(ChatColor.WHITE + "Who can set faction displays?");
            }

            @Override
            public Material getMaterial(Player player) {
                return Material.FIREBALL;
            }

            @Override
            public void clicked(Player player, int slot, ClickType clickType) {
                player.closeInventory();
                new PermissionTypeMenu(team, "Display").openMenu(player);
            }
        });

        return buttonMap;
    }
}
