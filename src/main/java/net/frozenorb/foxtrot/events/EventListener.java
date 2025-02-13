package net.frozenorb.foxtrot.events;

import com.cheatbreaker.api.CheatBreakerAPI;
import com.cheatbreaker.api.object.CBNotification;
import com.minexd.zoot.Zoot;
import com.minexd.zoot.chat.Chat;
import com.minexd.zoot.profile.Profile;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import net.frozenorb.foxtrot.Foxtrot;
import net.frozenorb.foxtrot.events.dtc.DTC;
import net.frozenorb.foxtrot.events.events.EventActivatedEvent;
import net.frozenorb.foxtrot.events.events.EventCapturedEvent;
import net.frozenorb.foxtrot.events.events.EventDeactivatedEvent;
import net.frozenorb.foxtrot.events.koth.KOTH;
import net.frozenorb.foxtrot.events.koth.events.KOTHControlLostEvent;
import net.frozenorb.foxtrot.persist.maps.CheatbreakerNotificationMap;
import net.frozenorb.foxtrot.settings.Setting;
import net.frozenorb.foxtrot.team.Team;
import net.frozenorb.foxtrot.util.InventoryUtils;
import net.frozenorb.qlib.qLib;
import net.frozenorb.qlib.serialization.LocationSerializer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class EventListener implements Listener {

    public EventListener() {
        Bukkit.getLogger().info("Creating indexes...");
        DBCollection mongoCollection = Foxtrot.getInstance().getMongoPool().getDB(Foxtrot.MONGO_DB_NAME).getCollection("KOTHCaptures");

        mongoCollection.createIndex(new BasicDBObject("Capper", 1));
        mongoCollection.createIndex(new BasicDBObject("CapperTeam", 1));
        mongoCollection.createIndex(new BasicDBObject("EventName", 1));
        Bukkit.getLogger().info("Creating indexes done.");
    }

    @EventHandler
    public void onKOTHActivated(EventActivatedEvent event) {
        if (event.getEvent().isHidden()) {
            return;
        }

        for (Player player : Foxtrot.getInstance().getServer().getOnlinePlayers()) {
           if (Foxtrot.getInstance().getCheatbreakerNotificationMap().isToggled(player.getUniqueId())) {
               CheatBreakerAPI.getInstance().sendNotification(player, new CBNotification("&8[&3" + Bukkit.getServer().getServerName() + "&8] &b" + event.getEvent().getName() + " is now live!", 3, TimeUnit.SECONDS));
           }
        }

        String[] messages;

        switch (event.getEvent().getName()) {
            case "EOTW":
                messages = new String[]{
                        ChatColor.RED + "███████",
                        ChatColor.RED + "█" + ChatColor.DARK_RED + "█████" + ChatColor.RED + "█" + " " + ChatColor.DARK_RED + "[EOTW]",
                        ChatColor.RED + "█" + ChatColor.DARK_RED + "█" + ChatColor.RED + "█████" + " " + ChatColor.RED.toString() + ChatColor.BOLD + "The cap point at spawn",
                        ChatColor.RED + "█" + ChatColor.DARK_RED + "████" + ChatColor.RED + "██" + " " + ChatColor.RED.toString() + ChatColor.BOLD + "is now active.",
                        ChatColor.RED + "█" + ChatColor.DARK_RED + "█" + ChatColor.RED + "█████" + " " + ChatColor.DARK_RED + "EOTW " + ChatColor.GOLD + "can be contested now.",
                        ChatColor.RED + "█" + ChatColor.DARK_RED + "█████" + ChatColor.RED + "█",
                        ChatColor.RED + "███████"
                };

                for (Player player : Foxtrot.getInstance().getServer().getOnlinePlayers()) {
                    player.playSound(player.getLocation(), Sound.WITHER_SPAWN, 1F, 1F);
                }

                break;
            case "Biohazard":
                messages = new String[]{
                        ChatColor.GREEN + "███████",
                        ChatColor.GREEN + "█" + ChatColor.DARK_GREEN + "████" + ChatColor.GREEN + "██" + " " + ChatColor.DARK_GREEN + "[Biohazard]",
                        ChatColor.GREEN + "█" + ChatColor.DARK_GREEN + "█" +ChatColor.GREEN + "███" + ChatColor.DARK_GREEN + "█" + ChatColor.GREEN + "█" + " " + ChatColor.GREEN.toString() + ChatColor.BOLD + "The Biohazard Event",
                        ChatColor.GREEN + "█" + ChatColor.DARK_GREEN + "████" + ChatColor.GREEN + "██" + " " + ChatColor.GREEN.toString() + ChatColor.BOLD + "is now active.",
                        ChatColor.GREEN + "█" + ChatColor.DARK_GREEN + "█" +ChatColor.GREEN + "███" + ChatColor.DARK_GREEN + "█" + ChatColor.GREEN + "█" + " " + ChatColor.DARK_GREEN + "The Cap " + ChatColor.GREEN + "can be contested now.",
                        ChatColor.GREEN + "█" + ChatColor.DARK_GREEN + "████" + ChatColor.GREEN + "██",
                        ChatColor.GREEN + "███████"
                };

                for (Player player : Foxtrot.getInstance().getServer().getOnlinePlayers()) {
                    player.playSound(player.getLocation(), Sound.ENDERDRAGON_GROWL, 1F, 1F);
                }

                break;
            case "Citadel":
                messages = new String[]{
                        ChatColor.GRAY + "███████",
                        ChatColor.GRAY + "██" + ChatColor.DARK_PURPLE + "████" + ChatColor.GRAY + "█",
                        ChatColor.GRAY + "█" + ChatColor.DARK_PURPLE + "█" + ChatColor.GRAY + "█████ " + ChatColor.GOLD + "[Citadel]",
                        ChatColor.GRAY + "█" + ChatColor.DARK_PURPLE + "█" + ChatColor.GRAY + "█████ " + ChatColor.DARK_PURPLE + event.getEvent().getName(),
                        ChatColor.GRAY + "█" + ChatColor.DARK_PURPLE + "█" + ChatColor.GRAY + "█████ " + ChatColor.GOLD + "can be contested now.",
                        ChatColor.GRAY + "█" + ChatColor.DARK_PURPLE + "█" + ChatColor.GRAY + "█████",
                        ChatColor.GRAY + "██" + ChatColor.DARK_PURPLE + "████" + ChatColor.GRAY + "█",
                        ChatColor.GRAY + "███████"
                };

                break;

            default:
                String eventType = "";
                String capPos = "Unknown";
                String capWorld;



                if (event.getEvent() instanceof KOTH) {
                    KOTH koth = (KOTH) event.getEvent();
                    capWorld = getWorldDisplayName(koth.getWorld());
                    eventType = "KOTH";
                    capPos = ChatColor.ITALIC.toString() + capWorld + ChatColor.GRAY + " (" + koth.getCapLocation().getBlockX() + ", " + koth.getCapLocation().getBlockZ() + ")";
                } else if (event.getEvent() instanceof DTC) {
                    DTC dtc = (DTC) event.getEvent();
                    capWorld = getWorldDisplayName(dtc.getWorld());
                    eventType = "DTC";
                    capPos = ChatColor.ITALIC.toString() + capWorld + ChatColor.GRAY + " (" + dtc.getCapLocation().getBlockX() + ", " + dtc.getCapLocation().getBlockZ() + ")";
                }

                String main = Foxtrot.getInstance().getServerHandler().getEventMainColor();
                String other = Foxtrot.getInstance().getServerHandler().getEventOtherColor();

                messages = new String[]{
                        other + "███████",
                        other + "█" + main + "█"   + other + "███" + main + "█" + other + "█",
                        other + "█" + main + "█"   + other + "██"  + main + "█" + other + "██" + " " + ChatColor.BLUE.toString() + ChatColor.BOLD + "[Event]",
                        other + "█" + main + "███" + other + "███" + " " + ChatColor.AQUA + event.getEvent().getName() + ChatColor.WHITE + " " + eventType,
                        other + "█" + main + "█"   + other + "██"  + main + "█" + other + "██" + " " + ChatColor.WHITE + "can now be contested.",
                        other + "█" + main + "█"   + other + "███" + main + "█" + other + "█" + " " + ChatColor.GRAY.toString() + capPos,
                        other + "█" + main + "█"   + other + "███" + main + "█" + other + "█",
                        other + "███████"
                };

                break;
        }

        final String[] messagesFinal = messages;

        new BukkitRunnable() {

            public void run() {
                for (Player player : Foxtrot.getInstance().getServer().getOnlinePlayers()) {
                    player.sendMessage(messagesFinal);
                }
            }

        }.runTaskAsynchronously(Foxtrot.getInstance());

        // Can't forget console now can we
        for (String message : messages) {
            Foxtrot.getInstance().getLogger().info(message);
        }
    }

    @EventHandler
    public void onKOTHCaptured(final EventCapturedEvent event) {
        if (event.getEvent().isHidden()) {
            return;
        }

        final Team team = Foxtrot.getInstance().getTeamHandler().getTeam(event.getPlayer());
        String teamName = ChatColor.GOLD + "[" + ChatColor.YELLOW + "-" + ChatColor.GOLD + "]";

        if (team != null) {
            teamName = ChatColor.GOLD + "[" + ChatColor.YELLOW + team.getName() + ChatColor.GOLD + "]";
        }

        final String[] filler = {"", "", "", "", "", ""};
        String[] messages;

        String main = Foxtrot.getInstance().getServerHandler().getEventMainColor();
        String other = Foxtrot.getInstance().getServerHandler().getEventOtherColor();

        if (event.getEvent().getName().equalsIgnoreCase("Citadel")) {
            messages = new String[]{
                    other + "███████",
                    other + "██" + main + "████" + other + "█",
                    other + "█" + main + "█" + other + "█████ " + ChatColor.GOLD + "[Citadel]",
                    other + "█" + main + "█" + other + "█████ " + ChatColor.YELLOW + "controlled by",
                    other + "█" + main + "█" + other + "█████ " + teamName + ChatColor.WHITE + event.getPlayer().getDisplayName(),
                    other + "█" + main + "█" + other + "█████",
                    other + "██" + main + "████" + other + "█",
                    other + "███████"
            };
        } else if (event.getEvent().getName().equalsIgnoreCase("EOTW")) {
            messages = new String[]{
                    ChatColor.RED + "███████",
                    ChatColor.RED + "█" + ChatColor.DARK_RED + "█████" + ChatColor.RED + "█" + " " + ChatColor.DARK_RED + "[EOTW]",
                    ChatColor.RED + "█" + ChatColor.DARK_RED + "█" + ChatColor.RED + "█████" + " " + ChatColor.RED.toString() + ChatColor.BOLD + "EOTW has been",
                    ChatColor.RED + "█" + ChatColor.DARK_RED + "████" + ChatColor.RED + "██" + " " + ChatColor.RED.toString() + ChatColor.BOLD + "controlled by",
                    ChatColor.RED + "█" + ChatColor.DARK_RED + "█" + ChatColor.RED + "█████" + " " + teamName + ChatColor.WHITE + event.getPlayer().getDisplayName(),
                    ChatColor.RED + "█" + ChatColor.DARK_RED + "█████" + ChatColor.RED + "█",
                    ChatColor.RED + "███████",
            };
        } else if (event.getEvent().getType() == EventType.DTC) {
            messages = new String[]{
                    ChatColor.RED + "███████",
                    ChatColor.RED + "█" + ChatColor.GOLD + "█████" + ChatColor.RED + "█" + " " + ChatColor.GOLD + "[Event]",
                    ChatColor.RED + "█" + ChatColor.GOLD + "█" + ChatColor.RED + "█████" + " " + ChatColor.YELLOW.toString() + ChatColor.BOLD + "DTC has been",
                    ChatColor.RED + "█" + ChatColor.GOLD + "████" + ChatColor.RED + "██" + " " + ChatColor.YELLOW.toString() + ChatColor.BOLD + "controlled by",
                    ChatColor.RED + "█" + ChatColor.GOLD + "█" + ChatColor.RED + "█████" + " " + teamName + ChatColor.WHITE + event.getPlayer().getDisplayName(),
                    ChatColor.RED + "█" + ChatColor.GOLD + "█████" + ChatColor.RED + "█",
                    ChatColor.RED + "███████",
            };

            ItemStack rewardKey = InventoryUtils.generateKOTHRewardKey(event.getEvent().getName() + " DTC", 1);
            ItemStack kothSign = Foxtrot.getInstance().getServerHandler().generateKOTHSign(event.getEvent().getName(), team == null ? event.getPlayer().getName() : team.getName(), EventType.DTC);

            event.getPlayer().getInventory().addItem(rewardKey);
            event.getPlayer().getInventory().addItem(kothSign);

            if (!event.getPlayer().getInventory().contains(rewardKey)) {
                event.getPlayer().getWorld().dropItemNaturally(event.getPlayer().getLocation(), rewardKey);
            }

            if (!event.getPlayer().getInventory().contains(kothSign)) {
                event.getPlayer().getWorld().dropItemNaturally(event.getPlayer().getLocation(), kothSign);
            }
        } else {
            messages = new String[]{
                    ChatColor.GOLD + "[KingOfTheHill] " + ChatColor.BLUE + event.getEvent().getName() + ChatColor.YELLOW + " has been controlled by " + teamName + ChatColor.WHITE + event.getPlayer().getDisplayName() + ChatColor.YELLOW + "!",
                    ChatColor.GOLD + "[KingOfTheHill] " + ChatColor.YELLOW + "Awarded" + ChatColor.BLUE + " KOTH Key" + ChatColor.YELLOW + " to " + teamName + ChatColor.WHITE + event.getPlayer().getDisplayName() + ChatColor.YELLOW + "."
            };

            KOTH koth = (KOTH) event.getEvent();
            int tier = 1;
            if (Bukkit.getWorld(koth.getWorld()).getEnvironment() != World.Environment.NORMAL) {
                tier = 2;
            }


            ItemStack rewardKey = InventoryUtils.generateKOTHRewardKey(event.getEvent().getName() + " KOTH", tier);
            ItemStack kothSign = Foxtrot.getInstance().getServerHandler().generateKOTHSign(event.getEvent().getName(), team == null ? event.getPlayer().getName() : team.getName(), EventType.KOTH);

            event.getPlayer().getInventory().addItem(rewardKey);
            event.getPlayer().getInventory().addItem(kothSign);

            if (!event.getPlayer().getInventory().contains(rewardKey)) {
                event.getPlayer().getWorld().dropItemNaturally(event.getPlayer().getLocation(), rewardKey);
            }

            if (!event.getPlayer().getInventory().contains(kothSign)) {
                event.getPlayer().getWorld().dropItemNaturally(event.getPlayer().getLocation(), kothSign);
            }

            Team playerTeam = Foxtrot.getInstance().getTeamHandler().getTeam(event.getPlayer());
            if (playerTeam != null) {
                if (Foxtrot.getInstance().getRampageHandler().isRampageActive()) {
                    playerTeam.setKothCaptures(playerTeam.getKothCaptures() + 2);
                } else {
                    playerTeam.setKothCaptures(playerTeam.getKothCaptures() + 1);
                }
            }
        }

        final String[] messagesFinal = messages;

        new BukkitRunnable() {

            public void run() {
                for (Player player : Foxtrot.getInstance().getServer().getOnlinePlayers()) {
                    player.sendMessage(filler);
                    player.sendMessage(messagesFinal);
                }
            }

        }.runTaskAsynchronously(Foxtrot.getInstance());

        // Can't forget console now can we
        // but we don't want to give console the filler.
        for (String message : messages) {
            Foxtrot.getInstance().getLogger().info(message);
        }

        final BasicDBObject dbObject = new BasicDBObject();

        dbObject.put("EventName", event.getEvent().getName());
        dbObject.put("EventType", event.getEvent().getType().name());
        dbObject.put("CapturedAt", new Date());
        dbObject.put("Capper", event.getPlayer().getUniqueId().toString().replace("-", ""));
        dbObject.put("CapperTeam", team == null ? null : team.getUniqueId().toString());
        if (event.getEvent().getType() == EventType.KOTH) {
            dbObject.put("EventLocation", LocationSerializer.serialize(((KOTH) event.getEvent()).getCapLocation().toLocation(event.getPlayer().getWorld())));
        }

        new BukkitRunnable() {

            public void run() {
                DBCollection kothCapturesCollection = Foxtrot.getInstance().getMongoPool().getDB(Foxtrot.MONGO_DB_NAME).getCollection("KOTHCaptures");
                kothCapturesCollection.insert(dbObject);
            }

        }.runTaskAsynchronously(Foxtrot.getInstance());

        new BukkitRunnable() {

            public void run() {
                Foxtrot.getInstance().getTeamHandler().getTeam(event.getPlayer()).getMembers().forEach(member -> {
                    Profile profile = Profile.getProfiles().get(member);
                    if (!event.getEventName().equalsIgnoreCase("Citadel") || !event.getEventName().equalsIgnoreCase("Conquest")) {
                        profile.setTokens(profile.getTokens() + 10);
                        profile.save();
                    } else {
                        if (event.getEventName().equalsIgnoreCase("Citadel"))
                            profile.setTokens(profile.getTokens() + 40);
                        if (event.getEventName().equalsIgnoreCase("Conquest"))
                            profile.setTokens(profile.getTokens() + 50);
                        profile.save();
                    }
                });
            }
        }.runTaskAsynchronously(Foxtrot.getInstance());
    }

    @EventHandler
    public void onKOTHControlLost(final KOTHControlLostEvent event) {
        if (event.getKOTH().getRemainingCapTime() <= (event.getKOTH().getCapTime() - 30)) {
            Foxtrot.getInstance().getServer().broadcastMessage(ChatColor.GOLD + "[KingOfTheHill] Control of " + ChatColor.YELLOW + event.getKOTH().getName() + ChatColor.GOLD + " lost.");
        }
    }

    @EventHandler
    public void onKOTHDeactivated(EventDeactivatedEvent event) {
        // activate koths every 10m on the kitmap
        if (!Foxtrot.getInstance().getMapHandler().isKitMap() && !Foxtrot.getInstance().getServerHandler().isVeltKitMap()) {
            return;
        }

        Bukkit.getScheduler().runTaskLater(Foxtrot.getInstance(), () -> {
            net.frozenorb.foxtrot.events.EventHandler eventHandler = Foxtrot.getInstance().getEventHandler();
            List<net.frozenorb.foxtrot.events.Event> localEvents = new ArrayList<>(eventHandler.getEvents());

            if (localEvents.isEmpty()) {
                return;
            }

            List<KOTH> koths = new ArrayList<>();
            // don't start a koth while another is active
            for (Event localEvent : localEvents) {
                if (localEvent.isActive()) {
                    return;
                } else if (localEvent.getType() == EventType.KOTH) {
                    koths.add((KOTH) localEvent);
                }
            }

            KOTH selected = koths.get(qLib.RANDOM.nextInt(koths.size()));
            selected.activate();
        }, 10 * 60 * 20);
    }

    private String getWorldDisplayName(String input) {
        switch(input) {
            case "world": {
                return "Overworld";
            }
            case "world_nether": {
                return "The Nether";
            }
            case "world_the_end": {
                return "The End";
            }
            default: {
                return input;
            }
        }
    }

}