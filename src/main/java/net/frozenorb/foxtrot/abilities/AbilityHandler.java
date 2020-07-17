package net.frozenorb.foxtrot.abilities;

import com.google.common.collect.ImmutableList;
import lombok.Getter;
import net.frozenorb.foxtrot.Foxtrot;
import net.frozenorb.foxtrot.abilities.type.*;
import net.frozenorb.qlib.util.TimeUtils;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.concurrent.TimeUnit;

public class AbilityHandler {

    @Getter private boolean abilitiesEnabled;
    private Map<String, AbstractAbility> abilityMap = new LinkedHashMap<>();
    private Map<UUID, Long> cooldownMap = new HashMap<>();

    public AbilityHandler() {
        abilitiesEnabled = Foxtrot.getInstance().getConfig().getBoolean("abilities", true);
        if (!abilitiesEnabled) return;

        Arrays.asList(
                new SwitcherAbility(),
                new GrapplingAbility(),
                new RocketAbility(),
                new RotateAbility(),
                new PocketBardAbility(),
                new NauseaAbility(),
                new FakePearlAbility()
        ).forEach(this::registerAbility);
    }

    public List<AbstractAbility> getAbilities() {
        return ImmutableList.copyOf(abilityMap.values());
    }

    public AbstractAbility findAbility(String name) {
        if (abilityMap.containsKey(name.toUpperCase()))
            return abilityMap.get(name.toUpperCase());

        for (AbstractAbility ability : abilityMap.values()) {
            if (ability.getId().equalsIgnoreCase(name))
                return ability;
        }

        return null;
    }

    private void registerAbility(AbstractAbility ability) {
        Foxtrot.getInstance().getServer().getPluginManager().registerEvents(ability, Foxtrot.getInstance());
        abilityMap.put(ability.getId(), ability);
    }

    public boolean isOnCooldown(UUID uuid) {
        return getCooldownLeft(uuid) > 0L;
    }

    public long getCooldownLeft(UUID uuid) {
        if (!cooldownMap.containsKey(uuid))
            return 0L;

        long timeLeft = cooldownMap.getOrDefault(uuid, 0L) - System.currentTimeMillis();

        if (timeLeft <= 0L)
            clearCooldown(uuid);

        return timeLeft;
    }

    public void applyCooldown(UUID uuid) {
        cooldownMap.put(uuid, System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(10));
    }

    public void clearCooldown(UUID uuid) {
        cooldownMap.remove(uuid);
    }


}