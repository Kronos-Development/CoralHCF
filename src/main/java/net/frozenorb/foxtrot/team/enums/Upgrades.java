package net.frozenorb.foxtrot.team.enums;

import com.google.common.collect.Lists;
import com.minexd.zoot.util.CC;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.ChatColor;
import org.bukkit.Material;

import java.util.List;

@AllArgsConstructor
public enum Upgrades {

    ONE("Speedy DTR", ChatColor.GOLD, Material.GOLD_BLOCK,
            Lists.newArrayList(CC.translate("&7This will make you regenerate DTR"),
            CC.translate("&7faster by 10 minutes."),
            CC.translate("&cRequired Points: 20")),
            20,
            false),

    TWO("Making Bank", ChatColor.GREEN, Material.EMERALD,
            Lists.newArrayList(CC.translate("&7This will make the store have a 20%"),
            CC.translate("&7Discount when buying materials."),
            CC.translate("&cRequired Points: 40")),
            40,
            false),

    THREE("three", ChatColor.GOLD, Material.CHEST, Lists.newArrayList("helo"),60,false),

    FOUR("four", ChatColor.GOLD, Material.ACTIVATOR_RAIL, Lists.newArrayList("helo"),80, false),

    FIVE("five", ChatColor.GOLD, Material.INK_SACK, Lists.newArrayList("helo"),100, false),

    SIX("Soul Stealer", ChatColor.DARK_RED, Material.NETHER_STAR,
            Lists.newArrayList(CC.translate("&cThis will allow you to collect the souls of players"),
                    CC.translate("&7Will give you &cStrength 2 for 1 minute every time you kill two players."),
                    CC.translate("&cRequired Points: 60")),
                    60,
                    false),

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
