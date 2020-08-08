package net.frozenorb.foxtrot.team.permissions.menu.type.buttons;

import lombok.AllArgsConstructor;
import net.frozenorb.foxtrot.team.Team;
import net.frozenorb.qlib.menu.Button;
import net.frozenorb.qlib.util.UUIDUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
public class PermissionPlayerButton extends Button {

    private Team team;
    private boolean isRally;
    private boolean isDisplay;
    private UUID member;

    @Override
    public String getName(Player player) {
        return ChatColor.LIGHT_PURPLE + UUIDUtils.name(member);
    }

    @Override
    public List<String> getDescription(Player player) {
        List<String> description = new ArrayList<>();
        if (isRally) {
            if (team.hasRallyPermission(member)) {
                description.add(ChatColor.BLUE.toString() + ChatColor.BOLD + "  ► " + ChatColor.YELLOW + "Allowed");
                description.add("    " + ChatColor.YELLOW + "Disallowed");
            } else {
                description.add("    " + ChatColor.YELLOW + "Allowed");
                description.add(ChatColor.BLUE.toString() + ChatColor.BOLD + "  ► " + ChatColor.YELLOW + "Disallowed");
            }
        } else if (isDisplay) {
                if (team.hasDisplayPermission(member)) {
                    description.add(ChatColor.BLUE.toString() + ChatColor.BOLD + "  ► " + ChatColor.YELLOW + "Allowed");
                    description.add("    " + ChatColor.YELLOW + "Disallowed");
                } else {
                    description.add("    " + ChatColor.YELLOW + "Allowed");
                    description.add(ChatColor.BLUE.toString() + ChatColor.BOLD + "  ► " + ChatColor.YELLOW + "Disallowed");
                }
            }else {
            if(team.hasSubclaimPermission(member)) {
                description.add(ChatColor.BLUE.toString() + ChatColor.BOLD + "  ► " + ChatColor.YELLOW + "Allowed");
                description.add("    " + ChatColor.YELLOW + "Disallowed");
            } else {
                description.add("    " + ChatColor.YELLOW + "Allowed");
                description.add(ChatColor.BLUE.toString() + ChatColor.BOLD + "  ► " + ChatColor.YELLOW + "Disallowed");
            }
        }

        return description;
    }

    @Override
    public Material getMaterial(Player player) {
        return Material.SKULL_ITEM;
    }

    @Override
    public byte getDamageValue(Player player) {
        return 3;
    }

    @Override
    public void clicked(Player player, int slot, ClickType clickType) {
        boolean value;
        if(isRally) {
            value = !team.hasRallyPermission(member);
            team.setRallyPermission(player.getUniqueId(), member, value);
        }if(isDisplay) {
            value = !team.hasDisplayPermission(member);
            team.setDisplayPermission(player.getUniqueId(), member, value);
        }
        else {
            value = !team.hasSubclaimPermission(member);
            team.setSubclaimPermission(player.getUniqueId(), member, value);
        }
        team.flagForSave();
    }
}
