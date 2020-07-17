package net.frozenorb.foxtrot.abilities.type;

import com.google.common.collect.Lists;
import net.frozenorb.foxtrot.Foxtrot;
import net.frozenorb.foxtrot.abilities.AbstractAbility;
import net.frozenorb.foxtrot.team.Team;
import net.frozenorb.foxtrot.team.dtr.DTRBitmask;
import net.frozenorb.qlib.util.ItemBuilder;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class RotateAbility extends AbstractAbility {
    @Override
    public String getId() {
        return "ROTATE_STICK";
    }

    @Override
    public long getCooldown() {
        return TimeUnit.SECONDS.toMillis(90L);
    }

    @Override
    public int getMaxUses() {
        return -1;
    }

    @Override
    public ItemStack getItem() {
        return ItemBuilder
                .of(Material.STICK)
                .build();
    }

    @Override
    public String getDisplayName() {
        return ChatColor.RED.toString() + ChatColor.BOLD + "Rotate Stick";
    }

    @Override
    public String getScoreboardName() {
        return getDisplayName();
    }

    @Override
    public ChatColor getColor() {
        return ChatColor.RED;
    }

    @Override
    public List<String> getDescription() {
        return Lists.newArrayList(
                ChatColor.GRAY + "Punch a player to",
                ChatColor.GRAY + "rotate them by 180 degrees."
        );
    }
    @EventHandler
    public void onAttack(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player)) return;
        if (!(event.getEntity() instanceof Player)) return;

        Player victim = (Player) event.getEntity();
        Player attacker = (Player) event.getDamager();

        ItemStack itemInHand = attacker.getItemInHand();
        if (itemInHand == null || !isSimilar(itemInHand, false)) return;

        if (DTRBitmask.SAFE_ZONE.appliesAt(attacker.getLocation())) {
            attacker.sendMessage(ChatColor.RED + "You can't do this is in a Safe-Zone!");
            event.setCancelled(true);
            return;
        }

        if (DTRBitmask.SAFE_ZONE.appliesAt(victim.getLocation())) {
            attacker.sendMessage(ChatColor.RED + "This player is in a Safe-Zone!");
            event.setCancelled(true);
            return;
        }

        Team team = Foxtrot.getInstance().getTeamHandler().getTeam(attacker);
        if (team != null && team.isMember(victim.getUniqueId())) {
            attacker.sendMessage(ChatColor.RED + "You cannot do this to your teammate!");
            return;
        }

        if (!useAbility(attacker)) return;
        removeOne(attacker);

        Location loc = victim.getLocation();
        loc.setYaw(loc.getYaw() + 180);
        victim.teleport(loc);

        attacker.sendMessage(ChatColor.YELLOW + "You rotated " + ChatColor.RED + victim.getDisplayName() + ChatColor.YELLOW + "!");
        victim.sendMessage(ChatColor.YELLOW + "You have been rotated by " + ChatColor.RED + attacker.getDisplayName() + ChatColor.YELLOW + "!");

        event.setDamage(0);
    }
}