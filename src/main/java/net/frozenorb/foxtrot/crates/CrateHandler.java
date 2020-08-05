package net.frozenorb.foxtrot.crates;

import net.frozenorb.foxtrot.*;
import net.frozenorb.qlib.qLib;
import net.minecraft.server.v1_7_R4.PacketPlayOutWorldParticles;
import org.bukkit.plugin.*;
import com.mongodb.*;
import net.frozenorb.foxtrot.util.*;
import java.util.*;
import org.bukkit.entity.*;
import org.bukkit.event.player.*;
import org.bukkit.inventory.*;
import org.bukkit.scheduler.*;
import org.bukkit.*;
import org.bukkit.event.*;
import org.bukkit.event.block.*;

public class CrateHandler implements Listener
{
    private Map<String, Crate> crates;
    private DBCollection collection;

    public CrateHandler() {
        this.crates = new HashMap<String, Crate>();
        Bukkit.getLogger().info("Creating indexes...");
        (this.collection = Foxtrot.getInstance().getMongoPool().getDB(Foxtrot.MONGO_DB_NAME).getCollection("HCTCrates")).createIndex(new BasicDBObject("CrateName", 1));
        this.collection.createIndex(new BasicDBObject("Items", 1));
        Bukkit.getLogger().info("Creating indexes done.");
        this.loadCrates();
        Foxtrot.getInstance().getServer().getPluginManager().registerEvents(this, Foxtrot.getInstance());
    }

    public void loadCrates() {
        for (DBObject dbObject : this.collection.find()) {
            Crate crate = new Crate((String)dbObject.get("CrateName"));
            crate.setItems(InventorySerialization.deserialize((BasicDBList)dbObject.get("Items")));
            this.crates.put(crate.getKitName().toLowerCase(), crate);
        }
    }

    public void updateCrate(Player player, Crate crate) {
        crate.setItems(player.getInventory().getContents());
        BasicDBObject dbObject = new BasicDBObject();
        dbObject.put("CrateName", crate.getKitName());
        dbObject.put("Items", InventorySerialization.serialize(crate.getItems()));
        this.collection.update(new BasicDBObject("CrateName", crate.getKitName()), dbObject, true, false);
    }

    public void giveCrate(Player player, Crate crate) {
        for (ItemStack itemStack : crate.getItems()) {
            if (itemStack != null) {
                player.getInventory().addItem(new ItemStack[] { itemStack });
            }
        }
    }

    @EventHandler
    public void onCrateInteract(PlayerInteractEvent event) {
        if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            Player player = event.getPlayer();
            ItemStack inHand = player.getItemInHand();
            if (inHand.getType() == Material.ENDER_CHEST && inHand.hasItemMeta()) {
                String name = inHand.getItemMeta().getDisplayName();
                for (Crate crate : this.crates.values()) {
                    if (name.equals(crate.getKitName())) {
                        if (this.getFreeSlots(player.getInventory()) >= crate.getSize() - 1) {
                            if (inHand.getAmount() > 1) {
                                inHand.setAmount(inHand.getAmount() - 1);
                            }
                            else {
                                player.getInventory().remove(inHand);

                            ItemStack[] is = crate.getItems();
                                player.getInventory().addItem(is);
                            }
                            new BukkitRunnable() {
                                public void run() {
                                    player.updateInventory();
                                    PacketPlayOutWorldParticles packet = new PacketPlayOutWorldParticles("crit",
                                            (float)player.getLocation().getX(), (float)player.getLocation().getY(),
                                            (float)player.getLocation().getZ(),
                                            0.0f, 0.0f, 0.0f, 1.0f, 0);
                                }
                            }.runTaskLater(Foxtrot.getInstance(), 1L);
                        }
                        else {
                            player.sendMessage(ChatColor.RED + "You dont have enough space in your inventory!");
                        }
                        event.setCancelled(true);
                        event.setUseInteractedBlock(Event.Result.DENY);
                        event.setUseItemInHand(Event.Result.DENY);
                    }
                }
            }
        }
    }

    @EventHandler
    public void onCratePlace(BlockPlaceEvent event) {
        if (event.getItemInHand().getType() == Material.ENDER_CHEST && event.getItemInHand().hasItemMeta()) {
            String name = event.getItemInHand().getItemMeta().getDisplayName();
            for (Crate crate : this.crates.values()) {
                if (name.equals(crate.getKitName())) {
                    event.setCancelled(true);
                }
            }
        }
    }

    private int getFreeSlots(Inventory inventory) {
        int free = 0;
        for (ItemStack is : inventory.getContents()) {
            if (is == null || is.getType() == Material.AIR) {
                ++free;
            }
        }
        return free;
    }

    public Map<String, Crate> getCrates() {
        return this.crates;
    }
}

