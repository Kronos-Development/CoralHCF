package net.frozenorb.foxtrot.map.deathban;

import net.frozenorb.foxtrot.server.EnderpearlCooldownHandler;
import net.frozenorb.foxtrot.team.dtr.DTRBitmask;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;

import net.frozenorb.foxtrot.Foxtrot;
import net.frozenorb.foxtrot.commands.LastInvCommand;
import net.frozenorb.qlib.util.TimeUtils;

public class DeathbanListener implements Listener {

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        LastInvCommand.recordInventory(event.getEntity());

        EnderpearlCooldownHandler.getEnderpearlCooldown().remove(event.getEntity().getName()); // cancel enderpearls

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

        new BukkitRunnable() {

            public void run() {
                if (!event.getEntity().isOnline()) {
                    return;
                }

                if (Foxtrot.getInstance().getServerHandler().isPreEOTW()) {
                    event.getEntity().kickPlayer(ChatColor.YELLOW + "Come back tomorrow for SOTW!");
                } else {
                    event.getEntity().kickPlayer(ChatColor.YELLOW + "Come back in " + time + "!");
                }
            }

        }.runTaskLater(Foxtrot.getInstance(), 10 * 20L);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        if (!Foxtrot.getInstance().getDeathbanMap().isDeathbanned(event.getPlayer().getUniqueId())) {
            return;
        }

        Player player = event.getPlayer();
        boolean shouldBypass = player.isOp() || player.hasPermission("zoot.staff");
        boolean isPowers = Foxtrot.getInstance().getConfig().getBoolean("powers");

        if (shouldBypass) {
            Foxtrot.getInstance().getDeathbanMap().revive(player.getUniqueId());
            return;
        }
        if (isPowers) {
            if (!Foxtrot.getInstance().getServerHandler().isPreEOTW() || !Foxtrot.getInstance().getServerHandler().isEOTW()) return;
        }

        long unbannedOn = Foxtrot.getInstance().getDeathbanMap().getDeathban(event.getPlayer().getUniqueId());
        long left = unbannedOn - System.currentTimeMillis();
        String time = TimeUtils.formatIntoDetailedString((int) left / 1000);

        if (Foxtrot.getInstance().getServerHandler().isPreEOTW()) {
            player.sendMessage(ChatColor.RED + "Come back tomorrow for SOTW.");
            return;
        }

        int soulbound = Foxtrot.getInstance().getSoulboundLivesMap().getLives(player.getUniqueId());

        if (soulbound < 1) {
            player.kickPlayer(ChatColor.RED + "You are currently deathbanned, check back in " + ChatColor.BOLD + time + ChatColor.RED + "!");
            return;
        }

        Foxtrot.getInstance().getSoulboundLivesMap().setLives(player.getUniqueId(), soulbound - 1);
        Foxtrot.getInstance().getDeathbanMap().revive(player.getUniqueId());

        player.spigot().respawn();
        player.sendMessage(ChatColor.GREEN + "You have used a life to revive yourself. You now have " + (soulbound - 1) + " lives remaining.");
    }

}