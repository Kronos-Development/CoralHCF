package net.frozenorb.foxtrot.map.deathban.purgatory;

import net.frozenorb.foxtrot.Foxtrot;
import net.frozenorb.foxtrot.commands.LastInvCommand;
import net.frozenorb.foxtrot.server.EnderpearlCooldownHandler;
import net.frozenorb.foxtrot.team.dtr.DTRBitmask;
import net.frozenorb.qlib.util.TimeUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.*;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

public class PurgatoryListener implements Listener {

    private List<String> reviveConfirmation = new ArrayList<>();

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getClickedBlock() == null || !(event.getClickedBlock().getState() instanceof Sign)) {
            return;
        }

        Player player = event.getPlayer();

        if (Foxtrot.getInstance().getMapHandler().isKitMap()) {
            return;
        }

        if (Foxtrot.getInstance().getInDuelPredicate().test(event.getPlayer())) {
            return;
        }

        if (Foxtrot.getInstance().getServerHandler().isVeltKitMap()) {
            return;
        }

        if (!Foxtrot.getInstance().getDeathbanMap().isDeathbanned(event.getPlayer().getUniqueId())) {
            return;
        }

        Sign sign = (Sign) event.getClickedBlock().getState();

        if (!sign.getLine(2).toLowerCase().contains("use a life") || !sign.getLine(1).toLowerCase().contains("use a life")) {
            return;
        }

        int soulbound = Foxtrot.getInstance().getSoulboundLivesMap().getLives(player.getUniqueId());

        if (soulbound < 1) {
            player.sendMessage(ChatColor.RED + "You do not have any lives!");
            return;
        }

        if (!reviveConfirmation.contains(player.getName())) {
            player.sendMessage(ChatColor.YELLOW + "Are you sure you want to use a life? If yes click the sign again.");
            player.sendMessage(ChatColor.YELLOW + "You will have " + (soulbound - 1) + " lives remaining.");
            reviveConfirmation.add(player.getName());
            return;
        }
        reviveConfirmation.remove(player.getName());

        Foxtrot.getInstance().getSoulboundLivesMap().setLives(player.getUniqueId(), soulbound - 1);
        Foxtrot.getInstance().getDeathbanMap().revive(player.getUniqueId());
        player.sendMessage(ChatColor.GREEN + "You have used a life to revive yourself. You now have " + (soulbound - 1) + " lives remaining.");
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event){
        if(Foxtrot.getInstance().getDeathbanMap().isDeathbanned(event.getEntity().getUniqueId())) {
            if (event.getEntity() instanceof Player) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        LastInvCommand.recordInventory(event.getEntity());

        EnderpearlCooldownHandler.getEnderpearlCooldown().remove(event.getEntity().getName());

        if (Foxtrot.getInstance().getMapHandler().isKitMap()) {
            return;
        }

        if (Foxtrot.getInstance().getInDuelPredicate().test(event.getEntity())) {
            return;
        }

        if (Foxtrot.getInstance().getServerHandler().isVeltKitMap()) {
            return;
        }

        int seconds = (int) Foxtrot.getInstance().getServerHandler().getDeathban(event.getEntity());
        Foxtrot.getInstance().getDeathbanMap().deathban(event.getEntity().getUniqueId(), seconds);

        String time = TimeUtils.formatIntoDetailedString(seconds);

        if (Foxtrot.getInstance().getServerHandler().isPreEOTW()) {
            event.getEntity().kickPlayer(ChatColor.RED + "Come back tomorrow for SOTW!");
            return;
        }

        Bukkit.getScheduler().runTaskLater(Foxtrot.getInstance(), () -> event.getEntity().spigot().respawn(), 5);

        boolean shouldBypass = event.getEntity().isOp();

        if (!shouldBypass)
            shouldBypass = event.getEntity().hasPermission("foxtrot.staff");

        if (shouldBypass) {
            Foxtrot.getInstance().getDeathbanMap().revive(event.getEntity().getUniqueId());
        } else {
            event.getEntity().sendMessage(ChatColor.RED + "You have been deathbanned for " + ChatColor.BOLD + time + ChatColor.RED + "!");
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();

        if (Foxtrot.getInstance().getMapHandler().isKitMap()) {
            return;
        }

        if (Foxtrot.getInstance().getInDuelPredicate().test(event.getPlayer())) {
            return;
        }

        if (Foxtrot.getInstance().getServerHandler().isVeltKitMap()) {
            return;
        }

        if (!Foxtrot.getInstance().getDeathbanMap().isDeathbanned(event.getPlayer().getUniqueId())) {
            return;
        }

        event.setCancelled(true);

        PurgatoryHandler purgatoryHandler = Foxtrot.getInstance().getMapHandler().getPurgatoryHandler();

        int price = purgatoryHandler.getPriceMap().getOrDefault(event.getBlock().getType(), 0);
        if (price > 0) {
            Material material = event.getBlock().getType();
            Vector location = event.getBlock().getLocation().toVector();

            if (purgatoryHandler.getMineMap().containsKey(location)) {
                event.getBlock().setType(Material.COBBLESTONE);
                return;
            }

            purgatoryHandler.getMineMap().put(location, material);
            event.getBlock().setType(Material.COBBLESTONE);

            new BukkitRunnable() {
                @Override
                public void run() {
                    event.getBlock().setType(purgatoryHandler.getMineMap().remove(location));
                }
            }.runTaskLater(Foxtrot.getInstance(), 20L * 5L);

            Foxtrot.getInstance().getDeathbanMap().reduce(player.getUniqueId(), price);

            if (!Foxtrot.getInstance().getDeathbanMap().isDeathbanned(player.getUniqueId()))
                Foxtrot.getInstance().getMapHandler().getPurgatoryHandler().withdrawFromPurgatory(player, false);
        }
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        if (Foxtrot.getInstance().getMapHandler().isKitMap()) {
            return;
        }

        if (Foxtrot.getInstance().getInDuelPredicate().test(event.getPlayer())) {
            return;
        }

        if (Foxtrot.getInstance().getServerHandler().isVeltKitMap()) {
            return;
        }

        if (!Foxtrot.getInstance().getDeathbanMap().isDeathbanned(event.getPlayer().getUniqueId())) {
            return;
        }

        boolean shouldBypass = event.getPlayer().isOp();

        if (!shouldBypass)
            shouldBypass = event.getPlayer().hasPermission("foxtrot.staff");

        if (shouldBypass) {
            event.setRespawnLocation(Foxtrot.getInstance().getServerHandler().getSpawnLocation());
            event.getPlayer().sendMessage(ChatColor.YELLOW + "You would have been deathbanned, but you have permissions to bypass.");
            return;
        }

        event.setRespawnLocation(Foxtrot.getInstance().getMapHandler().getPurgatoryHandler().getPurgatoryLocation());

        new BukkitRunnable() {
            @Override
            public void run() {
                if (!event.getPlayer().isOnline())
                    return;

                event.getPlayer().teleport(Foxtrot.getInstance().getMapHandler().getPurgatoryHandler().getPurgatoryLocation());
                Foxtrot.getInstance().getMapHandler().getPurgatoryHandler().addToPurgatory(event.getPlayer());
            }
        }.runTaskLater(Foxtrot.getInstance(), 5L);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        //Foxtrot.getInstance().getMapHandler().getPurgatoryHandler().getBanCache().remove(event.getPlayer().getName());
        reviveConfirmation.remove(event.getPlayer().getName());
    }

    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent event) {
        if (Foxtrot.getInstance().getMapHandler().isKitMap()) {
            return;
        }

        if (Foxtrot.getInstance().getInDuelPredicate().test(event.getPlayer())) {
            return;
        }

        if (Foxtrot.getInstance().getServerHandler().isVeltKitMap()) {
            return;
        }

        if (!Foxtrot.getInstance().getDeathbanMap().isDeathbanned(event.getPlayer().getUniqueId())) {
            return;
        }

        event.setCancelled(true);
        event.getPlayer().updateInventory();
    }

    @EventHandler
    public void onPlayerPickupItem(PlayerPickupItemEvent event) {
        if (Foxtrot.getInstance().getMapHandler().isKitMap()) {
            return;
        }

        if (Foxtrot.getInstance().getInDuelPredicate().test(event.getPlayer())) {
            return;
        }

        if (Foxtrot.getInstance().getServerHandler().isVeltKitMap()) {
            return;
        }

        if (!Foxtrot.getInstance().getMapHandler().isPurgatory()) {
            return;
        }

        if (event.getItem().getItemStack().isSimilar(Foxtrot.getInstance().getMapHandler().getPurgatoryHandler().getPickaxe())) {
            event.setCancelled(true);
            event.getItem().remove();
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        if (!Foxtrot.getInstance().getDeathbanMap().isDeathbanned(event.getPlayer().getUniqueId())) {
            if (Foxtrot.getInstance().getMapHandler().getPurgatoryHandler().getBanCache().getOrDefault(event.getPlayer().getName(), false)) {
                Foxtrot.getInstance().getMapHandler().getPurgatoryHandler().withdrawFromPurgatory(event.getPlayer(), false);
            }
            return;
        }

        Player player = event.getPlayer();
        boolean shouldBypass = player.isOp() || player.hasPermission("foxtrot.staff");

        if (shouldBypass) {
            Foxtrot.getInstance().getDeathbanMap().revive(event.getPlayer().getUniqueId());
        } else {
            Foxtrot.getInstance().getMapHandler().getPurgatoryHandler().addToPurgatory(event.getPlayer());
        }
    }

}