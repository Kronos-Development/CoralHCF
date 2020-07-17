package net.frozenorb.foxtrot.pvpclasses.pvpclasses;

import lombok.Getter;
import net.frozenorb.foxtrot.Foxtrot;
import net.frozenorb.foxtrot.pvpclasses.PvPClass;
import net.frozenorb.foxtrot.pvpclasses.PvPClassHandler;
import net.frozenorb.foxtrot.team.Team;
import net.frozenorb.foxtrot.team.dtr.DTRBitmask;
import net.frozenorb.qlib.util.TimeUtils;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import java.util.*;
import java.util.concurrent.TimeUnit;

public class ScoutClass extends PvPClass {

	@Getter private static Map<String, Long> lastSpeedUsage = new HashMap<>();
	@Getter private static Map<String, Long> lastGrapplingUsage = new HashMap<>();
	private static Map<String, Boolean> grapplingNoDamage = new HashMap<>();

	public ScoutClass() {
		super("Scout", 5, Arrays.asList(Material.SUGAR));
	}

	@Override
	public boolean qualifies(PlayerInventory armor) {
		return wearingAllArmor(armor) &&
		       armor.getHelmet().getType() == Material.CHAINMAIL_HELMET &&
		       armor.getChestplate().getType() == Material.LEATHER_CHESTPLATE &&
		       armor.getLeggings().getType() == Material.LEATHER_LEGGINGS &&
		       armor.getBoots().getType() == Material.CHAINMAIL_BOOTS;
	}

	@Override
	public void apply(Player player) {
		player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 0), true);
		player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, Integer.MAX_VALUE, 1), true);
	}

	@Override
	public void tick(Player player) {
		int currentSpeed = 0;

		for (PotionEffect effect : player.getActivePotionEffects()) {
			if (effect.getType().getId() == PotionEffectType.SPEED.getId()) {
				currentSpeed = effect.getAmplifier();
				break;
			}
		}

		/*if (player.getItemInHand() != null && player.getItemInHand().getType() == Material.SUGAR && !DTRBitmask.SAFE_ZONE.appliesAt(player.getLocation())) {
			if (currentSpeed == 0)
				player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 1), true);
		} else if (currentSpeed == 1) {
			player.removePotionEffect(PotionEffectType.SPEED);
		}*/

		if (currentSpeed == 0 && player.getItemInHand() != null && player.getItemInHand().getType() == Material.SUGAR && !DTRBitmask.SAFE_ZONE.appliesAt(player.getLocation())) {
			smartAddPotion(player, new PotionEffect(PotionEffectType.SPEED, 20 * 6, 1), false, this);
		}

		if (!player.hasPotionEffect(PotionEffectType.SPEED)) {
			player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 0));
		}

		if (!player.hasPotionEffect(PotionEffectType.DAMAGE_RESISTANCE)) {
			player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, Integer.MAX_VALUE, 1));
		}
	}

	@Override
	public boolean itemConsumed(Player player, Material material) {
		if (material == Material.SUGAR) { // SPEED
			if (lastSpeedUsage.containsKey(player.getName()) && lastSpeedUsage.get(player.getName()) > System.currentTimeMillis()) {
				long millisLeft = lastSpeedUsage.get(player.getName()) - System.currentTimeMillis();
				String msg = TimeUtils.formatIntoDetailedString((int) millisLeft / 1000);
				player.sendMessage(ChatColor.RED + "You cannot use this for another §c§l" + msg + "§c.");
				return false;
			}

			if (DTRBitmask.SAFE_ZONE.appliesAt(player.getLocation())) {
				player.sendMessage(ChatColor.RED + "Scout effects cannot be used while in spawn.");
				return false;
			}

			for (Player nearbyPlayer : getNearbyPlayers(player, true)) {
				nearbyPlayer.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 20 * 10, 2), true);
			}
			lastSpeedUsage.put(player.getName(), System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(30));

			return true;
		}
		return true;
	}

	@EventHandler
	public void onPlayerQuitEvent(PlayerQuitEvent event) {
		lastSpeedUsage.remove(event.getPlayer().getName());
		lastGrapplingUsage.remove(event.getPlayer().getName());
		grapplingNoDamage.remove(event.getPlayer().getName());
	}

	@EventHandler
	public void onEntityDamage(EntityDamageEvent event) {
		if (!(event.getEntity() instanceof Player) || !(event.getCause() == EntityDamageEvent.DamageCause.FALL)) return;

		Player player = (Player) event.getEntity();
		if (!lastGrapplingUsage.containsKey(player.getName()) || !grapplingNoDamage.containsKey(player.getName()) || !grapplingNoDamage.get(player.getName())) return;
		grapplingNoDamage.remove(player.getName());

		long grappleCooldown = lastGrapplingUsage.get(player.getName()) - System.currentTimeMillis();
		if (grappleCooldown > 0L)
			event.setCancelled(true);
	}

	@EventHandler
	public void onPlayerFish(PlayerFishEvent event) {
		if (event.getState() == PlayerFishEvent.State.CAUGHT_FISH || event.getState() == PlayerFishEvent.State.FISHING) return;
		if (!PvPClassHandler.hasKitOn(event.getPlayer(), this)) return;

		Player player = event.getPlayer();
		ItemStack itemInHand = player.getItemInHand();

		if (itemInHand == null || itemInHand.getType() != Material.FISHING_ROD) return;

		if (lastGrapplingUsage.containsKey(player.getName()) && lastGrapplingUsage.get(player.getName()) > System.currentTimeMillis()) {
			long millisLeft = lastGrapplingUsage.get(player.getName()) - System.currentTimeMillis();
			String msg = TimeUtils.formatIntoDetailedString((int) millisLeft / 1000);
			player.sendMessage(ChatColor.RED + "You cannot use this for another §c§l" + msg + "§c.");
			return;
		}

		Location hook = event.getHook().getLocation();
		lastGrapplingUsage.put(player.getName(), System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(10L));

		switch (event.getState()) {
			case FAILED_ATTEMPT: {
				player.sendMessage(ChatColor.RED + "Your hook needs to be in the ground!");
				break;
			}

			case IN_GROUND: {
				if (event.getHook().getLocation().distance(player.getLocation()) > 30) {
					player.sendMessage(ChatColor.RED + "You are too far from the hook!");
					return;
				}

				pullTo(player, hook, false);
				//player.setVelocity(getVector(player, hook));
				break;
			}

			case CAUGHT_ENTITY: {
				if (!(event.getCaught() instanceof Player)) return;
				Player caught = (Player) event.getCaught();

				if (caught.getLocation().distance(player.getLocation()) > 30) {
					player.sendMessage(ChatColor.RED + "You are too far from " + ChatColor.YELLOW + caught.getDisplayName() + ChatColor.RED + " to do this!");
					return;
				}

				pullTo(caught, player.getLocation(), true);
				//caught.setVelocity(getVector(caught, player.getLocation()));
				break;
			}
		}

		grapplingNoDamage.put(player.getName(), true);
	}

	public List<Player> getNearbyPlayers(Player player, boolean friendly) {
		List<Player> valid = new ArrayList<>();
		Team sourceTeam = Foxtrot.getInstance().getTeamHandler().getTeam(player);

		// We divide by 2 so that the range isn't as much on the Y level (and can't be abused by standing on top of / under events)
		for (Entity entity : player.getNearbyEntities(BardClass.BARD_RANGE, BardClass.BARD_RANGE / 2, BardClass.BARD_RANGE)) {
			if (entity instanceof Player) {
				Player nearbyPlayer = (Player) entity;

				if (Foxtrot.getInstance().getPvPTimerMap().hasTimer(nearbyPlayer.getUniqueId())) {
					continue;
				}

				if (sourceTeam == null) {
					if (!friendly) {
						valid.add(nearbyPlayer);
					}

					continue;
				}

				boolean isFriendly = sourceTeam.isMember(nearbyPlayer.getUniqueId());
				boolean isAlly = sourceTeam.isAlly(nearbyPlayer.getUniqueId());

				if (friendly && isFriendly) {
					valid.add(nearbyPlayer);
				} else if (!friendly && !isFriendly && !isAlly) { // the isAlly is here so you can't give your allies negative effects, but so you also can't give them positive effects.
					valid.add(nearbyPlayer);
				}
			}
		}

		valid.add(player);

		return (valid);
	}

	private void pullTo(Entity e, Location loc, boolean pull) {
		Location l = e.getLocation();

		if (l.distanceSquared(loc) < 9) {
			if (loc.getY() > l.getY()) {
				e.setVelocity(new Vector(0, 0.25, 0));
				return;
			}
			Vector v = loc.toVector().subtract(l.toVector());
			e.setVelocity(v);
			return;
		}

		double d = loc.distance(l);
		double g = -0.08;
		double x = ((pull ? 1.0 : 3.0) + 0.07 * d) * (loc.getX() - l.getX()) / d;
		double y = (1.0 + 0.03 * d) * (loc.getY() - l.getY()) / d - 0.5 * g * d;
		double z = (1.0 + 0.07 * d) * (loc.getZ() - l.getZ()) / d;

		Vector v = e.getVelocity();
		v.setX(x);
		v.setY(y);
		v.setZ(z);
		e.setVelocity(v);
	}

	/*public Vector getVector(Entity entity, Location target) {
		Location entityLoc = entity.getLocation();
		double distance = target.distance(entityLoc);
		double gravity = -0.08;

		return entity.getVelocity()
				.clone()
				.setX((1.0+0.07*distance) * (target.getX() - entityLoc.getX())/distance)
				.setY((1.0+0.07*distance) * (target.getY() - entityLoc.getY())/distance - 0.525*gravity*distance)
				.setZ((1.0+0.07*distance) * (target.getZ() - entityLoc.getZ())/distance).multiply(1.15);
	}*/

}