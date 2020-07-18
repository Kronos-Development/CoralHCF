package net.frozenorb.foxtrot.server.conditional.staff.chest;

import net.minecraft.server.v1_7_R4.*;
import org.bukkit.craftbukkit.v1_7_R4.inventory.CraftInventory;
import org.bukkit.craftbukkit.v1_7_R4.inventory.CraftInventoryDoubleChest;
import org.bukkit.craftbukkit.v1_7_R4.inventory.CraftInventoryPlayer;
import org.bukkit.craftbukkit.v1_7_R4.inventory.CraftInventoryView;

public class SilentContainerChest extends Container {

    public IInventory container;
    private int f;
    private CraftInventoryView bukkitEntity;
    private PlayerInventory player;

    public CraftInventoryView getBukkitView() {
        if (this.bukkitEntity != null) {
            return this.bukkitEntity;
        }
        CraftInventory inventory;
        if (this.container instanceof PlayerInventory) {
            inventory = new CraftInventoryPlayer((PlayerInventory)this.container);
        }
        else if (this.container instanceof InventoryLargeChest) {
            inventory = new CraftInventoryDoubleChest((InventoryLargeChest)this.container);
        }
        else {
            inventory = new CraftInventory(this.container);
        }
        return this.bukkitEntity = new CraftInventoryView(this.player.player.getBukkitEntity(), inventory, this);
    }

    public SilentContainerChest(IInventory firstInv, IInventory secondInv) {
        this.bukkitEntity = null;
        this.container = secondInv;
        this.f = secondInv.getSize() / 9;
        int i = (this.f - 4) * 18;
        this.player = (PlayerInventory)firstInv;
        for (int j = 0; j < this.f; ++j) {
            for (int k = 0; k < 9; ++k) {
                this.a(new Slot(secondInv, k + j * 9, 8 + k * 18, 18 + j * 18));
            }
        }
        for (int j = 0; j < 3; ++j) {
            for (int k = 0; k < 9; ++k) {
                this.a(new Slot(firstInv, k + j * 9 + 9, 8 + k * 18, 103 + j * 18 + i));
            }
        }
        for (int j = 0; j < 9; ++j) {
            this.a(new Slot(firstInv, j, 8 + j * 18, 161 + i));
        }
    }

    public boolean a(EntityHuman entityhuman) {
        return !this.checkReachable || this.container.a(entityhuman);
    }

    public ItemStack b(EntityHuman entityhuman, int i) {
        ItemStack itemstack = null;
        Slot slot = (Slot) this.c.get(i);
        if (slot != null && slot.hasItem()) {
            ItemStack item = slot.getItem();
            itemstack = item.cloneItemStack();
            if (i < this.f * 9) {
                if (!this.a(item, this.f * 9, this.c.size(), true)) {
                    return null;
                }
            }
            else if (!this.a(item, 0, this.f * 9, false)) {
                return null;
            }
            if (item.count == 0) {
                slot.set(null);
            }
            else {
                slot.f();
            }
        }
        return itemstack;
    }

    public void b(EntityHuman entityhuman) {
        super.b(entityhuman);
    }
}
