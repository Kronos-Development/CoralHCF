package net.frozenorb.foxtrot.abilities.type;

import com.google.common.collect.Lists;
import com.minexd.zoot.util.CC;
import net.frozenorb.foxtrot.Foxtrot;
import net.frozenorb.foxtrot.abilities.AbilityHandler;
import net.frozenorb.foxtrot.abilities.AbstractAbility;
import net.frozenorb.foxtrot.team.dtr.DTRBitmask;
import net.frozenorb.qlib.util.ItemBuilder;
import net.minecraft.server.v1_7_R4.Packet;
import net.minecraft.server.v1_7_R4.PacketPlayOutWorldParticles;
import net.minecraft.server.v1_7_R4.PlayerConnection;
import org.bukkit.*;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class FlashAbility extends AbstractAbility {
    @Override
    public String getId() {
        return "FLASHBANG";
    }

    @Override
    public long getCooldown() {
        return TimeUnit.MINUTES.toMillis(2);
    }

    @Override
    public int getMaxUses() {
        return -1;
    }

    @Override
    public ItemStack getItem() {
        return ItemBuilder
                .of(Material.INK_SACK)
                .data((short) 15)
                .build();
    }

    @Override
    public String getDisplayName() {
        return ChatColor.WHITE + ChatColor.BOLD.toString() + "Cody's Flashbang";
    }

    @Override
    public String getScoreboardName() {
        return ChatColor.WHITE + ChatColor.BOLD.toString() + "Flashbang";
    }

    @Override
    public ChatColor getColor() {
        return ChatColor.WHITE;
    }

    @Override
    public List<String> getDescription() {
        return Lists.newArrayList(ChatColor.GRAY + "Temporarily stun your",
                ChatColor.DARK_GRAY + "Opponents for either a quick getaway",
                ChatColor.DARK_GRAY + "Or a sneaky attack.");
    }

    @EventHandler
    public void onThrow(PlayerInteractEvent event) {
        if (event.getAction() == Action.LEFT_CLICK_BLOCK || event.getAction() == Action.LEFT_CLICK_AIR) return;
        Player player = event.getPlayer();
        ItemStack itemInHand = event.getPlayer().getItemInHand();

        if (itemInHand == null || !isSimilar(itemInHand, false)) return;
        player.launchProjectile(Snowball.class);
        addUse(player);
    }

    @EventHandler
    public void onLaunch(ProjectileLaunchEvent event) {
        if (!(event.getEntity().getShooter() instanceof Player)) {
            return;
        }
        final Projectile entity = event.getEntity();
        final Player player = (Player)entity.getShooter();
        ItemStack itemInHand = player.getItemInHand();

        if (itemInHand == null || !isSimilar(itemInHand, false)) return;
        if (entity instanceof Snowball) {
            Snowball snowball = (Snowball)entity;

            if (!checkCooldown(player)) {
                event.setCancelled(true);
                player.updateInventory();
                return;
            }

            if (DTRBitmask.SAFE_ZONE.appliesAt(player.getLocation())) {
                event.setCancelled(true);
                player.sendMessage(ChatColor.RED + "You can't do this is in a Safe-Zone!");
                player.updateInventory();
                return;
            }

            if (Foxtrot.getInstance().getPvPTimerMap().hasTimer(player.getUniqueId())) {
                event.setCancelled(true);
                player.sendMessage(ChatColor.RED + "You can't do this while you have PVP Timer!");
                player.updateInventory();
                return;
            }
            
            snowball.setMetadata("flashbang", new FixedMetadataValue(Foxtrot.getInstance(), player.getUniqueId()));
        }
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {
        if(!(event.getDamager() instanceof Snowball)) return;
        if(!(event.getEntity() instanceof Player)) return;

        Snowball snowball = (Snowball)event.getDamager();
        Player damaged = (Player) event.getEntity();
        if (!snowball.hasMetadata("flashbang")) return;

        damaged.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 100, 2));
        damaged.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 40, 2));

        Player attacker = (Player) snowball.getShooter();

        Location victimLoc = damaged.getLocation();
        Location attackerLoc = attacker.getLocation();

        if (DTRBitmask.SAFE_ZONE.appliesAt(attackerLoc)) {
            attacker.sendMessage(CC.translate("&c&lWARNING! &eYou can't do this is in a &aSafe-Zone&e!"));
            return;
        }

        if (DTRBitmask.SAFE_ZONE.appliesAt(victimLoc)) {
            attacker.sendMessage(CC.translate("&c&lWARNING! &eThis player is in a &aSafe-Zone&e!"));
            return;
        }

        if (Foxtrot.getInstance().getPvPTimerMap().hasTimer(attacker.getUniqueId())) {
            attacker.sendMessage(CC.translate("&c&lWARNING! &eyou can't do this while you have &aPVP Timer&e!"));
            return;
        }

        if (Foxtrot.getInstance().getPvPTimerMap().hasTimer(damaged.getUniqueId())) {
            attacker.sendMessage(CC.translate("&c&lWARNING! &eThis player currently hsa their &aPVP Timer&e!"));
            return;
        }

        useAbility((Player) snowball.getShooter());

        new BukkitRunnable() {
            public void run() {
                World world = damaged.getWorld();
                world.playEffect(damaged.getLocation(), Effect.EXPLOSION_HUGE, 0);
                world.playEffect(damaged.getLocation(), Effect.CLOUD, 0);
                world.playEffect(damaged.getLocation(), Effect.CLOUD, 0);
                world.playEffect(damaged.getLocation(), Effect.EXPLOSION_HUGE, 0);
                damaged.playSound(damaged.getLocation(), Sound.FIREWORK_LARGE_BLAST, 2, 0);
            }
        }.runTask(Foxtrot.getInstance());
    }
}
