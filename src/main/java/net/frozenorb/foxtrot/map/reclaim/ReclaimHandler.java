package net.frozenorb.foxtrot.map.reclaim;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import net.frozenorb.foxtrot.Foxtrot;
import net.minecraft.util.org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class ReclaimHandler {

    private Map<String, Reclaim> reclaims = new HashMap<>();
    private List<UUID> reclaimed = new ArrayList<>();
    private String message;

    public ReclaimHandler() {
        message = Foxtrot.getInstance().getConfig().getString("reclaim.message", "{player} &ehas claimed their {rankdisplay} &eperks!");

        ConfigurationSection ranks = Foxtrot.getInstance().getConfig().getConfigurationSection("reclaim.ranks");
        for (String rankName : ranks.getKeys(false)) {
            reclaims.put(rankName, new Reclaim(rankName, ranks.getString(rankName + ".name", rankName), ranks.getStringList(rankName + ".commands")));
        }

        Bukkit.getLogger().info("Loaded " + reclaims.size() + " reclaims.");

        try {
            File f = new File(Foxtrot.getInstance().getDataFolder(), "reclaimed.json");

            if (!f.exists()) {
                f.createNewFile();
            }

            JsonArray array = new JsonParser().parse(FileUtils.readFileToString(f)).getAsJsonArray();
            for (JsonElement element : array) {
                reclaimed.add(UUID.fromString(element.getAsString()));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void save() {
        try {
            File f = new File(Foxtrot.getInstance().getDataFolder(), "reclaimed.json");

            if (!f.exists()) {
                f.createNewFile();
            }

            JsonArray array = new JsonArray();
            for (UUID uuid : reclaimed) {
                array.add(new JsonPrimitive(uuid.toString()));
            }

            FileUtils.write(f, array.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean hasReclaimed(UUID uuid) {
        return reclaimed.contains(uuid);
    }

    public void resetReclaim(UUID uuid) {
        reclaimed.remove(uuid);
    }

    public void reclaim(Player player) {
        String rank = Foxtrot.getInstance().getServerHandler().getPermissions().getPlayerRank(player);
        Reclaim reclaim = reclaims.get(rank);

        if (reclaim == null) {
            player.sendMessage(ChatColor.RED + "Your rank doesn't have a reclaim.");
            return;
        }

        if (reclaimed.contains(player.getUniqueId())) {
            player.sendMessage(ChatColor.RED + "You have already reclaimed this map.");
            return;
        }

        reclaimed.add(player.getUniqueId());
        reclaim.apply(message, player);
    }

}