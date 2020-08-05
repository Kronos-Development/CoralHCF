package net.frozenorb.foxtrot.crates;

import net.frozenorb.qlib.qLib;
import org.bukkit.inventory.*;
import java.util.*;
import org.bukkit.*;

public class Crate
{
    private String kitName;
    private ItemStack[] items;

    public Crate(String kitName) {
        this(kitName, new ItemStack[1]);
    }

    public Crate(String kitName, ItemStack[] items) {
        this.kitName = kitName;
        this.items = items;
    }

    public List<String> getLore() {
        List<String> lore = new ArrayList<String>();
        lore.add(ChatColor.DARK_PURPLE + "Right click to open this " + this.kitName + ChatColor.DARK_PURPLE + " crate.");
        lore.add("");
        lore.add(ChatColor.YELLOW + "Crate requires " + ChatColor.DARK_GRAY + this.getSize() + ChatColor.YELLOW + " empty slots to open.");
        return lore;
    }

    public int getSize() {
        return this.items.length;
    }

    public String getKitName() {
        return this.kitName;
    }

    public ItemStack[] getItems() {
        return this.items;
    }

    public void setItems(ItemStack[] items) {
        this.items = items;
    }
}
