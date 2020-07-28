package net.frozenorb.foxtrot.server.conditional;

import lombok.Getter;
import net.frozenorb.foxtrot.Foxtrot;
import net.frozenorb.foxtrot.server.conditional.staff.ModHandler;
import net.frozenorb.foxtrot.server.conditional.staff.chest.ChestUtils;
import net.frozenorb.qlib.command.FrozenCommandHandler;
import net.frozenorb.qlib.visibility.FrozenVisibilityHandler;
import net.frozenorb.qlib.visibility.VisibilityAction;
import net.frozenorb.qlib.visibility.VisibilityHandler;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;

public class ConditionalHandler {

    @Getter private boolean staffHandler;

    public ConditionalHandler() {
        if (!isEnabled("Vulcan") && !isEnabled("qModSuite") && !isEnabled("pModSuite") && !isEnabled("Basic") && !isEnabled("Bridge"))
            registerFoxtrotStaffHandler();
    }

    private void registerFoxtrotStaffHandler() {
        staffHandler = true;

        FrozenCommandHandler.registerPackage(Foxtrot.getInstance(), "net.frozenorb.foxtrot.server.conditional.staff");

        Bukkit.getServer().getPluginManager().registerEvents(new Listener() {

            @EventHandler
            public void onJoin(PlayerJoinEvent event) {
                Player player = event.getPlayer();

                if (player.hasPermission("foxtrot.modmode"))
                    ModHandler.enableModMode(player);
            }

            @EventHandler
            public void onQuit(PlayerQuitEvent event) {
                Player player = event.getPlayer();

                ModHandler.disableModMode(player);
            }

            @EventHandler
            public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
                Player player = event.getPlayer();
                if (!ModHandler.isModMode(player) || !ModHandler.StaffItems.INSPECT_BOOK.isSimilar(player.getItemInHand())) {
                    return;
                }
                if (!(event.getRightClicked() instanceof Player)) {
                    return;
                }
                Player clicked = (Player)event.getRightClicked();
                player.sendMessage(ChatColor.YELLOW + "Opening inventory of: " + ChatColor.AQUA + clicked.getName());
                player.chat("/invsee " + clicked.getName());
            }

            @EventHandler
            public void onInteract(PlayerInteractEvent event) {
                Player player = event.getPlayer();

                if (!ModHandler.isModMode(player) || !event.getAction().name().contains("RIGHT")) return;

                if (event.getClickedBlock() != null && event.getClickedBlock().getState() instanceof Chest) {
                    event.setCancelled(true);
                    ChestUtils.openSilently(event.getPlayer(), (Chest)event.getClickedBlock().getState());
                }else if(event.getItem() != null) {
                    if (event.getItem().isSimilar(ModHandler.StaffItems.GO_VIS))
                        ModHandler.setVanished(player, false);
                    else if (event.getItem().isSimilar(ModHandler.StaffItems.GO_INVIS))
                        ModHandler.setVanished(player, true);
                }
            }

            @EventHandler
            public void onDamage(EntityDamageByEntityEvent event) {
                if (!(event.getDamager() instanceof Player)) return;
                Player player = (Player) event.getDamager();

                if (ModHandler.isVanished(player)) {
                    player.sendMessage(ChatColor.RED.toString() + ChatColor.BOLD + "You can't do this while in mod mode.");
                    event.setCancelled(true);
                }
            }

            @EventHandler
            public void onPlayerInteract(PlayerInteractEvent event) {
                if (event.getAction() == Action.PHYSICAL && ModHandler.isVanished(event.getPlayer())) {
                    event.setCancelled(true);
                }
            }

            @EventHandler
            public void onPlace(BlockPlaceEvent event) {
                if (ModHandler.isVanished(event.getPlayer())) {
                    if (event.getPlayer().getGameMode() != GameMode.CREATIVE || !event.getPlayer().hasMetadata("build"))
                        event.setCancelled(true);
                }
            }

            @EventHandler
            public void onBreak(BlockBreakEvent event) {
                if (ModHandler.isVanished(event.getPlayer())) {
                    if (event.getPlayer().getGameMode() != GameMode.CREATIVE || !event.getPlayer().hasMetadata("build"))
                        event.setCancelled(true);
                }
            }

            @EventHandler
            public void onPlayerPickupItem(PlayerPickupItemEvent event) {
                if (ModHandler.isVanished(event.getPlayer())) {
                    event.setCancelled(true);
                }
            }

            @EventHandler
            public void onEntityDamage(EntityDamageEvent event) {
                if (event.getEntity() instanceof Player && ModHandler.isModMode((Player)event.getEntity())) {
                    event.setCancelled(true);
                }
            }

            @EventHandler
            public void onFoodLevelChange(FoodLevelChangeEvent event) {
                if (event.getEntity() instanceof Player && ModHandler.isModMode((Player)event.getEntity())) {
                    event.setCancelled(true);
                }
            }

            @EventHandler
            public void onEntityTarget(EntityTargetEvent event) {
                if (event.getTarget() instanceof Player && ModHandler.isModMode((Player)event.getTarget())) {
                    event.setCancelled(true);
                }
            }

            @EventHandler
            public void onPlayerDropItem(PlayerDropItemEvent event) {
                if (ModHandler.isModMode(event.getPlayer())) {
                    ItemStack item = event.getItemDrop().getItemStack();
                    if (item.isSimilar(ModHandler.StaffItems.COMPASS) || item.isSimilar(ModHandler.StaffItems.CARPET) || item.isSimilar(ModHandler.StaffItems.INSPECT_BOOK) || item.isSimilar(ModHandler.StaffItems.WAND) || item.isSimilar(ModHandler.StaffItems.GO_VIS) || item.isSimilar(ModHandler.StaffItems.GO_INVIS)) {
                        event.setCancelled(true);
                        return;
                    }
                    if (ModHandler.isVanished(event.getPlayer())) {
                        event.getItemDrop().remove();
                    }
                }
            }

            @EventHandler
            public void onPlayerDeath(PlayerDeathEvent event) {
                if (ModHandler.isModMode(event.getEntity())) {
                    event.getDrops().clear();
                    event.setDeathMessage(null);
                }
            }

        }, Foxtrot.getInstance());

        FrozenVisibilityHandler.registerHandler("Foxtrot Visibility Handler", new VisibilityHandler() {
            @Override
            public VisibilityAction getAction(Player player, Player viewer) {
                return shouldHide(player, viewer) ? VisibilityAction.HIDE : VisibilityAction.NEUTRAL;
            }

            private boolean shouldHide(Player player, Player viewer) {
                boolean vanished = ModHandler.isVanished(player);
                boolean modModeViewer = ModHandler.isModMode(viewer);

                return vanished && !modModeViewer && !viewer.hasPermission("foxtrot.staff") || viewer.hasMetadata("nostaff");
            }
        });
    }

    private boolean isEnabled(String name) {
        return Bukkit.getServer().getPluginManager().isPluginEnabled(name);
    }

    public void save() {
        if (isStaffHandler()) {
            Bukkit.getOnlinePlayers().forEach(ModHandler::disableModMode);
        }
    }
}