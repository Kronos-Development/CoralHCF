package net.frozenorb.foxtrot.team.permissions.menu.type;

import lombok.Getter;
import net.frozenorb.foxtrot.team.Team;
import net.frozenorb.foxtrot.team.permissions.menu.PermissionsMenu;
import net.frozenorb.foxtrot.team.permissions.menu.type.buttons.PermissionPlayerButton;
import net.frozenorb.qlib.menu.Button;
import net.frozenorb.qlib.menu.Menu;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class PermissionTypeMenu extends Menu {

    @Getter private Team team;
    @Getter private String type;
    private boolean isRally;
    private boolean isDisplay;

    public PermissionTypeMenu(Team team, String type) {
        super(type + " access.");
        this.team = team;
        this.type = type;
        this.isRally = type.contains("Rally");
        this.isDisplay = type.contains("Display");
        setAutoUpdate(true);
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttonMap = new HashMap<>();

        AtomicInteger atomicInteger = new AtomicInteger(0);

        if(isRally) team.getMembers().stream().filter(member -> !team.isOwner(member) && !team.isCaptain(member) && !team.isCoLeader(member)).forEach(member -> buttonMap.put(atomicInteger.getAndIncrement(), new PermissionPlayerButton(team, isRally, isDisplay, member)));
        if(isDisplay) team.getMembers().stream().filter(member -> !team.isOwner(member) && !team.isCaptain(member) && !team.isCoLeader(member)).forEach(member -> buttonMap.put(atomicInteger.getAndIncrement(), new PermissionPlayerButton(team, isDisplay, isRally, member)));
        else team.getMembers().stream().filter(member -> !team.isOwner(member) && !team.isCaptain(member) && !team.isCoLeader(member)).forEach(member -> buttonMap.put(atomicInteger.getAndIncrement(), new PermissionPlayerButton(team, isRally, isDisplay, member)));

        buttonMap.put(size(buttonMap) - 1, new Button() {
            @Override
            public String getName(Player player) {
                return ChatColor.RED.toString() + ChatColor.BOLD + "Back";
            }

            @Override
            public List<String> getDescription(Player player) {
                return Arrays.asList("", ChatColor.RED + "Click here to return to", ChatColor.RED + "the previous menu.");
            }

            @Override
            public Material getMaterial(Player player) {
                return Material.REDSTONE;
            }

            @Override
            public void clicked(Player player, int slot, ClickType clickType) {
                player.closeInventory();
                new PermissionsMenu(team).openMenu(player);
            }
        });

        return buttonMap;
    }
}
