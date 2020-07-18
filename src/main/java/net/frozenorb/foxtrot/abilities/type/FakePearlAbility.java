package net.frozenorb.foxtrot.abilities.type;

import com.google.common.collect.Lists;
import com.minexd.zoot.profile.Profile;
import net.frozenorb.foxtrot.Foxtrot;
import net.frozenorb.foxtrot.abilities.AbstractAbility;
import net.frozenorb.foxtrot.pvpclasses.PvPClassHandler;
import net.frozenorb.foxtrot.pvpclasses.pvpclasses.ScoutClass;
import net.frozenorb.qlib.util.ItemBuilder;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.EnderPearl;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.util.Vector;

import java.util.*;
import java.util.concurrent.TimeUnit;

public class FakePearlAbility extends AbstractAbility {
    @Override
    public String getId() {
        return "FAKE_PEARL";
    }

    @Override
    public long getCooldown() {
        return TimeUnit.SECONDS.toMillis(45L);
    }

    @Override
    public int getMaxUses() {
        return -1;
    }

    @Override
    public ItemStack getItem() {
        return ItemBuilder
                .of(Material.ENDER_PEARL)
                .build();
    }

    @Override
    public String getDisplayName() {
        return ChatColor.DARK_PURPLE.toString() + ChatColor.BOLD + "DeadEdit's Fake Pearl";
    }

    @Override
    public String getScoreboardName() {
        return ChatColor.DARK_PURPLE.toString() + ChatColor.BOLD + "Fake Pearl";
    }

    @Override
    public ChatColor getColor() {
        return ChatColor.DARK_PURPLE;
    }

    @Override
    public List<String> getDescription() {
        return Lists.newArrayList(
                ChatColor.GRAY + "Need to run away but need to trick",
                ChatColor.GRAY + "your enemies? Throw a Fake Pearl!"
        );
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack is = event.getItem();
        if(is == null || is.getType() == null || is.getType() == Material.AIR || !is.hasItemMeta() || !this.isSimilar(player.getItemInHand(), true)) return;
        if(!useAbility(player)) {
            event.setCancelled(true);
            return;
        }

        player.setMetadata("FakePearl", new FixedMetadataValue(Foxtrot.getInstance(), true));
    }

    @EventHandler
    public void onTeleport(PlayerTeleportEvent event) {
        Player player = event.getPlayer();
        if(event.getCause() != PlayerTeleportEvent.TeleportCause.ENDER_PEARL) return;
        if(player.hasMetadata("FakePearl")) {
            player.removeMetadata("FakePearl", Foxtrot.getInstance());
            event.setCancelled(true);
        }
    }

}