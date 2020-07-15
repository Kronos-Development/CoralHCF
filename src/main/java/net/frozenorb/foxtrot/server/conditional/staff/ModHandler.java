package net.frozenorb.foxtrot.server.conditional.staff;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.frozenorb.foxtrot.Foxtrot;
import net.frozenorb.foxtrot.server.cheatbreaker.CBAPIHook;
import net.frozenorb.qlib.nametag.FrozenNametagHandler;
import net.frozenorb.qlib.visibility.FrozenVisibilityHandler;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ModHandler {

    private static Map<UUID, ModCache> modCacheMap = new HashMap<>();

    public static boolean isModMode(Player player) {
        return player.hasMetadata("modmode");
    }

    public static boolean isVanished(Player player) {
        return player.hasMetadata("invisible");
    }

    public static boolean toggleModMode(Player player) {
        return toggleModMode(player, false);
    }

    public static boolean toggleModMode(Player player, boolean silent) {
        boolean newState = !isModMode(player);

        if (!silent)
            player.sendMessage(ChatColor.GOLD + "Mod Mode: " + (newState ? (ChatColor.GREEN + "Enabled") : (ChatColor.RED + "Disabled")));

        if (newState) {
            player.setMetadata("modmode", new FixedMetadataValue(Foxtrot.getInstance(), true));

            ModCache cache = new ModCache(player);
            modCacheMap.put(player.getUniqueId(), cache);

            player.getInventory().clear();
            player.getInventory().setArmorContents(null);

            if (player.hasPermission("foxtrot.gamemode"))
                player.setGameMode(GameMode.CREATIVE);
            else {
                player.setGameMode(GameMode.SURVIVAL);
                player.setAllowFlight(true);
                player.setFlying(true);
            }

            setVanished(player, true);

            player.getInventory().setItem(0, StaffItems.COMPASS);
            player.getInventory().setItem(1, StaffItems.INSPECT_BOOK);

            if (player.hasPermission("worldedit.wand")) {
                player.getInventory().setItem(2, StaffItems.WAND);
                player.getInventory().setItem(3, StaffItems.CARPET);
            }
            else
                player.getInventory().setItem(2, StaffItems.CARPET);

            player.getInventory().setItem(8, StaffItems.GO_VIS);

            CBAPIHook.giveAllStaffModules(player);
        } else {
            player.removeMetadata("modmode", Foxtrot.getInstance());
            setVanished(player, false);

            ModCache cache = modCacheMap.remove(player.getUniqueId());
            if (cache != null)
                cache.restore(player);

            if (player.getGameMode() != GameMode.CREATIVE)
                player.setAllowFlight(false);

            CBAPIHook.disableAllStaffModules(player);
        }

        player.updateInventory();
        FrozenNametagHandler.reloadPlayer(player);
        FrozenVisibilityHandler.update(player);

        return newState;
    }

    public static void enableModMode(Player player) {
        if (isModMode(player)) return;
        toggleModMode(player);
    }

    public static void disableModMode(Player player) {
        if (!isModMode(player)) return;
        toggleModMode(player);
    }

    public static void setVanished(Player player, boolean state) {
        if (state)
            player.setMetadata("invisible", new FixedMetadataValue(Foxtrot.getInstance(), true));
        else
            player.removeMetadata("invisible", Foxtrot.getInstance());

        if (isModMode(player))
            player.getInventory().setItem(8, state ? StaffItems.GO_VIS : StaffItems.GO_INVIS);

        player.spigot().setCollidesWithEntities(!state);
        player.updateInventory();

        FrozenNametagHandler.reloadPlayer(player);
        FrozenVisibilityHandler.update(player);
    }

    @Getter
    @AllArgsConstructor
    protected static class ModCache {
        private ItemStack[] inventory;
        private ItemStack[] armor;
        private GameMode gameMode;

        public ModCache(Player player) {
            this.inventory = player.getInventory().getContents();
            this.armor = player.getInventory().getArmorContents();
            this.gameMode = player.getGameMode();
        }

        public void restore(Player player) {
            player.getInventory().setContents(inventory);
            player.getInventory().setArmorContents(armor);
            player.setGameMode(gameMode);
        }
    }

    public static class StaffItems {
        public static ItemStack COMPASS = build(Material.COMPASS, ChatColor.AQUA + "Compass");
        public static ItemStack INSPECT_BOOK = build(Material.BOOK, ChatColor.AQUA + "Inspection Book");
        public static ItemStack WAND = build(Material.WOOD_AXE, ChatColor.AQUA + "WorldEdit Wand");

        public static ItemStack GO_VIS = build(Material.INK_SACK, 1, DyeColor.GRAY.getDyeData(), ChatColor.AQUA + "Become Visible");
        public static ItemStack GO_INVIS = build(Material.INK_SACK, 1, DyeColor.LIME.getDyeData(), ChatColor.AQUA + "Become Invisible");

        public static ItemStack CARPET = build(Material.CARPET, 1, DyeColor.CYAN.getDyeData(), " ");

        public static ItemStack build(Material type, String displayName) {
            return build(type, 1, (byte)0, displayName);
        }

        public static ItemStack build(Material type, int amount, byte data, String displayName) {
            ItemStack stack = new ItemStack(type, amount, (short)1, data);
            ItemMeta meta = stack.getItemMeta();
            meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', displayName));
            stack.setItemMeta(meta);
            return stack;
        }
    }

}