package net.frozenorb.foxtrot.events.region.glowmtn;

import lombok.Getter;
import lombok.Setter;
import net.frozenorb.foxtrot.Foxtrot;
import net.frozenorb.foxtrot.events.region.glowmtn.listeners.GlowListener;
import net.frozenorb.foxtrot.team.claims.Claim;
import net.frozenorb.qlib.qLib;
import net.minecraft.util.org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class GlowHandler {

    private static File file;
    @Getter private static String glowTeamName = "Glowstone";
    @Getter @Setter private GlowMountain glowMountain;

    public GlowHandler() {
        try {
            file = new File(Foxtrot.getInstance().getDataFolder(), "glowmtn.json");

            if (!file.exists()) {
                glowMountain = null;

                if (file.createNewFile()) {
                    Foxtrot.getInstance().getLogger().warning("Created a new glow mountain json file.");
                }
            } else {
                glowMountain = qLib.GSON.fromJson(FileUtils.readFileToString(file), GlowMountain.class);
                Foxtrot.getInstance().getLogger().info("Successfully loaded the glow mountain from file");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        int secs = Foxtrot.getInstance().getServerHandler().isHardcore() ? (90 * 60 * 20) : Foxtrot.getInstance().getServerHandler().getTabServerName().contains("cane") ? Foxtrot.getInstance().getMapHandler().getTeamSize() == 8 ? 20 * 25 * 60 : 20 * 45 * 60 : 12000;
        Foxtrot.getInstance().getServer().getScheduler().runTaskTimer(Foxtrot.getInstance(), () -> {
            getGlowMountain().reset();

            // Broadcast the reset
            Bukkit.broadcastMessage(ChatColor.GOLD + "[Glowstone Mountain]" + ChatColor.GREEN + " All glowstone has been reset!");
        }, secs, secs);

        Foxtrot.getInstance().getServer().getPluginManager().registerEvents(new GlowListener(), Foxtrot.getInstance());
    }

    public void save() {
        try {
            FileUtils.write(file, qLib.GSON.toJson(glowMountain));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean hasGlowMountain() {
        return glowMountain != null;
    }

    public static Claim getClaim() {
        List<Claim> claims = Foxtrot.getInstance().getTeamHandler().getTeam(glowTeamName).getClaims();
        return claims.isEmpty() ? null : claims.get(0); // null if no glowmtn is set!
    }
}