package net.frozenorb.foxtrot.server.cheatbreaker;

import net.frozenorb.foxtrot.server.SpawnTagHandler;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by vape on 6/7/2020 at 5:43 PM.
 */
public class CBAPITask extends BukkitRunnable {
    private static final Map<String, Boolean> spawnTagCache = new LinkedHashMap<>();

    @Override
    public void run() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            handleSpawnTag(player);
        }
    }

    private void handleSpawnTag(Player player) {
        boolean previous = spawnTagCache.getOrDefault(player.getName(), false);
        boolean current = SpawnTagHandler.isTagged(player);

        if (previous != current)
            CBAPIHook.setCompetitiveGame(player, current);

        spawnTagCache.put(player.getName(), current);
    }
}