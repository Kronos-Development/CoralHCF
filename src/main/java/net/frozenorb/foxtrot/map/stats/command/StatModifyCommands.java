package net.frozenorb.foxtrot.map.stats.command;

import net.frozenorb.foxtrot.Foxtrot;
import net.frozenorb.foxtrot.map.stats.StatsEntry;
import net.frozenorb.foxtrot.team.Team;
import net.frozenorb.qlib.command.Command;
import net.frozenorb.qlib.command.Param;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class StatModifyCommands {

	@Command(names = "sm setkills", permission = "op")
	public static void setKills(Player player, @Param(name = "kills") int kills) {
		if (!Foxtrot.getInstance().getMapHandler().isKitMap() && !Foxtrot.getInstance().getServerHandler().isVeltKitMap()) {
			player.sendMessage("§cThis is a KitMap only command.");
			return;
		}

		StatsEntry stats = Foxtrot.getInstance().getMapHandler().getStatsHandler().getStats(player);
		stats.setKills(kills);

		Foxtrot.getInstance().getKillsMap().setKills(player.getUniqueId(), kills);

		player.sendMessage(ChatColor.GREEN + "You've set your own kills to: " + kills);
	}

	@Command(names = "sm setdeaths", permission = "op")
	public static void setDeaths(Player player, @Param(name = "deaths") int deaths) {
		if (!Foxtrot.getInstance().getMapHandler().isKitMap() && !Foxtrot.getInstance().getServerHandler().isVeltKitMap()) {
			player.sendMessage("§cThis is a KitMap only command.");
			return;
		}

		StatsEntry stats = Foxtrot.getInstance().getMapHandler().getStatsHandler().getStats(player);
		stats.setDeaths(deaths);

		Foxtrot.getInstance().getDeathsMap().setDeaths(player.getUniqueId(), deaths);

		player.sendMessage(ChatColor.GREEN + "You've set your own deaths to: " + deaths);
	}

	@Command(names = "sm setteamkills", permission = "op")
	public static void setTeamKills(Player player, @Param(name = "kills") int kills) {
		if (!Foxtrot.getInstance().getMapHandler().isKitMap() && !Foxtrot.getInstance().getServerHandler().isVeltKitMap()) {
			player.sendMessage("§cThis is a KitMap only command.");
			return;
		}

		Team team = Foxtrot.getInstance().getTeamHandler().getTeam(player);

		if (team != null) {
			team.setKills(kills);
			player.sendMessage(ChatColor.GREEN + "You've set your team's kills to: " + kills);
		}
	}

	@Command(names = "sm setteamdeaths", permission = "op")
	public static void setTeamDeaths(Player player, @Param(name = "deaths") int deaths) {
		if (!Foxtrot.getInstance().getMapHandler().isKitMap() && !Foxtrot.getInstance().getServerHandler().isVeltKitMap()) {
			player.sendMessage("§cThis is a KitMap only command.");
			return;
		}

		Team team = Foxtrot.getInstance().getTeamHandler().getTeam(player);

		if (team != null) {
			team.setDeaths(deaths);
			player.sendMessage(ChatColor.GREEN + "You've set your team's deaths to: " + deaths);
		}
	}

}
