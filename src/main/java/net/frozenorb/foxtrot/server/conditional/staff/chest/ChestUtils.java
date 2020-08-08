package net.frozenorb.foxtrot.server.conditional.staff.chest;

import net.md_5.bungee.api.ChatColor;
import net.minecraft.server.v1_7_R4.*;
import org.bukkit.block.Chest;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_7_R4.inventory.CraftInventory;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;

public class ChestUtils
{
    private static Field windowField;

    public static void openSilently(Player p, Chest c) {
        try {
            p.sendMessage(ChatColor.RED + "Opening chest silently...");
            EntityPlayer player = ((CraftPlayer)p).getHandle();
            IInventory inventory = ((CraftInventory)c.getInventory()).getInventory();
            player.nextContainerCounter();
            int counter = ChestUtils.windowField.getInt(player);
            SilentContainerChest silentChest = new SilentContainerChest((IInventory)player.inventory, inventory);
            player.playerConnection.sendPacket(new PacketPlayOutOpenWindow(counter, 0, inventory.getInventoryName(), inventory.getSize(), inventory.k_()));
            player.activeContainer = silentChest;
            player.activeContainer.windowId = counter;
            player.activeContainer.addSlotListener(player);
            c.update();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    static {
        ChestUtils.windowField = null;
        try {
            (ChestUtils.windowField = EntityPlayer.class.getDeclaredField("containerCounter")).setAccessible(true);
        }
        catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }
}
