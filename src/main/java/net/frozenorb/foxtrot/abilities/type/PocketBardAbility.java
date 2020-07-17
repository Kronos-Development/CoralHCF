package net.frozenorb.foxtrot.abilities.type;

import com.google.common.collect.Lists;
import net.frozenorb.foxtrot.abilities.AbstractAbility;
import net.frozenorb.foxtrot.pvpclasses.PvPClass;
import net.frozenorb.foxtrot.pvpclasses.pvpclasses.BardClass;
import net.frozenorb.foxtrot.pvpclasses.pvpclasses.bard.BardEffect;
import net.frozenorb.foxtrot.util.PotionUtils;
import net.frozenorb.qlib.util.ItemBuilder;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class PocketBardAbility extends AbstractAbility {
    @Override
    public String getId() {
        return "POCKET_BARD";
    }

    @Override
    public long getCooldown() {
        return TimeUnit.SECONDS.toMillis(45);
    }

    @Override
    public int getMaxUses() {
        return -1;
    }

    @Override
    public ItemStack getItem() {
        return ItemBuilder
                .of(Material.INK_SACK)
                .data((short) 11)
                .build();
    }

    @Override
    public String getDisplayName() {
        return ChatColor.YELLOW.toString() + ChatColor.BOLD + "Pocket Bard";
    }

    @Override
    public String getScoreboardName() {
        return getDisplayName();
    }

    @Override
    public ChatColor getColor() {
        return ChatColor.YELLOW;
    }

    @Override
    public List<String> getDescription() {
        return Lists.newArrayList(
                ChatColor.GRAY + "Use this to give yourself",
                ChatColor.GRAY + "a choice of effects."
        );
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack itemInHand = player.getItemInHand();

        if (itemInHand == null || !isSimilar(itemInHand, false)) return;

        if (!useAbility(player)) {
            event.setCancelled(true);
            player.updateInventory();
            return;
        }

        event.setCancelled(true);
        removeOne(player);
        player.updateInventory();

        BardEffect bardEffect = new ArrayList<>(BardClass.BARD_CLICK_EFFECTS.values().stream().filter(bardEffect1 -> bardEffect1.getPotionEffect() != null && bardEffect1.getPotionEffect().getType() != PotionEffectType.WITHER && bardEffect1.getPotionEffect().getType() != PotionEffectType.SPEED && bardEffect1.getPotionEffect().getType() != PotionEffectType.JUMP).collect(Collectors.toList())).get(new Random().nextInt(BardClass.BARD_PASSIVE_EFFECTS.values().size()));

        player.sendMessage(ChatColor.YELLOW + "Your " + getDisplayName() + ChatColor.YELLOW + " has given you " + ChatColor.WHITE + PotionUtils.getName(bardEffect.getPotionEffect().getType())  + " " + (bardEffect.getPotionEffect().getAmplifier() + 1) + ChatColor.YELLOW + "!");
        PvPClass.smartAddPotion(player, bardEffect.getPotionEffect(), true, null);
    }
}