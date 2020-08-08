package net.frozenorb.foxtrot.map.deathban.purgatory;

import com.minexd.zoot.util.CC;
import com.mongodb.BasicDBObject;
import com.mongodb.util.JSON;
import lombok.Getter;
import lombok.Setter;
import net.frozenorb.foxtrot.Foxtrot;
import net.frozenorb.foxtrot.persist.maps.DeathbanMap;
import net.frozenorb.foxtrot.util.MapUtil;
import net.frozenorb.qlib.util.ItemBuilder;
import net.minecraft.util.org.apache.commons.io.FileUtils;
import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class PurgatoryHandler {

    @Getter @Setter
    private Location purgatoryLocation;

    @Getter private Map<Material, Integer> priceMap;
    @Getter private Map<Vector, Material> mineMap;

    @Getter private Map<String, Boolean> banCache;

    @Getter private ItemStack pickaxe = ItemBuilder
            .of(Material.IRON_PICKAXE)
            .name(ChatColor.RED.toString() + ChatColor.BOLD + "Purgatory Pickaxe")
            .build();
    @Getter private ItemStack coralpick = ItemBuilder
            .of(Material.DIAMOND_PICKAXE)
            .name(ChatColor.LIGHT_PURPLE.toString() + ChatColor.BOLD + "Coral Pickaxe")
            .build();
    @Getter private ItemStack reefpick = ItemBuilder
            .of(Material.DIAMOND_PICKAXE)
            .name(ChatColor.YELLOW.toString() + ChatColor.BOLD + "Reef Pickaxe")
            .enchant(Enchantment.DIG_SPEED, 1)
            .build();
    @Getter private ItemStack mermaidpick = ItemBuilder
            .of(Material.DIAMOND_PICKAXE)
            .name(ChatColor.AQUA.toString() + ChatColor.BOLD + "Mermaid Pickaxe")
            .enchant(Enchantment.DIG_SPEED, 2)
            .build();
    @Getter private ItemStack neptunepick = ItemBuilder
            .of(Material.DIAMOND_PICKAXE)
            .name(ChatColor.DARK_AQUA.toString() + ChatColor.BOLD + "Neptune Pickaxe")
            .enchant(Enchantment.DIG_SPEED, 3)
            .build();
    @Getter private ItemStack poseidonpick = ItemBuilder
            .of(Material.DIAMOND_PICKAXE)
            .name(ChatColor.BLUE.toString() + ChatColor.BOLD + "Poseidon Pickaxe")
            .enchant(Enchantment.DIG_SPEED, 4)
            .build();
    @Getter private ItemStack vippick = ItemBuilder
            .of(Material.GOLD_PICKAXE)
            .name(ChatColor.BLUE.toString() + CC.translate("&e&k:&6&lVIP&e&k: &6&lPickaxe"))
            .enchant(Enchantment.DIG_SPEED, 5)
            .build();

    public PurgatoryHandler() {
        priceMap = new LinkedHashMap<>();
        mineMap = new HashMap<>();
        banCache = new HashMap<>();

        priceMap.put(Material.DIAMOND_ORE, 30);
        priceMap.put(Material.EMERALD_ORE, 25);
        priceMap.put(Material.GOLD_ORE, 15);
        priceMap.put(Material.IRON_ORE, 10);
        priceMap.put(Material.COAL_ORE, 5);

        priceMap = MapUtil.sortByValue(priceMap);

        try {
            File f = new File(Foxtrot.getInstance().getDataFolder(), "purgatory.json");

            if (!f.exists()) {
                f.createNewFile();
            }

            BasicDBObject dbo = (BasicDBObject) JSON.parse(FileUtils.readFileToString(f));

            if (dbo != null) {
                String world = dbo.getString("spawn.world");
                double x = dbo.getDouble("spawn.x");
                double y = dbo.getDouble("spawn.y");
                double z = dbo.getDouble("spawn.z");
                float pitch = (float) dbo.getDouble("spawn.pitch");
                float yaw = (float) dbo.getDouble("spawn.yaw");

                purgatoryLocation = new Location(Bukkit.getWorld(world), x, y, z, yaw, pitch);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (purgatoryLocation == null) {
            purgatoryLocation = Foxtrot.getInstance().getServerHandler().getSpawnLocation();
            save();
        }

        Foxtrot.getInstance().getServer().getPluginManager().registerEvents(new PurgatoryListener(), Foxtrot.getInstance());

        new BukkitRunnable() {
            @Override
            public void run() {
                DeathbanMap map = Foxtrot.getInstance().getDeathbanMap();

                for (Player online : Bukkit.getOnlinePlayers()) {
                    boolean previous = banCache.getOrDefault(online.getName(), false);
                    boolean now = map.isDeathbanned(online.getUniqueId());

                    banCache.put(online.getName(), now);

                    if (previous && !now)
                        withdrawFromPurgatory(online, false);
                }
            }
        }.runTaskTimer(Foxtrot.getInstance(), 20L, 20L);
    }

    private void resetBlocks() {
        synchronized (mineMap) {
            for (Map.Entry<Vector, Material> entry : mineMap.entrySet()) {
                int x = entry.getKey().getBlockX(), y = entry.getKey().getBlockY(), z = entry.getKey().getBlockZ();
                World world = purgatoryLocation.getWorld();
                world.getBlockAt(x, y, z).setType(entry.getValue());
            }

            mineMap.clear();
        }
    }

    public void save() {
        if (!Bukkit.isPrimaryThread())
            Foxtrot.getInstance().getServer().getScheduler().runTask(Foxtrot.getInstance(), this::resetBlocks);
        else
            resetBlocks();

        try {
            File f = new File(Foxtrot.getInstance().getDataFolder(), "purgatory.json");

            if (!f.exists()) {
                f.createNewFile();
            }

            BasicDBObject dbo = (BasicDBObject) JSON.parse(FileUtils.readFileToString(f));
            if (dbo == null) dbo = new BasicDBObject();

            dbo.append("spawn.world", purgatoryLocation.getWorld().getName());
            dbo.append("spawn.x", purgatoryLocation.getX());
            dbo.append("spawn.y", purgatoryLocation.getY());
            dbo.append("spawn.z", purgatoryLocation.getZ());
            dbo.append("spawn.pitch", purgatoryLocation.getPitch());
            dbo.append("spawn.yaw", purgatoryLocation.getYaw());

            FileUtils.write(f, dbo.toJson());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addToPurgatory(Player player) {
        if (!Foxtrot.getInstance().getDeathbanMap().isDeathbanned(player.getUniqueId())) {
            withdrawFromPurgatory(player, false);
            return;
        }

        player.sendMessage(ChatColor.GREEN + "You have been put into purgatory!");
        player.sendMessage(ChatColor.GREEN + "To redude your deathban, you need to mine valuables. " + ChatColor.GRAY + "(/purgatory)");

        player.getInventory().remove(Material.DIAMOND_PICKAXE);
        if(player.hasPermission("foxtrot.vip")){
            player.getInventory().addItem(vippick);
        } else if (player.hasPermission("foxtrot.poseidon")) {
            player.getInventory().addItem(poseidonpick);
        } else if (player.hasPermission("foxtrot.neptune")) {
            player.getInventory().addItem(neptunepick);
        } else if (player.hasPermission("foxtrot.mermaid")) {
            player.getInventory().addItem(mermaidpick);
        } else if (player.hasPermission("foxtrot.reef")) {
            player.getInventory().addItem(reefpick);
        } else if (player.hasPermission("foxtrot.coral")) {
            player.getInventory().addItem(coralpick);
        } else {
            player.getInventory().addItem(pickaxe);
        }
        player.updateInventory();
    }

    public void withdrawFromPurgatory(Player player, boolean revived) {
        banCache.remove(player.getName());
        player.closeInventory();
        player.getInventory().remove(Material.DIAMOND_PICKAXE);
        player.getInventory().remove(Material.GOLD_PICKAXE);
        player.getInventory().remove(Material.IRON_PICKAXE);

        player.teleport(Foxtrot.getInstance().getServerHandler().getSpawnLocation());
        if (revived)
            player.sendMessage(ChatColor.GREEN + "You have been revived.");
        else
            player.sendMessage(ChatColor.GREEN + "Your deathban has expired, you have been withdrawn from purgatory.");
        player.updateInventory();
    }

}