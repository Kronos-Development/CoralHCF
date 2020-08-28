package net.frozenorb.foxtrot.events.rampage;

import com.minexd.zoot.util.CC;
import lombok.Getter;
import lombok.Setter;
import net.frozenorb.foxtrot.Foxtrot;
import net.frozenorb.foxtrot.events.rampage.listener.RampageListener;
import net.frozenorb.foxtrot.team.claims.LandBoard;
import net.frozenorb.qlib.qLib;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;

import static org.bukkit.Material.CHEST;

public class RampageHandler {

    @Getter @Setter
    private boolean rampageActive = false;
    private Location rampagePackage;
    private World world;

    public RampageHandler() {
        this.world = Bukkit.getWorlds().get(0);
        Bukkit.getPluginManager().registerEvents(new RampageListener(), Foxtrot.getInstance());

        new BukkitRunnable() {
            @Override
            public void run() {
                if (!Foxtrot.getInstance().getRampageHandler().isRampageActive()) {
                    return;
                }
                spawn();
            }
        }.runTaskTimer(Foxtrot.getInstance(), 1200L, 1200L);
    }

    public void spawn() {
        int x = 0, z = 0;

        while (Math.abs(x) <= 100) x = qLib.RANDOM.nextInt(1000) - 500;
        while (Math.abs(z) <= 100) z = qLib.RANDOM.nextInt(1000) - 500;

        while (LandBoard.getInstance().getTeam(new Location(world, x, 100, z)) != null) {
            x = 0; z = 0;

            while (Math.abs(x) <= 100) x = qLib.RANDOM.nextInt(1000) - 500;
            while (Math.abs(z) <= 100) z = qLib.RANDOM.nextInt(1000) - 500;
        }

        int y = world.getHighestBlockYAt(x, z);
        Block block = world.getBlockAt(x, y, z);

        if (block == null) {
            // couldn't find location, lets try again
            spawn();
            return;
        }

        Block realBlock = block.getRelative(BlockFace.UP);
        realBlock.setType(CHEST);
        realBlock.setMetadata("RampagePackage", new FixedMetadataValue(Foxtrot.getInstance(), true));
        rampagePackage = realBlock.getLocation();

        Bukkit.getLogger().info("Spawning package at " + realBlock.getLocation());
        Bukkit.getServer().broadcastMessage(CC.translate( "&4&l[RAMPAGE EVENT] &cA Prybar has spawned at &4" + x + "&c, &4" + y + "&c, &4" + z + "&c!"));
        Bukkit.getScheduler().runTaskLater(Foxtrot.getInstance(), this::remove, 600L);
    }

    private void remove() {
        if (rampagePackage != null && rampagePackage.getBlock() != null && rampagePackage.getBlock().getType() == CHEST) {
            rampagePackage.getBlock().removeMetadata("RampagePackage", Foxtrot.getInstance());
            rampagePackage.getBlock().setType(Material.AIR);

            Bukkit.getServer().broadcastMessage(CC.translate("&4&l[RAMPAGE EVENT] &cThe Prybar despawned."));
        }
    }


}
