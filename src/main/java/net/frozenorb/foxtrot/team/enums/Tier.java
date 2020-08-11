package net.frozenorb.foxtrot.team.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.ChatColor;

@AllArgsConstructor
public enum Tier {

    TIERI ("Tier1", "Tier I", ChatColor.GOLD),
    TIERII ("Tier2", "Tier II", ChatColor.YELLOW),
    TIERIII ("Tier3", "Tier III", ChatColor.AQUA),
    TIERIV ("Tier4", "Tier IV", ChatColor.DARK_AQUA),
    TIERV ("Tier5", "Tier V", ChatColor.BLUE);

    //this is how we get each team's tier
    @Getter private String name, displayName;
    @Getter private ChatColor color;

}
