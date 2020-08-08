package net.frozenorb.foxtrot.elevators;

import com.minexd.zoot.util.CC;
import net.frozenorb.foxtrot.Foxtrot;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Arrays;
import java.util.List;

import static net.frozenorb.foxtrot.elevators.ElevatorUtil.findValidLocation;

public class ElevatorListener implements Listener {
    private static List<String> signOperators = Arrays.asList("up", "down");
    private static List<Material> signMaterials = Arrays.asList(Material.SIGN_POST, Material.WALL_SIGN);

    @EventHandler
    public void onSignInteract(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK
                || !signMaterials.contains(event.getClickedBlock().getType())) {
            return;
        }
        BlockState blockState = event.getClickedBlock().getState();

        if (!(blockState instanceof Sign)) {
            return;
        }

        Sign sign = (Sign) blockState;

        if (!sign.getLine(0).equalsIgnoreCase(CC.translate("&9[Elevator]"))) {
            return;
        }

        float oldyaw = event.getPlayer().getLocation().getYaw();
        float oldpitch = event.getPlayer().getLocation().getPitch();

        Location teleportLocation = findValidLocation(event.getClickedBlock().getLocation(), ElevatorDirection.valueOf(sign.getLine(1).toLowerCase()));

        if (teleportLocation != null) {
            event.getPlayer().teleport(teleportLocation);
            event.getPlayer().getLocation().setYaw(oldyaw);
            event.getPlayer().getLocation().setPitch(oldpitch);
        } else {
            return;
        }
    }

    @EventHandler
    public void onSignChange(SignChangeEvent event) {
        if (!event.getLine(0).toLowerCase().equalsIgnoreCase("[elevator]")) {
            return;
        }
        if (event.getLine(1) == null
                || !signOperators.contains(event.getLine(1).toLowerCase())) {
            return;
        }

        event.setLine(0, CC.translate("&9[Elevator]"));
        event.setLine(1, (event.getLine(1)));
    }
}
