package net.frozenorb.foxtrot.team.enums;

import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.ChatColor;
import org.bukkit.Material;

import java.util.List;

@AllArgsConstructor
public enum Upgrades {

    ONE("one", ChatColor.GOLD, Material.LEATHER_HELMET, Lists.newArrayList("helo"), 20, false),
    TWO("two", ChatColor.GOLD, Material.EGG, Lists.newArrayList("helo"),40,false),
    THREE("three", ChatColor.GOLD, Material.CHEST, Lists.newArrayList("helo"),60,false),
    FOUR("four", ChatColor.GOLD, Material.ACTIVATOR_RAIL, Lists.newArrayList("helo"),80, false),
    FIVE("five", ChatColor.GOLD, Material.INK_SACK, Lists.newArrayList("helo"),100, false),
    SIX("six", ChatColor.GOLD, Material.INK_SACK, Lists.newArrayList("helo"),120, false),
    SEVEN("seven", ChatColor.GOLD, Material.INK_SACK, Lists.newArrayList("helo"),140,false),
    EIGHT("eight", ChatColor.GOLD, Material.INK_SACK, Lists.newArrayList("helo"),160, false),
    NINE("nine", ChatColor.GOLD, Material.INK_SACK, Lists.newArrayList("helo"),180, false);

    //stupid method for setting shit
    @Getter private String name;
    @Getter private ChatColor color;
    @Getter private Material material;
    @Getter private List<String> description;
    @Getter private int ptsrequired;
    @Getter @Setter private boolean enabled;
}
