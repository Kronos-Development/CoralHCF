package net.frozenorb.foxtrot.abilities.type;

import com.google.common.collect.Lists;
import net.frozenorb.foxtrot.Foxtrot;
import net.frozenorb.foxtrot.abilities.AbstractAbility;
import net.frozenorb.foxtrot.team.Team;
import net.frozenorb.foxtrot.team.dtr.DTRBitmask;
import net.frozenorb.qlib.util.ItemBuilder;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class RocketAbility extends AbstractAbility {
    @Override
    public String getId() {
        return "ROCKET";
    }

    @Override
    public long getCooldown() {
        return TimeUnit.SECONDS.toMillis(30L);
    }

    @Override
    public int getMaxUses() {
        return -1;
    }

    @Override
    public ItemStack getItem() {
        return ItemBuilder
                .of(Material.FIREWORK)
                .build();
    }

    @Override
    public String getDisplayName() {
        return ChatColor.LIGHT_PURPLE.toString() + ChatColor.BOLD + "Rocket";
    }

    @Override
    public ChatColor getColor() {
        return ChatColor.LIGHT_PURPLE;
    }

    @Override
    public List<String> getDescription() {
        return Lists.newArrayList(
                ChatColor.GRAY + "Use this to push everyone",
                ChatColor.GRAY + "in the air around you."
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

        for (Player nearby : getNearbyPlayers(player)) {
            nearby.setVelocity(new Vector(0, 1.2, 0));
            nearby.sendMessage(ChatColor.YELLOW + "You were pushed in the air by " + ChatColor.RED + player.getDisplayName() + ChatColor.YELLOW + "!");
        }
    }

    public List<Player> getNearbyPlayers(Player player) {
        List<Player> valid = new ArrayList<>();
        Team sourceTeam = Foxtrot.getInstance().getTeamHandler().getTeam(player);

        // We divide by 2 so that the range isn't as much on the Y level (and can't be abused by standing on top of / under events)
        for (Entity entity : player.getNearbyEntities(4, 2, 4)) {
            if (entity instanceof Player) {
                Player nearbyPlayer = (Player) entity;

                if (DTRBitmask.SAFE_ZONE.appliesAt(nearbyPlayer.getLocation()))
                    continue;

                if (Foxtrot.getInstance().getPvPTimerMap().hasTimer(nearbyPlayer.getUniqueId()))
                    continue;

                valid.add(nearbyPlayer);
            }
        }

        valid.add(player);
        return (valid);
    }

}