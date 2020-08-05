package net.frozenorb.foxtrot.crates;

import net.frozenorb.qlib.command.Command;
import org.bukkit.entity.*;
import org.bukkit.inventory.*;
import net.frozenorb.foxtrot.*;
import java.util.*;
import org.bukkit.*;
import org.bukkit.inventory.meta.*;
import net.frozenorb.qlib.command.*;
import org.bukkit.command.*;

public class CrateCommand
{
    @Command(names = { "crate" }, permission = "op", hidden = true)
    public static void onGive(Player sender, @Param(name = "kit") String kit) {
        ItemStack enderChest = new ItemStack(Material.ENDER_CHEST, 1);
        ItemMeta itemMeta = enderChest.getItemMeta();
        try {
            Crate crate = Foxtrot.getInstance().getCrateHandler().getCrates().get(kit.toLowerCase());
            itemMeta.setDisplayName(crate.getKitName());
            itemMeta.setLore(crate.getLore());
            enderChest.setItemMeta(itemMeta);
            sender.getInventory().addItem(enderChest);
            sender.sendMessage(ChatColor.GREEN + "Generated the " + crate.getKitName() + ChatColor.GREEN + " crate and added it to your inventory!");
        }
        catch (Exception ex) {
            sender.sendMessage(ChatColor.RED + "Cannot create crate item for kit '" + kit + "'");
        }
    }

    @Command(names = { "crate give" }, permission = "op", hidden = true)
    public static void onGive(CommandSender sender, @Param(name = "kit") String kit, @Param(name = "target") Player target) {
        ItemStack enderChest = new ItemStack(Material.ENDER_CHEST, 1);
        ItemMeta itemMeta = enderChest.getItemMeta();
        try {
            Crate crate = Foxtrot.getInstance().getCrateHandler().getCrates().get(kit.toLowerCase());
            itemMeta.setDisplayName(crate.getKitName());
            itemMeta.setLore(crate.getLore());
            enderChest.setItemMeta(itemMeta);
            target.getInventory().addItem(enderChest);
            sender.sendMessage(ChatColor.GREEN + "Generated the " + crate.getKitName() + ChatColor.GREEN + " crate and added it to " + target.getName() + "'s inventory!");
        }
        catch (Exception ex) {
            sender.sendMessage(ChatColor.RED + "Cannot create crate item for kit '" + kit + "'");
        }
    }

    @Command(names = "debugis", permission = "op")
    public static void onDebug(Player player, @Param(name = "kit") String kit) {
    Crate crate = Foxtrot.getInstance().getCrateHandler().getCrates().get(kit);

    player.sendMessage(crate.getKitName() + " " + crate.getItems().toString());

    }

    @Command(names = { "crate create" }, permission = "op", hidden = true)
    public static void onCreate(Player player, @Param(name = "kit") String kit) {
        Crate crate = new Crate(kit);
        Foxtrot.getInstance().getCrateHandler().getCrates().put(kit.toLowerCase(), crate);
        player.sendMessage(ChatColor.GREEN + "Created an empty crate for kit `" + crate.getKitName() + "`");
    }

    @Command(names = { "crate edit" }, permission = "op", hidden = true)
    public static void onEdit(Player player, @Param(name = "kit") String kit) {
        Crate crate = Foxtrot.getInstance().getCrateHandler().getCrates().get(kit.toLowerCase());
        if (crate == null) {
            player.sendMessage(ChatColor.RED + "Cannot edit crate for kit `" + kit + "`");
            return;
        }
        Foxtrot.getInstance().getCrateHandler().updateCrate(player, crate);
        player.sendMessage(ChatColor.GREEN + "Updated crate items for kit `" + crate.getKitName() + "`");
    }
}

