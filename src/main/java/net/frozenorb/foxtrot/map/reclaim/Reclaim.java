package net.frozenorb.foxtrot.map.reclaim;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.List;

@Getter
@AllArgsConstructor
public class Reclaim {
    private String rank;
    private String rankDisplayName;
    private List<String> commands;

    public void apply(String broadcast, Player player) {
        broadcast = broadcast.replace("{player}", player.getDisplayName());
        broadcast = broadcast.replace("{rank}", rank);
        broadcast = broadcast.replace("{rankdisplay}", rankDisplayName);
        Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', broadcast));

        for (String command : commands) {
            command = command.replace("{player}", player.getName());
            command = command.replace("{rank}", rank);

            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
        }
    }

}