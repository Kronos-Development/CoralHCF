package net.frozenorb.foxtrot.abilities.menu;

import net.frozenorb.foxtrot.abilities.AbstractAbility;
import net.frozenorb.qlib.menu.Button;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class AbilityInfoButton extends Button {
    private AbstractAbility ability;

    public AbilityInfoButton(AbstractAbility ability) {
        this.ability = ability;
    }

    @Override
    public void clicked(Player player, int slot, ClickType clickType) {
        if (!player.hasPermission("foxtrot.ability.give")) return;

        player.getInventory().addItem(ability.constructItem(1));
    }

    @Override
    public ItemStack getButtonItem(Player player) {
        ItemStack stack = super.getButtonItem(player);
        stack.setDurability(ability.getItem().getDurability());
        stack.addUnsafeEnchantment(Enchantment.LOOT_BONUS_BLOCKS, 10);
        return stack;
    }

    @Override
    public String getName(Player player) {
        return ability.getDisplayName();
    }

    @Override
    public List<String> getDescription(Player player) {
        return ability.getMenuLore();
    }

    @Override
    public Material getMaterial(Player player) {
        return ability.getItem().getType();
    }
}