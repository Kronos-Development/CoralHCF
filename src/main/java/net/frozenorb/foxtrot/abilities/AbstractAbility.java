package net.frozenorb.foxtrot.abilities;

import com.google.common.collect.Maps;
import net.frozenorb.foxtrot.Foxtrot;
import net.frozenorb.foxtrot.util.ReflectUtil;
import net.frozenorb.qlib.util.ItemBuilder;
import net.frozenorb.qlib.util.TimeUtils;
import net.minecraft.server.v1_7_R4.NBTTagCompound;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_7_R4.inventory.CraftItemStack;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public abstract class AbstractAbility implements Listener {

    private Map<UUID, Long> cooldownMap = new HashMap<>();

    public boolean isOnCooldown(UUID uuid) {
        return getCooldownLeft(uuid) > 0L;
    }

    public long getCooldownLeft(UUID uuid) {
        if (!cooldownMap.containsKey(uuid))
            return 0L;

        long timeLeft = cooldownMap.getOrDefault(uuid, 0L) - System.currentTimeMillis();

        if (timeLeft <= 0L)
            clearCooldown(uuid);

        return timeLeft;
    }

    public void applyCooldown(UUID uuid) {
        cooldownMap.put(uuid, System.currentTimeMillis() + getCooldown());
    }

    public void clearCooldown(UUID uuid) {
        cooldownMap.remove(uuid);
    }

    public boolean checkCooldown(Player player) {
        if (isOnCooldown(player.getUniqueId())) {
            player.sendMessage(ChatColor.RED + "You cannot use this ability for another " + ChatColor.BOLD + TimeUtils.formatIntoDetailedString((int)getCooldownLeft(player.getUniqueId()) / 1000) + ChatColor.RED + ".");
            return false;
        }
        return true;
    }

    public boolean useAbility(Player player) {
        if (!checkCooldown(player)) return false;

        applyCooldown(player.getUniqueId());
        return true;
    }


    public abstract String getId();
    public abstract long getCooldown();
    public abstract int getMaxUses();

    public abstract ItemStack getItem();

    public abstract String getDisplayName();
    public abstract ChatColor getColor();
    public abstract List<String> getDescription();

    public ItemStack constructItem(int amount) {
        return ItemBuilder
                .of(getItem().getType())
                .data(getItem().getDurability())
                .name(getDisplayName())
                .setLore(getLore())
                .amount(amount)
                .enchant(Enchantment.LOOT_BONUS_BLOCKS, 10)
                .build();
    }

    public List<String> getMenuLore() {
        List<String> lore = new ArrayList<>(getDescription());

        lore.add("");
        lore.add(ChatColor.YELLOW + "Cooldown: " + ChatColor.WHITE + TimeUtils.formatIntoDetailedString((int)getCooldown() / 1000));

        if (getMaxUses() > 0) {
            lore.add(ChatColor.YELLOW + "Maximum Uses: " + ChatColor.WHITE + getMaxUses());
        }

        return lore;
    }

    private List<String> getLore() {
        List<String> lore = new ArrayList<>(getDescription());

        if (getMaxUses() > 0) {
            lore.add("");
            lore.add(ChatColor.YELLOW + "Remaining Uses: " + ChatColor.WHITE + getMaxUses());
        }

        return lore;
    }

    public int addUse(Player player) {
        if (player.getItemInHand() == null || getMaxUses() <= 0) return Integer.MAX_VALUE;

        try {
            int remaining = getRemainingUses(player) - 1;
            setRemainingUses(player, remaining);

            return remaining;
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return 0;
    }

    private void setRemainingUses(Player player, int amount) {
        ItemStack itemInHand = player.getItemInHand();
        if (getMaxUses() <= 0 || itemInHand == null || !itemInHand.hasItemMeta() || !itemInHand.getItemMeta().hasLore()) return;

        ItemMeta meta = itemInHand.getItemMeta();
        List<String> lore = meta.getLore();
        String usesLine = lore.remove(lore.size() - 1);

        if (!usesLine.startsWith(ChatColor.YELLOW + "Remaining Uses: "))
            return;

        lore.add(ChatColor.YELLOW + "Remaining Uses: " + ChatColor.WHITE + amount);

        meta.setLore(lore);
        itemInHand.setItemMeta(meta);
        player.setItemInHand(itemInHand);
        player.updateInventory();
    }

    private int getRemainingUses(Player player) {
        ItemStack itemInHand = player.getItemInHand();
        if (getMaxUses() <= 0 || itemInHand == null || !itemInHand.hasItemMeta() || !itemInHand.getItemMeta().hasLore()) return Integer.MAX_VALUE;

        ItemMeta meta = itemInHand.getItemMeta();
        List<String> lore = meta.getLore();
        String usesLine = ChatColor.stripColor(lore.get(lore.size() - 1));

        if (!usesLine.startsWith("Remaining Uses: "))
            return 0;

        try {
            return Integer.parseInt(usesLine.split("Remaining Uses: ", 2)[1]);
        } catch (Exception ex) {}
        return 0;
    }

    public void removeOne(Player player) {
        ItemStack hand = player.getItemInHand();

        if (hand == null) return;
        if (hand.getAmount() <= 1)
            player.setItemInHand(new ItemStack(Material.AIR));
        else
            hand.setAmount(hand.getAmount() - 1);
    }

    public boolean isSimilar(ItemStack item, boolean ignoreDurability) {
        if (item == null)
            return false;

        ItemStack thisItem = constructItem(1);

        if (thisItem == item)
            return true;

        return thisItem.getTypeId() == item.getTypeId()
                && (ignoreDurability || thisItem.getDurability() == item.getDurability())
                && thisItem.hasItemMeta() == item.hasItemMeta()
                && (!thisItem.hasItemMeta() || (
                    Bukkit.getItemFactory().equals(thisItem.getItemMeta(), item.getItemMeta())
                    || (
                            thisItem.getItemMeta().getDisplayName().equalsIgnoreCase(item.getItemMeta().getDisplayName())
                            && Maps.difference(
                                    thisItem.getItemMeta().getEnchants(),
                                    item.getItemMeta().getEnchants()
                            ).areEqual()
                    )
                ));
    }

}