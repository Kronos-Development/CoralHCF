package net.frozenorb.foxtrot.listener;

import com.minexd.zoot.util.CC;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

/*
Made by Cody at 12:51 AM on 8/8/20
 */

public class MarsListener implements Listener {

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        Block to = event.getTo().getBlock().getRelative(BlockFace.DOWN);
        World mars = Bukkit.getWorld("mars");
        Player player = event.getPlayer();

        //again, its foxtrot so we try and use this wisely, make the server do less work.
        if (to.getType() == Material.GLOWING_REDSTONE_ORE || to.getType() == Material.REDSTONE_ORE) {
            player.sendMessage("1");
            if (to.getRelative(BlockFace.EAST).getType() != Material.IRON_BLOCK) return;
            if (to.getRelative(BlockFace.WEST).getType() != Material.IRON_BLOCK) return;
            if (to.getRelative(BlockFace.NORTH).getType() != Material.IRON_BLOCK) return;
            if (to.getRelative(BlockFace.SOUTH).getType() != Material.IRON_BLOCK) return;

            //why would we be teleporting the player if the world is null? (Mojang thinking)

            player.teleport(Bukkit.getWorld("mars").getSpawnLocation());
            player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, Integer.MAX_VALUE, 2));

        }

        //this is the exit block. just place water and you should have it.
        if(player.getWorld().getName().toLowerCase() == "mars") {
            if(to.getType() == Material.STATIONARY_WATER || to.getType() == Material.WATER) {
                //
                player.teleport(EndListener.getEndReturn());
                player.removePotionEffect(PotionEffectType.JUMP);
            }

        }
    }
}
