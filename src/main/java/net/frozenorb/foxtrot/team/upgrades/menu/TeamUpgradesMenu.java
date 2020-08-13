package net.frozenorb.foxtrot.team.upgrades.menu;

import com.google.common.collect.Lists;
import com.minexd.zoot.util.CC;
import net.frozenorb.foxtrot.team.Team;
import net.frozenorb.foxtrot.team.enums.Upgrades;
import net.frozenorb.foxtrot.team.permissions.menu.type.PermissionTypeMenu;
import net.frozenorb.qlib.menu.Button;
import net.frozenorb.qlib.menu.Menu;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TeamUpgradesMenu extends Menu {

    private Upgrades upgrades;
    private Team team;

    public TeamUpgradesMenu(Team team) {
        super("Team Upgrades");
        this.team = team;
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttonMap = new HashMap<>();
        int i = 0;
        for (Upgrades upgrades : Upgrades.values()) {
            buttonMap.put(i++, new Button() {
                @Override
                public String getName(Player player) {
                    return upgrades.getColor() + ChatColor.BOLD.toString() + upgrades.getName();
                }

                @Override
                public List<String> getDescription(Player player) {
                    return upgrades.getDescription();
                }

                @Override
                public Material getMaterial(Player player) {
                    return upgrades.getMaterial();
                }

                @Override
                public void clicked(Player player, int slot, ClickType clickType) {
                    if (team.getPoints() < upgrades.getPtsrequired()) {
                        player.sendMessage(CC.translate("&cYou don't have enough points for the &l" + upgrades.getName() + "&c upgrade."));
                        return;
                    }
                    if (upgrades.isEnabled() == true) {
                        player.sendMessage(CC.translate("&cYou already have the " + upgrades.getName() + " upgrade."));
                        return;
                    }
                    upgrades.setEnabled(true);

                }
            });
        }

        return buttonMap;
    }
}
