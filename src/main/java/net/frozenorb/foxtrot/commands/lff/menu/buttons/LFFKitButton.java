package net.frozenorb.foxtrot.commands.lff.menu.buttons;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import net.frozenorb.foxtrot.commands.lff.menu.LFFMenu;
import net.frozenorb.qlib.menu.Button;
import net.frozenorb.qlib.util.ItemBuilder;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;
@Getter
public class LFFKitButton extends Button {

    private String kitName;
    private Material kitIcon;
    private LFFMenu parent;

    public LFFKitButton(String kitName, Material kitIcon, LFFMenu parent) {
        this.kitName = kitName;
        this.kitIcon = kitIcon;
        this.parent = parent;
    }

    @Override
    public String getName(Player player) {
        return ChatColor.LIGHT_PURPLE + kitName;
    }

    @Override
    public List<String> getDescription(Player player) {
        return Arrays.asList("", ChatColor.BLUE + "Click to select the", ChatColor.BLUE + kitName + " class.");
    }

    @Override
    public Material getMaterial(Player player) {
        return kitIcon;
    }

    @Override
    public ItemStack getButtonItem(Player player) {
        ItemStack itemStack = ItemBuilder.of(kitIcon).name(getName(player)).setLore(getDescription(player)).build();
        if(parent.getSelected().contains(getKitName())) itemStack.addUnsafeEnchantment(Enchantment.DURABILITY, 1);

        return itemStack;
    }

    @Override
    public void clicked(Player player, int slot, ClickType clickType) {
        boolean selected = parent.getSelected().contains(getKitName());
        if(selected) parent.getSelected().remove(getKitName());
        else parent.getSelected().add(getKitName());

        player.sendMessage(ChatColor.YELLOW + "You have " + (!selected ? ChatColor.GREEN + "now" : ChatColor.RED + "no longer") + ChatColor.YELLOW + " selected the " + ChatColor.LIGHT_PURPLE + kitName + ChatColor.YELLOW + " class.");
    }
}
