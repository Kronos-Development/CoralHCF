package net.frozenorb.foxtrot.events.rampage.listener;

import com.minexd.zoot.util.CC;
import net.frozenorb.foxtrot.Foxtrot;
import net.frozenorb.foxtrot.events.Event;
import net.frozenorb.foxtrot.events.events.EventActivatedEvent;
import net.frozenorb.foxtrot.events.koth.KOTH;
import net.frozenorb.foxtrot.events.rampage.RampageHandler;
import net.frozenorb.foxtrot.events.rampage.events.RampageEvent;
import net.frozenorb.foxtrot.util.InventoryUtils;
import net.frozenorb.qlib.util.ItemBuilder;
import net.frozenorb.qlib.util.ItemUtils;
import org.bukkit.*;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;

import static org.bukkit.Material.CHEST;

public class RampageListener implements Listener {

    RampageHandler rampageHandler = Foxtrot.getInstance().getRampageHandler();

    @EventHandler
    public void onStart(RampageEvent event) {
        Bukkit.getOnlinePlayers().forEach(player -> {
            player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, 0));
            player.sendMessage(CC.translate("&4&l[RAMPAGE EVENT] &cThe rampage event has commenced!"));
        });
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (Foxtrot.getInstance().getRampageHandler().isRampageActive()) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, 0));
            player.sendMessage(CC.translate("&4&l[RAMPAGE EVENT] &cYou Joined While the Rampage Event is active!"));
        }

    }

    @EventHandler
    public void onLeave(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        if (Foxtrot.getInstance().getRampageHandler().isRampageActive()) {
            if (player.hasPotionEffect(PotionEffectType.INCREASE_DAMAGE)) {
                player.removePotionEffect(PotionEffectType.INCREASE_DAMAGE);
            }
        }

    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if (event.getBlock().getType() == CHEST) {
            Chest chest = (Chest) event.getBlock().getState();
            if (!event.getBlock().hasMetadata("RampagePackage")) return;
            event.getPlayer().getWorld().dropItemNaturally(event.getBlock().getLocation(), InventoryUtils.PRYBAR);
            event.getPlayer().getWorld().playSound(event.getBlock().getLocation(), Sound.EXPLODE, 0.2F, 0.3F);
            event.getPlayer().getWorld().playEffect(event.getBlock().getLocation(), Effect.LARGE_SMOKE, 0);
            event.getPlayer().getWorld().playEffect(event.getBlock().getLocation(), Effect.LARGE_SMOKE, 0);
            event.getPlayer().getWorld().playEffect(event.getBlock().getLocation(), Effect.LARGE_SMOKE, 0);
            event.getBlock().setType(Material.AIR);
            event.getBlock().removeMetadata("RampagePackage", Foxtrot.getInstance());

            if (chest.hasMetadata("RampagePackage")) event.setCancelled(true);
            if (event.getBlock().hasMetadata("RampagePackage")) event.setCancelled(true);

        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if (event.getAction() != Action.LEFT_CLICK_BLOCK) return;
        if (!event.getClickedBlock().hasMetadata("RampagePackage")) return;

        event.getPlayer().getWorld().dropItemNaturally(event.getClickedBlock().getLocation(), InventoryUtils.PRYBAR);
        event.getPlayer().getWorld().playSound(event.getClickedBlock().getLocation(), Sound.EXPLODE, 0.2F, 0.3F);
        event.getPlayer().getWorld().playEffect(event.getClickedBlock().getLocation(), Effect.LARGE_SMOKE, 0);
        event.getPlayer().getWorld().playEffect(event.getClickedBlock().getLocation(), Effect.LARGE_SMOKE, 0);
        event.getPlayer().getWorld().playEffect(event.getClickedBlock().getLocation(), Effect.LARGE_SMOKE, 0);
        event.getClickedBlock().setType(Material.AIR);
        event.getClickedBlock().removeMetadata("RampagePackage", Foxtrot.getInstance());

        Bukkit.getServer().broadcastMessage(CC.translate("&4&l[RAMPAGE EVENT] &cThe Prybar at &4" + event.getClickedBlock().getLocation().getX() + "&c, &4" + event.getClickedBlock().getLocation().getZ() + " &cHas been opened."));
    }

    @EventHandler
    public void onBreak(PlayerInteractEvent event) {
        if (event.getPlayer().getItemInHand().isSimilar(InventoryUtils.PRYBAR)) {
            if (event.getClickedBlock().getType() == Material.ENDER_CHEST || event.getClickedBlock().getType() == CHEST || event.getClickedBlock().getType() == Material.TRAPPED_CHEST) {
                event.getPlayer().sendMessage(CC.translate("&cYou cannot prybar this item"));
                return;
            }

            Map<Location, Material> breakmap = new HashMap<>();
            event.getPlayer().getInventory().setItemInHand(new ItemStack(Material.AIR));
            breakmap.put(event.getClickedBlock().getLocation(), event.getClickedBlock().getType());
            event.getClickedBlock().setType(Material.AIR);

            new BukkitRunnable() {

                @Override
                public void run() {
                    event.getClickedBlock().setType(breakmap.remove(event.getClickedBlock().getLocation()));
                }
            }.runTaskLater(Foxtrot.getInstance(), 120L);

        }
    }


}
