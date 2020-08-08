package net.frozenorb.foxtrot.map.kits;

import java.sql.Blob;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.entity.Wolf;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import com.google.common.collect.Maps;

import net.frozenorb.foxtrot.Foxtrot;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class KitListener implements Listener {
    
    private static Map<UUID, Long> lastClicked = Maps.newHashMap();

    @EventHandler
    public void onPlayerInteractEntityEvent(PlayerInteractEntityEvent event) {
        if (event.getRightClicked() instanceof Wolf) {
            ((Wolf) event.getRightClicked()).setSitting(false);
            event.setCancelled(true);
        }
    }
    
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        
        if (event.getClickedBlock() == null || !(event.getClickedBlock().getState() instanceof Sign)) {
            return;
        }
        
        Sign sign = (Sign) event.getClickedBlock().getState();
        
        if (!sign.getLine(0).startsWith(ChatColor.BLUE + "- Kit")) {
            return;
        }
        
        Kit kit = Foxtrot.getInstance().getMapHandler().getKitManager().get(player.getUniqueId(), sign.getLine(1));
        
        attemptApplyKit(player, kit);
    }

    public static void attemptApplyKit(Player player, Kit kit) {
        if (kit == null) {
            player.sendMessage(ChatColor.RED + "Unknown kit.");
            return;
        }

        if (player.hasMetadata("modmode")) {
            player.sendMessage(ChatColor.RED + "You cannot use this while in mod mode.");
            return;
        }

        if (lastClicked.containsKey(player.getUniqueId()) && (System.currentTimeMillis() - lastClicked.get(player.getUniqueId()) < TimeUnit.SECONDS.toMillis(15))) {
            player.sendMessage(ChatColor.RED + "Please wait before using this again.");
            return;
        }

        if (!Foxtrot.getInstance().getMapHandler().getKitManager().canUseKit(player, kit.getName())) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cYou do not own this kit. Purchase it at store.coral.gg."));
            return;
        }

        kit.apply(player);

        lastClicked.put(player.getUniqueId(), System.currentTimeMillis());
    }

//    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
//    public void onMove(PlayerInteractEvent event) {
//        Player player = event.getPlayer();
//        Block block = event.getClickedBlock();
//
//        if(block.getType() == Material.STONE_PLATE) {
//           if (block.getRelative(BlockFace.DOWN).getType() == Material.OBSIDIAN) {
//               player.setVelocity(player.getLocation().getDirection().multiply(7.5).setY(1.5));
//            }
//        }

    private Map<String, Boolean> noDamage = new HashMap<>();

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player) || !(event.getCause() == EntityDamageEvent.DamageCause.FALL)) return;

        Player player = (Player) event.getEntity();
        if (!noDamage.containsKey(player.getName()) || !noDamage.get(player.getName())) return;
        event.setCancelled(true);
        noDamage.remove(player.getName());
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void helo(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        Location location = event.getTo();

        if (location.getBlock().getType() == Material.STONE_PLATE) {
            if (location.getBlock().getRelative(BlockFace.DOWN).getType() == Material.OBSIDIAN) {
                player.setVelocity(player.getLocation().getDirection().multiply(7.5).setY(1.5));
                player.setMetadata("noflag", new FixedMetadataValue(Foxtrot.getInstance(), true));
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        player.removeMetadata("noflag", Foxtrot.getInstance());
                    }
                }.runTaskLater(Foxtrot.getInstance(), 100);
                noDamage.put(player.getName(), true);
            }
        }

        if (location.getBlock().getType() == Material.WOOD_PLATE) {
            if (location.getBlock().getRelative(BlockFace.DOWN).getType() == Material.OBSIDIAN) {
                player.setVelocity(player.getLocation().getDirection().multiply(5.5).setY(2));
                player.setMetadata("noflag", new FixedMetadataValue(Foxtrot.getInstance(), true));
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        player.removeMetadata("noflag", Foxtrot.getInstance());
                    }
                }.runTaskLater(Foxtrot.getInstance(), 100);
                noDamage.put(player.getName(), true);
            }
        }

        if (location.getBlock().getType() == Material.GOLD_PLATE) {
            if (location.getBlock().getRelative(BlockFace.DOWN).getType() == Material.OBSIDIAN) {
                player.setVelocity(player.getLocation().getDirection().multiply(3.5).setY(5));
                player.setMetadata("noflag", new FixedMetadataValue(Foxtrot.getInstance(), true));
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        player.removeMetadata("noflag", Foxtrot.getInstance());
                    }
                }.runTaskLater(Foxtrot.getInstance(), 100);
                noDamage.put(player.getName(), true);
            }
        }

        if (location.getBlock().getType() == Material.IRON_PLATE) {
            if (location.getBlock().getRelative(BlockFace.DOWN).getType() == Material.OBSIDIAN) {
                player.setVelocity(player.getLocation().getDirection().multiply(1.5).setY(1.5));
                player.setMetadata("noflag", new FixedMetadataValue(Foxtrot.getInstance(), true));
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        player.removeMetadata("noflag", Foxtrot.getInstance());
                    }
                }.runTaskLater(Foxtrot.getInstance(), 100);
                noDamage.put(player.getName(), true);
            }
        }

    }

    
}
