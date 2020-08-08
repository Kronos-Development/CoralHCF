package net.frozenorb.foxtrot.abilities.type;

import com.google.common.collect.Lists;
import com.minexd.zoot.util.CC;
import net.frozenorb.foxtrot.abilities.AbstractAbility;
import net.frozenorb.qlib.util.ItemBuilder;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShootingAbility extends AbstractAbility {
    @Override
    public String getId() {
        return "MENTORN";
    }

    @Override
    public long getCooldown() {
        return 60;
    }

    @Override
    public int getMaxUses() {
        return -1;
    }

    @Override
    public ItemStack getItem() {
        return ItemBuilder.of(Material.LEASH).build();
    }

    @Override
    public String getDisplayName() {
        return ChatColor.DARK_AQUA + ChatColor.BOLD.toString() + "Mentorn's Firecracker";
    }

    @Override
    public String getScoreboardName() {
        return ChatColor.DARK_AQUA + ChatColor.BOLD.toString() + "Mentorn's Firecracker";
    }

    @Override
    public ChatColor getColor() {
        return ChatColor.DARK_AQUA;
    }

    @Override
    public List<String> getDescription() {
        return Lists.newArrayList(
                CC.translate("&7Launch yourself"),
                CC.translate("&7in the direction you're facing")
        );
    }

    private Map<String, Boolean> noDamage = new HashMap<>();

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player) || !(event.getCause() == EntityDamageEvent.DamageCause.FALL)) return;

        Player player = (Player) event.getEntity();
        if (!noDamage.containsKey(player.getName()) || !noDamage.get(player.getName())) return;
        event.setCancelled(true);
        noDamage.remove(player.getName());

        if (isOnCooldown(player.getUniqueId()))
            event.setCancelled(true);
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack itemInHand = player.getItemInHand();

        if (itemInHand == null || !isSimilar(itemInHand, false)) return;

        if (!useAbility(player)){
            return;
        }

        player.setVelocity(player.getLocation().getDirection().multiply(2.5).setY(1));
        noDamage.put(player.getName(), true);
        removeOne(player);
        addUse(player);
    }
}
