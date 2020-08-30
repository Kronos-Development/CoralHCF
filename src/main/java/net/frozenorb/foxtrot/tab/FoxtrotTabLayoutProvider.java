///*package net.frozenorb.foxtrot.tab;
//
//import com.google.common.collect.Maps;
//import net.frozenorb.foxtrot.Foxtrot;
//import net.frozenorb.foxtrot.events.Event;
//import net.frozenorb.foxtrot.events.EventScheduledTime;
//import net.frozenorb.foxtrot.events.koth.KOTH;
//import net.frozenorb.foxtrot.listener.BorderListener;
//import net.frozenorb.foxtrot.map.stats.StatsEntry;
//import net.frozenorb.foxtrot.nametag.FoxtrotNametagProvider;
//import net.frozenorb.foxtrot.team.Team;
//import net.frozenorb.foxtrot.team.claims.LandBoard;
//import net.frozenorb.foxtrot.team.commands.team.TeamListCommand;
//import net.frozenorb.foxtrot.util.PlayerDirection;
//import net.frozenorb.qlib.economy.FrozenEconomyHandler;
//import net.frozenorb.qlib.tab.LayoutProvider;
//import net.frozenorb.qlib.tab.TabLayout;
//import net.frozenorb.qlib.util.TimeUtils;
//import org.bukkit.Bukkit;
//import org.bukkit.ChatColor;
//import org.bukkit.Location;
//import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
//import org.bukkit.enchantments.Enchantment;
//import org.bukkit.entity.Player;
//
//import java.util.Date;
//import java.util.HashMap;
//import java.util.LinkedHashMap;
//import java.util.Map;
//
//public class FoxtrotTabLayoutProvider implements LayoutProvider {
//
//    private LinkedHashMap<Team, Integer> cachedTeamList = Maps.newLinkedHashMap();
//    long cacheLastUpdated;
//
//    @Override
//    public TabLayout provide(Player player) {
//        TabLayout layout = TabLayout.create(player);
//        TabListMode mode = Foxtrot.getInstance().getTabListModeMap().getTabListMode(player.getUniqueId());
//
//        if(mode == TabListMode.VANILLA) {
//            int x = 0;
//            int y = 0;
//
//            layout.set(x++, y, new FoxtrotNametagProvider().fetchNametag(player, player).getPrefix() + player.getName(), ((CraftPlayer)player).getHandle().ping);
//            for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
//                String prefix = new FoxtrotNametagProvider().fetchNametag(onlinePlayer, player).getPrefix();
//
//                if(onlinePlayer == player) continue;
//                if(onlinePlayer.hasMetadata("invisible")) {
//                    if(player.canSee(onlinePlayer)) prefix = ChatColor.GRAY + "*" + prefix;
//                    else continue;
//                }
//
//                layout.set(x++, y, prefix + onlinePlayer.getName(), ((CraftPlayer)onlinePlayer).getHandle().ping);
//
//                if (x == 3 && y == 20) {
//                    break;
//                }
//
//                if (x == 3) {
//                    x = 0;
//                    y++;
//                }
//            }
//        } else {
//
//
//            boolean hcf = true;
//            if (Foxtrot.getInstance().getServerHandler().isVeltKitMap() || Foxtrot.getInstance().getMapHandler().isKitMap()) {
//            /*kitmap(player, layout);
//            return layout;*/
//                hcf = false;
//            }
//
//            Team team = Foxtrot.getInstance().getTeamHandler().getTeam(player);
//
//            String serverName = Foxtrot.getInstance().getServerHandler().getTabServerName();
//            String serverIp = Foxtrot.getInstance().getServerHandler().getNetworkWebsite();
//
//            String titleColor = Foxtrot.getInstance().getServerHandler().getTabSectionColor();
//            String infoColor = Foxtrot.getInstance().getServerHandler().getTabInfoColor();
//
//            int
//                    balance = (int) FrozenEconomyHandler.getBalance(player.getUniqueId()),
//                    kills = 0, deaths = 0,
//                    lives = Foxtrot.getInstance().getSoulboundLivesMap().getLives(player.getUniqueId());
//
//            if (hcf) {
//                kills = Foxtrot.getInstance().getKillsMap().getKills(player.getUniqueId());
//                deaths = Foxtrot.getInstance().getDeathsMap().getDeaths(player.getUniqueId());
//            } else {
//                StatsEntry entry = Foxtrot.getInstance().getMapHandler().getStatsHandler().getStats(player);
//                kills = entry.getKills();
//                deaths = entry.getDeaths();
//            }
//
//            String direction = PlayerDirection.getCardinalDirection(player);
//            Location loc = player.getLocation();
//            String pos = loc.getBlockX() + ", " + loc.getBlockZ();
////        String pos = "(" + loc.getBlockX() + ", " + loc.getBlockZ() + ")";
//
//            layout.set(1, 1, serverName);
//            layout.set(1, 2, ChatColor.GRAY + serverIp);
//
//            // Player Info
//            layout.set(0, 4, titleColor + ChatColor.BOLD + "Player Info");
//            layout.set(0, 5, infoColor + "Balance: " + ChatColor.WHITE + "$" + balance);
//            layout.set(0, 6, infoColor + "Kills: " + ChatColor.WHITE + kills);
//            layout.set(0, 7, infoColor + "Deaths: " + ChatColor.WHITE + deaths);
//            layout.set(0, 8, infoColor + "Lives: " + ChatColor.WHITE + lives);
//
//            Team ownerTeam = LandBoard.getInstance().getTeam(loc);
//
//            String location;
//
//            if (ownerTeam != null) {
//                location = ownerTeam.getName(player.getPlayer());
//            } else if (!Foxtrot.getInstance().getServerHandler().isWarzone(loc)) {
//                location = ChatColor.GREEN + "Wilderness";
//            } else if (LandBoard.getInstance().getTeam(loc) != null && LandBoard.getInstance().getTeam(loc).getName().equalsIgnoreCase("citadel")) {
//                location = titleColor + "Citadel";
//            } else {
//                location = ChatColor.RED + "Warzone";
//            }
//
//            layout.set(0, 10, titleColor + ChatColor.BOLD + "Location");
//            layout.set(0, 11, infoColor + location + ChatColor.GRAY + " [" + direction + "]");
//            layout.set(0, 12, ChatColor.AQUA + "(" + ChatColor.WHITE + pos + ChatColor.AQUA + ")");
//
//            // Map Info
//            layout.set(2, 4, titleColor + ChatColor.BOLD + "Map Info");
//            layout.set(2, 5, infoColor + "Kit: " + ChatColor.WHITE + Foxtrot.getInstance().getServerHandler().getShortEnchants());
//            layout.set(2, 6, infoColor + "Factions: " + ChatColor.WHITE + Foxtrot.getInstance().getMapHandler().getTeamSize());
//            layout.set(2, 7, infoColor + "Allies: " + ChatColor.WHITE + Foxtrot.getInstance().getMapHandler().getAllyLimit());
//            layout.set(2, 8, infoColor + "Border: " + ChatColor.WHITE + BorderListener.BORDER_SIZE);
//
//            // Event
//            KOTH activeKOTH = null;
//            for (Event event : Foxtrot.getInstance().getEventHandler().getEvents()) {
//                if (!(event instanceof KOTH)) continue;
//                KOTH koth = (KOTH) event;
//                if (koth.isActive() && !koth.isHidden()) {
//                    activeKOTH = koth;
//                    break;
//                }
//            }
//
//            if (activeKOTH == null) {
//                Date now = new Date();
//
//                String nextKothName = null;
//                Date nextKothDate = null;
//
//                for (Map.Entry<EventScheduledTime, String> entry : Foxtrot.getInstance().getEventHandler().getEventSchedule().entrySet()) {
//                    if (entry.getKey().toDate().after(now)) {
//                        if (nextKothDate == null || nextKothDate.getTime() > entry.getKey().toDate().getTime()) {
//                            nextKothName = entry.getValue();
//                            nextKothDate = entry.getKey().toDate();
//                        }
//                    }
//                }
//
//                layout.set(2, 10, titleColor + ChatColor.BOLD + "Next Event");
//                if (nextKothName != null) {
//                    layout.set(2, 11, nextKothName);
//
//                    Event event = Foxtrot.getInstance().getEventHandler().getEvent(nextKothName);
//
//                    if (event instanceof KOTH) {
//                        int seconds = (int) ((nextKothDate.getTime() - System.currentTimeMillis()) / 1000);
//
//                        String time = formatIntoDetailedString(seconds)
//                                .replace("minutes", "min").replace("minute", "min")
//                                .replace("seconds", "sec").replace("second", "sec");
//
//                        layout.set(2, 12, infoColor + "In: " + ChatColor.WHITE + time);
//                    }
//                } else {
//                    layout.set(2, 11, "Not scheduled");
//                }
//            } else {
//                layout.set(2, 10, titleColor + ChatColor.BOLD + "Active Event");
//                layout.set(2, 11, activeKOTH.getName());
//                layout.set(2, 12, ChatColor.translateAlternateColorCodes('&',  "&b(&f" + activeKOTH.getCapLocation().getBlockX() + "&b, &f" + activeKOTH.getCapLocation().getBlockY() + "&b, &f" + activeKOTH.getCapLocation().getBlockZ() + "&b)"));
//            }
//
//            // Team info
//            if (team != null) {
//                String watcherRole = "Member";
//                if (team.isOwner(player.getUniqueId())) {
//                    watcherRole = "Leader";
//                } else if (team.isCoLeader(player.getUniqueId())) {
//                    watcherRole = "Co-Leader";
//                } else if (team.isCaptain(player.getUniqueId())) {
//                    watcherRole = "Captain";
//                }
//
//                layout.set(1, 4, titleColor + ChatColor.BOLD + "Faction Info");
//                layout.set(1, 5, infoColor + "Name: " + ChatColor.WHITE + team.getName());
//                layout.set(1, 6, infoColor + "Role: " + ChatColor.WHITE + watcherRole);
//                layout.set(1, 7, infoColor + "DTR: " + ChatColor.WHITE + team.getDTRColor() + Team.DTR_FORMAT.format(team.getDTR()) + team.getDTRSuffix());
//                layout.set(1, 8, infoColor + "Points: " + ChatColor.WHITE + team.getPoints());
//            }
//            Map<Team, Integer> teamPlayerCount = new HashMap<>();
//            boolean shouldReloadCache = cachedTeamList == null || (System.currentTimeMillis() - cacheLastUpdated > 2000);
//
//            if (shouldReloadCache) {
//                // Sort of weird way of getting player counts, but it does it in the least iterations (1), which is what matters!
//                for (Player other : Foxtrot.getInstance().getServer().getOnlinePlayers()) {
//                    if (other.hasMetadata("invisible")) {
//                        continue;
//                    }
//
//                    Team playerTeam = Foxtrot.getInstance().getTeamHandler().getTeam(other);
//
//                    if (playerTeam != null) {
//                        if (teamPlayerCount.containsKey(playerTeam)) {
//                            teamPlayerCount.put(playerTeam, teamPlayerCount.get(playerTeam) + 1);
//                        } else {
//                            teamPlayerCount.put(playerTeam, 1);
//                        }
//                    }
//                }
//            }
//
//            LinkedHashMap<Team, Integer> sortedTeamPlayerCount;
//
//            if (shouldReloadCache) {
//                sortedTeamPlayerCount = TeamListCommand.sortByValues(teamPlayerCount);
//                cachedTeamList = sortedTeamPlayerCount;
//                cacheLastUpdated = System.currentTimeMillis();
//            } else {
//                sortedTeamPlayerCount = cachedTeamList;
//            }
//
//            int index = -1;
//
//            boolean title = false;
//
//            for (Map.Entry<Team, Integer> teamEntry : sortedTeamPlayerCount.entrySet()) {
//                index++;
//
//                if (index > 19) {
//                    break;
//                }
//
//                if (!title) {
//                    title = true;
//                    layout.set(1, 13, titleColor + ChatColor.BOLD.toString() + "Online Teams");
//                }
//
//                String teamName = teamEntry.getKey().getName();
//                String teamColor = teamEntry.getKey().isMember(player.getUniqueId()) ? ChatColor.GREEN.toString() : infoColor;
//                int y = 14;
//
//                if (teamName.length() > 10) teamName = teamName.substring(0, 10);
//                layout.set(index, y, teamColor + teamName + ChatColor.GRAY + " (" + teamEntry.getValue() + ")");
//                if (index == 2) {
//                    index = 0;
//                    y++;
//                }
//            }
//        }
//
//        return layout;
//    }
//
//    private void kitmap(Player player, TabLayout layout) {
//        String serverName = Foxtrot.getInstance().getServerHandler().getTabServerName();
//        String titleColor = Foxtrot.getInstance().getServerHandler().getTabSectionColor();
//        String infoColor = Foxtrot.getInstance().getServerHandler().getTabInfoColor();
//
//        layout.set(1, 0, serverName);
//
//        layout.set(0, 4, titleColor + "&lMap Info");
//        layout.set(0, 5, infoColor + "Map Kit: P" + Enchantment.PROTECTION_ENVIRONMENTAL.getMaxLevel() + " S" + Enchantment.DAMAGE_ALL.getMaxLevel());
//        layout.set(0, 6, infoColor + "Faction Size: " + Foxtrot.getInstance().getMapHandler().getTeamSize());
//        layout.set(0, 7, infoColor + "Border: 3000");
//
//        int y = 8;
//
//        KOTH activeKOTH = null;
//        for (Event event : Foxtrot.getInstance().getEventHandler().getEvents()) {
//            if (!(event instanceof KOTH)) continue;
//            KOTH koth = (KOTH) event;
//            if (koth.isActive() && !koth.isHidden()) {
//                activeKOTH = koth;
//                break;
//            }
//        }
//
//        if (activeKOTH == null) {
//            Date now = new Date();
//
//            String nextKothName = null;
//            Date nextKothDate = null;
//
//            for (Map.Entry<EventScheduledTime, String> entry : Foxtrot.getInstance().getEventHandler().getEventSchedule().entrySet()) {
//                if (entry.getKey().toDate().after(now)) {
//                    if (nextKothDate == null || nextKothDate.getTime() > entry.getKey().toDate().getTime()) {
//                        nextKothName = entry.getValue();
//                        nextKothDate = entry.getKey().toDate();
//                    }
//                }
//            }
//
//            if (nextKothName != null) {
//                layout.set(0, ++y, titleColor + "Next KOTH:");
//                layout.set(0, ++y, infoColor + nextKothName);
//
//                Event event = Foxtrot.getInstance().getEventHandler().getEvent(nextKothName);
//
//                if (event != null && event instanceof KOTH) {
//                    KOTH koth = (KOTH) event;
//                    layout.set(0, ++y, infoColor + koth.getCapLocation().getBlockX() + ", " + koth.getCapLocation().getBlockY() + ", " + koth.getCapLocation().getBlockZ()); // location
//
//                    int seconds = (int) ((nextKothDate.getTime() - System.currentTimeMillis()) / 1000);
//                    layout.set(0, ++y, titleColor + "Goes active in:");
//
//                    String time = formatIntoDetailedString(seconds)
//                            .replace("minutes", "min").replace("minute", "min")
//                            .replace("seconds", "sec").replace("second", "sec");
//
//                    layout.set(0, ++y, infoColor + time);
//                }
//            }
//        } else {
//            layout.set(0, ++y, titleColor + activeKOTH.getName());
//            layout.set(0, ++y, infoColor + TimeUtils.formatIntoHHMMSS(activeKOTH.getRemainingCapTime()));
//            layout.set(0, ++y, infoColor + activeKOTH.getCapLocation().getBlockX() + ", " + activeKOTH.getCapLocation().getBlockY() + ", " + activeKOTH.getCapLocation().getBlockZ()); // location
//        }
//
//        layout.set(1, 2, titleColor + "Online&7: " + Bukkit.getOnlinePlayers().size() + "/" + Bukkit.getMaxPlayers());
//
//        layout.set(1, 4, titleColor + "Faction Info");
//        Team team = Foxtrot.getInstance().getTeamHandler().getTeam(player);
//        if (team != null) {
//            layout.set(1, 5, infoColor + "Name: " + team.getName());
//            if (team.getHQ() != null) {
//                String homeLocation = infoColor + "Home: " + team.getHQ().getBlockX() + ", " + team.getHQ().getBlockY() + ", " + team.getHQ().getBlockZ();
//                layout.set(1, 6, homeLocation);
//            } else {
//                layout.set(1, 6, infoColor + "Home: Not Set");
//            }
//
//            layout.set(1, 7, infoColor + "Balance: $" + (int) team.getBalance());
//        } else {
//            layout.set(1, 5, infoColor + "None");
//        }
//
//        layout.set(2, 4, titleColor + "Player Info");
//        layout.set(2, 5, infoColor + "Kills: " + Foxtrot.getInstance().getKillsMap().getKills(player.getUniqueId()));
//        layout.set(2, 6, infoColor + "Deaths: " + Foxtrot.getInstance().getDeathsMap().getDeaths(player.getUniqueId()));
//        layout.set(2, 7, infoColor + "Balance: $" + (int) Foxtrot.getInstance().getWrappedBalanceMap().getBalance(player.getUniqueId()));
//
//        layout.set(2, 9, titleColor + "Location");
//
//        String location;
//
//        Location loc = player.getLocation();
//        Team ownerTeam = LandBoard.getInstance().getTeam(loc);
//
//        if (ownerTeam != null) {
//            location = ownerTeam.getName(player.getPlayer());
//        } else if (!Foxtrot.getInstance().getServerHandler().isWarzone(loc)) {
//            location = ChatColor.GRAY + "The Wilderness";
//        } else if (LandBoard.getInstance().getTeam(loc) != null && LandBoard.getInstance().getTeam(loc).getName().equalsIgnoreCase("citadel")) {
//            location = titleColor + "Citadel";
//        } else {
//            location = ChatColor.RED + "Warzone";
//        }
//
//        layout.set(2, 11, location);
//
//        String direction = PlayerDirection.getCardinalDirection(player);
//        if (direction != null) {
//            layout.set(2, 10, ChatColor.GRAY + "(" + loc.getBlockX() + ", " + loc.getBlockZ() + ") [" + direction + "]");
//        } else {
//            layout.set(2, 10, ChatColor.GRAY + "(" + loc.getBlockX() + ", " + loc.getBlockZ() + ")");
//        }
//
//    }
//
//    public static String formatIntoDetailedString(int secs) {
//        if (secs <= 60) {
//            return "1 minute";
//        } else {
//            int remainder = secs % 86400;
//            int days = secs / 86400;
//            int hours = remainder / 3600;
//            int minutes = remainder / 60 - hours * 60;
//            String fDays = days > 0 ? " " + days + " day" + (days > 1 ? "s" : "") : "";
//            String fHours = hours > 0 ? " " + hours + " hour" + (hours > 1 ? "s" : "") : "";
//            String fMinutes = minutes > 0 ? " " + minutes + " minute" + (minutes > 1 ? "s" : "") : "";
//            return (fDays + fHours + fMinutes).trim();
//        }
//
//    }
//
//}
