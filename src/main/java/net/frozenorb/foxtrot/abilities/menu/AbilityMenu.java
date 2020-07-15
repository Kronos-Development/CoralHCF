package net.frozenorb.foxtrot.abilities.menu;

import net.frozenorb.foxtrot.Foxtrot;
import net.frozenorb.foxtrot.abilities.AbstractAbility;
import net.frozenorb.qlib.menu.Button;
import net.frozenorb.qlib.menu.Menu;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class AbilityMenu extends Menu {
    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        int i = 0;
        for (AbstractAbility ability : Foxtrot.getInstance().getAbilityHandler().getAbilities()) {
            buttons.put(i, new AbilityInfoButton(ability));
            i++;
        }

        return buttons;
    }

    @Override
    public String getTitle(Player player) {
        return ChatColor.DARK_AQUA.toString() + ChatColor.BOLD + "Abilities " + ChatColor.GRAY + "(" + Foxtrot.getInstance().getAbilityHandler().getAbilities().size() + ")";
    }
}