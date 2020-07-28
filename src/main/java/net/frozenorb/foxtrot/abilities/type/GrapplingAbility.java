package net.frozenorb.foxtrot.abilities.type;

import com.google.common.collect.Lists;
import net.frozenorb.foxtrot.abilities.AbstractAbility;
import net.frozenorb.foxtrot.pvpclasses.PvPClassHandler;
import net.frozenorb.foxtrot.pvpclasses.pvpclasses.ScoutClass;
import net.frozenorb.qlib.util.ItemBuilder;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class GrapplingAbility extends AbstractAbility {
    @Override
    public String getId() {
        return "GRAPPLING_HOOK";
    }

    @Override
    public long getCooldown() {
        return TimeUnit.SECONDS.toMillis(10L);
    }

    @Override
    public int getMaxUses() {
        return 15;
    }

    @Override
    public ItemStack getItem() {
        return ItemBuilder
                .of(Material.FISHING_ROD)
                .build();
    }

    @Override
    public String getDisplayName() {
        return ChatColor.GOLD.toString() + ChatColor.BOLD + "Grappling Hook";
    }

    @Override
    public String getScoreboardName() {
        return getDisplayName();
    }

    @Override
    public ChatColor getColor() {
        return ChatColor.GOLD;
    }

    @Override
    public List<String> getDescription() {
        return Lists.newArrayList(
                ChatColor.GRAY + "Use this to push yourself",
                ChatColor.GRAY + "or pull someone towards you."
        );
    }

    private Map<String, Boolean> noDamage = new HashMap<>();

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player) || !(event.getCause() == EntityDamageEvent.DamageCause.FALL)) return;

        Player player = (Player) event.getEntity();
        if (!noDamage.containsKey(player.getName()) || !noDamage.get(player.getName())) return;
        noDamage.remove(player.getName());

        if (isOnCooldown(player.getUniqueId()))
            event.setCancelled(true);
    }

    @EventHandler
    public void onPlayerFish(PlayerFishEvent event) {
        if (event.getState() == PlayerFishEvent.State.CAUGHT_FISH || event.getState() == PlayerFishEvent.State.FISHING) return;

        Player player = event.getPlayer();
        ItemStack itemInHand = player.getItemInHand();

        if (itemInHand == null || !isSimilar(itemInHand, true)) return;
        if (PvPClassHandler.getPvPClass(player) instanceof ScoutClass) return;
        if (!useAbility(player)) return;

        Location hook = event.getHook().getLocation();

        switch (event.getState()) {
            case IN_GROUND: {
                if (event.getHook().getLocation().distance(player.getLocation()) > 30) {
                    player.sendMessage(ChatColor.RED + "You are too far from the hook!");
                    return;
                }

                pullTo(player, hook, true);
                //player.setVelocity(getVector(player, hook));
                break;
            }

            case CAUGHT_ENTITY: {
                if (!(event.getCaught() instanceof Player)) return;
                Player caught = (Player) event.getCaught();

                if (caught.getLocation().distance(player.getLocation()) > 30) {
                    player.sendMessage(ChatColor.RED + "You are too far from " + ChatColor.YELLOW + caught.getDisplayName() + ChatColor.RED + " to do this!");
                    return;
                }

                pullTo(caught, player.getLocation(), true);
                //caught.setVelocity(getVector(caught, player.getLocation()));
                break;
            }
        }

        if(event.getState() != PlayerFishEvent.State.FAILED_ATTEMPT) {
            noDamage.put(player.getName(), true);
            int remaining = addUse(player);

            if (remaining <= 0) {
                removeOne(player);
                player.playSound(player.getLocation(), Sound.ITEM_BREAK, 10f, 1f);
            } else if (getMaxUses() > 0) {
                player.sendMessage(ChatColor.YELLOW + "This ability now has " + ChatColor.GREEN + remaining + " use" + (remaining > 1 ? "s" : "") + ChatColor.YELLOW + " left.");
            }
        }
    }

    private void pullTo(Entity e, Location loc, boolean pull) {
        Location l = e.getLocation();

        if (l.distanceSquared(loc) < 9) {
            if (loc.getY() > l.getY()) {
                e.setVelocity(new Vector(0, 0.25, 0));
                return;
            }
            Vector v = loc.toVector().subtract(l.toVector());
            e.setVelocity(v);
            return;
        }

        double d = loc.distance(l);
        double g = -0.08;
        double x = ((pull ? 1.0 : 3.0) + 0.07 * d) * (loc.getX() - l.getX()) / d;
        double y = (1.0 + 0.03 * d) * (loc.getY() - l.getY()) / d - 0.5 * g * d;
        double z = (1.0 + 0.07 * d) * (loc.getZ() - l.getZ()) / d;

        Vector v = e.getVelocity();
        v.setX(x);
        v.setY(y);
        v.setZ(z);
        e.setVelocity(v);
    }

    /*public Vector getVector(Entity entity, Location target) {
        Location entityLoc = entity.getLocation();
        double distance = target.distance(entityLoc);
        double gravity = -0.08;

        return entity.getVelocity()
                .clone()
                .setX((1.0+0.07*distance) * (target.getX() - entityLoc.getX())/distance)
                .setY((1.0+0.07*distance) * (target.getY() - entityLoc.getY())/distance - 0.525*gravity*distance)
                .setZ((1.0+0.07*distance) * (target.getZ() - entityLoc.getZ())/distance).multiply(1.15);
    }*/

}