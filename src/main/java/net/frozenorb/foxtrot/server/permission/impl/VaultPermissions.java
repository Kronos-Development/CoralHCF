package net.frozenorb.foxtrot.server.permission.impl;

import net.frozenorb.foxtrot.server.permission.IServerPermissions;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;

import java.util.function.Supplier;

public class VaultPermissions implements IServerPermissions {

    private Permission perms;

    @Override
    public Supplier<Boolean> isAvailable() {
        return () -> Bukkit.getPluginManager().isPluginEnabled("Vault") && setupPermissions();
    }

    @Override
    public void init() {
        setupPermissions();

        if (perms == null) {
            Bukkit.getLogger().warning("Failed to initialize Vault permissions!");
        }
    }

    private boolean setupPermissions() {
        RegisteredServiceProvider<Permission> permissionProvider = Bukkit.getServer().getServicesManager().getRegistration(net.milkbowl.vault.permission.Permission.class);
        if (permissionProvider != null) {
            perms = permissionProvider.getProvider();
        }
        return (perms != null);
    }

    @Override
    public String getPlayerRank(Player player) {
        if (perms == null)
            return "Default";
        return perms.getPrimaryGroup(player);
    }
}