package net.frozenorb.foxtrot.server.cheatbreaker;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;

/**
 * Created by vape on 6/7/2020 at 10:31 AM.
 */
@Getter
@Setter
@AllArgsConstructor
public class WrappedWaypoint {
    private String name;
    private int x, y, z;
    private String world;
    private int color;
    private boolean forced, visible;

    public WrappedWaypoint(String name, Location location, String world, int color) {
        this(name, location.getBlockX(), location.getBlockY(), location.getBlockZ(), world, color, true, true);
    }
}