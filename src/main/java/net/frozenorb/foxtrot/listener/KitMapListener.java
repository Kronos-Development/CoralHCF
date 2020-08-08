package net.frozenorb.foxtrot.listener;

import net.frozenorb.foxtrot.Foxtrot;
import net.frozenorb.foxtrot.server.SpawnTagHandler;
import net.frozenorb.foxtrot.team.Team;
import net.frozenorb.foxtrot.team.claims.LandBoard;
import net.frozenorb.foxtrot.team.dtr.DTRBitmask;
import net.frozenorb.foxtrot.util.Players;
import net.frozenorb.qlib.economy.FrozenEconomyHandler;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World.Environment;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

public class KitMapListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerDeath(PlayerDeathEvent e) {
        if (Foxtrot.getInstance().getMapHandler().getScoreboardTitle().contains("cane")) {
            return;
        }
        
        Player victim = e.getEntity();

        // 1. killer should not be null
        // 2. victim should not be equal to killer
        // 3. victim should not be naked
        if (victim.getKiller() != null && !victim.getUniqueId().equals(victim.getKiller().getUniqueId()) && !Players.isNaked(victim)) {
            String killerName = victim.getKiller().getName();
            FrozenEconomyHandler.deposit(victim.getKiller().getUniqueId(), 100 + getAdditional(victim.getKiller()));
            victim.getKiller().sendMessage(ChatColor.RED + "You received a reward for killing " + ChatColor.GREEN
                    + victim.getName() + ChatColor.RED + ".");
        }
    }
    
    private int getAdditional(Player killer) {
        if (killer.hasPermission("foxtrot.killreward.ghoul")) {
            return 5;
        } else if (killer.hasPermission("foxtrot.killreward.poltergeist")) {
            return 5;
        } else if (killer.hasPermission("foxtrot.killreward.sorcerer")) {
            return 10;
        } else if (killer.hasPermission("foxtrot.killreward.suprive")) {
            return 25;
        } else if (killer.hasPermission("foxtrot.killreward.juggernaut")) {
            return 50;
        } else if (killer.hasPermission("foxtrot.killreward.myth")) {
            return 75;
        } else if (killer.hasPermission("foxtrot.killreward.sapphire")) {
            return 100;
        } else if (killer.hasPermission("foxtrot.killreward.pearl")) {
            return 125;
        } else if (killer.hasPermission("foxtrot.killreward.ruby")) {
            return 150;
        } else if (killer.hasPermission("foxtrot.killreward.velt")) {
            return 175;
        } else if (killer.hasPermission("foxtrot.killreward.velt-plus")) {
            return 200;
        } else {
            return 0;
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onProjectileHit(ProjectileHitEvent event) {
        Bukkit.getScheduler().runTaskLater(Foxtrot.getInstance(), event.getEntity()::remove, 1L);
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onProjectileLaunch(ProjectileLaunchEvent event) {
        Team team = LandBoard.getInstance().getTeam(event.getEntity().getLocation());
        if (team != null && event.getEntity() instanceof Arrow && team.hasDTRBitmask(DTRBitmask.SAFE_ZONE)) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onCommand(PlayerCommandPreprocessEvent event) {
        String command = event.getMessage().toLowerCase();
        if (command.startsWith("/pv")
                || command.startsWith("/playervault")
                || command.startsWith("pv")
                || command.startsWith("playervaults")
                || command.startsWith("/vault")
                || command.startsWith("vault")
                || command.startsWith("vc")
                || command.startsWith("/vc")) {
            if (SpawnTagHandler.isTagged(event.getPlayer())) {
                event.setCancelled(true);
                event.getPlayer().sendMessage(ChatColor.RED + "You can't /pv in combat.");
            }
        }
    }
    
    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Foxtrot.getInstance().getMapHandler().getKitManager().logout(event.getPlayer().getUniqueId());
    }
    
    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Foxtrot.getInstance().getMapHandler().getKitManager().loadKits(event.getPlayer().getUniqueId());
    }
    
    @EventHandler(ignoreCancelled = true)
    public void onPortal(PlayerPortalEvent event) {
        if (event.getCause() != TeleportCause.NETHER_PORTAL) {
            return;
        }
        
        if (event.getTo().getWorld().getEnvironment() != Environment.NETHER) {
            return;
        }
        
        event.setTo(event.getTo().getWorld().getSpawnLocation().clone());
    }
}
