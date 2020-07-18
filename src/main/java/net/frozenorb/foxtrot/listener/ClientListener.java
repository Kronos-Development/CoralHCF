package net.frozenorb.foxtrot.listener;

import com.cheatbreaker.api.CheatBreakerAPI;
import com.cheatbreaker.api.object.CBWaypoint;
import com.cheatbreaker.api.object.MinimapStatus;
import com.cheatbreaker.nethandler.obj.ServerRule;
import net.frozenorb.foxtrot.Foxtrot;
import net.frozenorb.foxtrot.nametag.FoxtrotNametagProvider;
import net.frozenorb.foxtrot.server.conditional.staff.ModModeEnterEvent;
import net.frozenorb.foxtrot.server.conditional.staff.ModModeExitEvent;
import net.frozenorb.foxtrot.team.Team;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ClientListener implements Listener {

    public ClientListener() {
        Bukkit.getScheduler().runTaskTimer(Foxtrot.getInstance(), () -> {
            for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                Bukkit.getOnlinePlayers().forEach(player -> CheatBreakerAPI.getInstance().overrideNametag(onlinePlayer, fetchNametag(onlinePlayer, player), player));
            }
        }, 0, 40);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        Team team = Foxtrot.getInstance().getTeamHandler().getTeam(player);

        CheatBreakerAPI.getInstance().changeServerRule(player, ServerRule.SERVER_HANDLES_WAYPOINTS, true);
        CheatBreakerAPI.getInstance().changeServerRule(player, com.moonsworth.client.nethandler.obj.ServerRule.SERVER_HANDLES_WAYPOINTS, true);
        CheatBreakerAPI.getInstance().setMinimapStatus(player, MinimapStatus.FORCED_OFF);
        CheatBreakerAPI.getInstance().sendWaypoint(player, new CBWaypoint("Spawn", player.getWorld().getSpawnLocation(), -1, true, true));
        if(team != null) team.sendWaypoint(player.getUniqueId());
    }

    @EventHandler
    public void onModMode(ModModeEnterEvent event) {
        Player player = event.getPlayer();
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) CheatBreakerAPI.getInstance().overrideNametag(onlinePlayer, fetchNametag(onlinePlayer, player), player);
    }

    @EventHandler
    public void onModExit(ModModeExitEvent event) {
        Player player = event.getPlayer();
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) CheatBreakerAPI.getInstance().resetNametag(onlinePlayer, player);
    }


    public java.util.List<String> fetchNametag(Player target, Player viewer) {
        String nameTag = (target.hasMetadata("invisible") ? ChatColor.GRAY + "*" : "") + new FoxtrotNametagProvider().fetchNametag(target, viewer).getPrefix() + target.getName();
        List<String> tag = new ArrayList<>();
        Team team = Foxtrot.getInstance().getTeamHandler().getTeam(target);
        if(team != null) tag.add(target.hasMetadata("modmode") ? team.getTeamColor().toString() + "[" + team.getName() + "]" : ChatColor.GOLD + "[" + team.getName(viewer) + ChatColor.GOLD + "]");
        if(target.hasMetadata("modmode")) tag.add(ChatColor.GRAY + "[Mod Mode]");
        tag.add(nameTag);
        return tag;
    }

}
