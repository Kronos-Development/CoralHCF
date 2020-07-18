package net.frozenorb.foxtrot.server.permission.impl;

import com.minexd.zoot.profile.Profile;
import net.frozenorb.foxtrot.server.permission.IServerPermissions;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;

import java.util.function.Supplier;

public class ZootPermissions implements IServerPermissions {

    @Override
    public Supplier<Boolean> isAvailable() {
        return () -> Bukkit.getPluginManager().isPluginEnabled("zoot") || Bukkit.getPluginManager().isPluginEnabled("Helium");
    }

    @Override
    public void init() {

    }

    @Override
    public String getPlayerRank(Player player) {
        Profile profile = Profile.getByUuid(player.getUniqueId());

        if (profile.getActiveRank() == null)
            return "Default";
        return profile.getActiveRank().getDisplayName();
    }
}