package net.frozenorb.foxtrot.abilities.type;

import com.google.common.collect.Lists;
import com.minexd.zoot.util.CC;
import net.frozenorb.foxtrot.Foxtrot;
import net.frozenorb.foxtrot.abilities.AbstractAbility;
import net.frozenorb.foxtrot.team.Team;
import net.frozenorb.foxtrot.team.dtr.DTRBitmask;
import net.frozenorb.qlib.util.ItemBuilder;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Egg;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class SwitcherAbility extends AbstractAbility {
    @Override
    public String getId() {
        return "SWITCHER";
    }

    @Override
    public long getCooldown() {
        return TimeUnit.SECONDS.toMillis(60L);
    }

    @Override
    public int getMaxUses() {
        return -1;
    }

    @Override
    public ItemStack getItem() {
        return ItemBuilder
                .of(Material.EGG)
                .build();
    }

    @Override
    public String getDisplayName() {
        return ChatColor.WHITE.toString() + ChatColor.BOLD + "Dinkay's Switchers";
    }

    @Override
    public String getScoreboardName() {
        return ChatColor.WHITE.toString() + ChatColor.BOLD + "Switcher";
    }

    @Override
    public ChatColor getColor() {
        return ChatColor.WHITE;
    }

    @Override
    public List<String> getDescription() {
        return Lists.newArrayList(
                ChatColor.GRAY + "Throw this at a player",
                ChatColor.GRAY + "and switch places."
        );
    }

    @EventHandler
    public void onLaunch(ProjectileLaunchEvent event) {
        if (!(event.getEntity() instanceof Egg)) return;

        Egg egg = (Egg) event.getEntity();
        if (!(egg.getShooter() instanceof Player)) return;

        Player shooter = (Player) egg.getShooter();
        ItemStack itemInHand = shooter.getItemInHand();

        if (itemInHand == null || !isSimilar(itemInHand, false)) return;

        if (!checkCooldown(shooter)) {
            event.setCancelled(true);
            shooter.updateInventory();
            return;
        }

        //Checking if the shooter is in Safe-ZONE if so we cancel the ability
        if (DTRBitmask.SAFE_ZONE.appliesAt(shooter.getLocation())) {
            event.setCancelled(true);
            shooter.sendMessage(CC.translate("&c&lWARNING! &eYou can't do this is in a &aSafe-Zone&e!"));
            shooter.updateInventory();
            return;
        }

        //Checking if the shooter has PVP-Timer if so we cancel the ability
        if (Foxtrot.getInstance().getPvPTimerMap().hasTimer(shooter.getUniqueId())) {
            event.setCancelled(true);
            shooter.sendMessage(CC.translate("&c&lWARNING! &eyou can't do this while you have &aPVP Timer&e!"));
            shooter.updateInventory();
            return;
        }

        //Checking if the opponent has a PVP-TIMER if so we cancel the ability

        egg.setMetadata("switcher", new FixedMetadataValue(Foxtrot.getInstance(), true));
    }

    @EventHandler
    public void onAttack(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Egg)) return;
        if (!(event.getEntity() instanceof Player)) return;

        Egg egg = (Egg) event.getDamager();
        if (!egg.hasMetadata("switcher")) return;
        if (!(egg.getShooter() instanceof Player)) return;

        Player victim = (Player) event.getEntity();
        Player attacker = (Player) egg.getShooter();

        Location victimLoc = victim.getLocation();
        Location attackerLoc = attacker.getLocation();

        if (DTRBitmask.SAFE_ZONE.appliesAt(attackerLoc)) {
            attacker.sendMessage(ChatColor.RED + "You can't do this is in a Safe-Zone!");
            return;
        }

        if (DTRBitmask.SAFE_ZONE.appliesAt(victimLoc)) {
            attacker.sendMessage(ChatColor.RED + "This player is in a Safe-Zone!");
            return;
        }

        if (Foxtrot.getInstance().getPvPTimerMap().hasTimer(attacker.getUniqueId())) {
            attacker.sendMessage(ChatColor.RED + "You can't do this while you have PVP Timer!");
            return;
        }

        if (Foxtrot.getInstance().getPvPTimerMap().hasTimer(victim.getUniqueId())) {
            attacker.sendMessage(ChatColor.RED + "This player currently has their PVP Timer!");
            return;
        }

        Team team = Foxtrot.getInstance().getTeamHandler().getTeam(attacker);

        double distance = victimLoc.distance(attackerLoc);

        if (distance > 15) {
            attacker.sendMessage(ChatColor.RED + "You are too far from " + ChatColor.YELLOW + victim.getDisplayName() + ChatColor.RED + " to do this!");
            return;
        }

        if (!useAbility(attacker)) return;

        victim.teleport(attackerLoc);
        attacker.teleport(victimLoc);

        attacker.sendMessage(ChatColor.YELLOW + "You swapped places with " + ChatColor.RED + victim.getDisplayName() + ChatColor.YELLOW + "!");
        victim.sendMessage(ChatColor.YELLOW + "You swapped places with " + ChatColor.RED + attacker.getDisplayName() + ChatColor.YELLOW + "!");

        event.setDamage(0);
    }
}