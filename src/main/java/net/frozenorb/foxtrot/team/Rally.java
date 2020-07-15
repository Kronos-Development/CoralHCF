package net.frozenorb.foxtrot.team;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.frozenorb.foxtrot.Foxtrot;
import net.frozenorb.foxtrot.server.cheatbreaker.WrappedWaypoint;
import org.bukkit.Location;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.concurrent.TimeUnit;

/**
 * Created by vape on 6/7/2020 at 10:21 AM.
 */
@Getter
@RequiredArgsConstructor
public class Rally {
    private final Location location;
    private final WrappedWaypoint waypoint;
    private final long timestamp;
    private BukkitTask expireTask;

    public void startExpireTask(Runnable callback) {
        expireTask = new BukkitRunnable() {
            @Override
            public void run() {
                callback.run();
            }
        }.runTaskLater(Foxtrot.getInstance(), TimeUnit.MINUTES.toSeconds(10L) * 20);
    }

    public void cancelExpireTask() {
        expireTask.cancel();
    }

    public long getRemaining() {
        return (timestamp + TimeUnit.MINUTES.toMillis(10L)) - System.currentTimeMillis();
    }

    public boolean isActive() {
        return getRemaining() > 0L;
    }
}