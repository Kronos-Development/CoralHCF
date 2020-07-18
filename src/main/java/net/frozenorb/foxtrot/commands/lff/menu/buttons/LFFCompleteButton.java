package net.frozenorb.foxtrot.commands.lff.menu.buttons;

import com.sk89q.util.StringUtil;
import lombok.AllArgsConstructor;
import net.frozenorb.foxtrot.Foxtrot;
import net.frozenorb.foxtrot.commands.lff.LFFCommand;
import net.frozenorb.foxtrot.commands.lff.menu.LFFMenu;
import net.frozenorb.qlib.menu.Button;
import net.minecraft.util.org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
public class LFFCompleteButton extends Button {

    private final LFFMenu lffMenu;

    @Override
    public String getName(Player player) {
        return ChatColor.GREEN + "Complete";
    }

    @Override
    public List<String> getDescription(Player player) {
        List<String> lore = new ArrayList<>(Collections.singletonList(""));
        if(lffMenu.getSelected().isEmpty()) lore.add(ChatColor.RED + "You don't have classes selected!");
        else {
            lore.add(ChatColor.BLUE + "You have selected:");
            lffMenu.getSelected().forEach(lffKitButton -> lore.add(ChatColor.BLUE.toString() + ChatColor.BOLD + "  ► " + ChatColor.BLUE + lffKitButton));
        }
        return lore;
    }

    @Override
    public Material getMaterial(Player player) {
        return Material.EMERALD_BLOCK;
    }

    @Override
    public void clicked(Player player, int slot, ClickType clickType) {
        if(lffMenu.getSelected().isEmpty()) {
            player.sendMessage(ChatColor.RED + "You do not have any classes selected!");
            return;
        }

        player.closeInventory();
        LFFCommand.applyCooldown(player.getUniqueId());

        Bukkit.getOnlinePlayers().stream().filter(aPlayer -> Foxtrot.getInstance().getToggleLFFMessageMap().isEnabled(aPlayer.getUniqueId())).forEach(aPlayer -> {
            aPlayer.sendMessage(ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH + StringUtils.repeat('-', 43));
            aPlayer.sendMessage(player.getDisplayName() + ChatColor.WHITE + " is " + ChatColor.AQUA + "looking for a faction" + ChatColor.WHITE + "!");
            aPlayer.sendMessage(ChatColor.GRAY + " » " + ChatColor.AQUA + "Classes: " + ChatColor.WHITE + StringUtils.join(lffMenu.getSelected(), ChatColor.WHITE + ", "));
            aPlayer.sendMessage(ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH + StringUtils.repeat('-', 43));
        });


    }
}
