package net.frozenorb.foxtrot.abilities.type;

import com.google.common.collect.Lists;
import net.frozenorb.foxtrot.abilities.AbstractAbility;
import net.frozenorb.qlib.util.ItemBuilder;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Collections;
import java.util.List;

public class GasMaskAbility extends AbstractAbility {
    @Override
    public String getId() {
        return "GAS_MASK";
    }

    @Override
    public long getCooldown() {
        return 0;
    }

    @Override
    public int getMaxUses() {
        return -1;
    }

    @Override
    public ItemStack getItem() {
        return ItemBuilder
            .of(Material.LEATHER_HELMET)
                .enchant(Enchantment.PROTECTION_ENVIRONMENTAL, 3)
            .color(Color.WHITE)
            .build();
    }

    @Override
    public String getDisplayName() {
        return ChatColor.DARK_GREEN + "Gas Mask";
    }

    @Override
    public String getScoreboardName() {
        return getDisplayName();
    }

    @Override
    public ChatColor getColor() {
        return ChatColor.DARK_GREEN;
    }

    @Override
    public List<String> getDescription() {
        return Lists.newArrayList(
                ChatColor.GRAY + "Wear this in the Biohazard Event",
                ChatColor.GRAY + "to not take any poison damage"
        );
    }
}
