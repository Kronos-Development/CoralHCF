package net.frozenorb.foxtrot.abilities;

import com.google.common.collect.ImmutableList;
import lombok.Getter;
import net.frozenorb.foxtrot.Foxtrot;
import net.frozenorb.foxtrot.abilities.type.*;

import java.util.*;

public class AbilityHandler {

    @Getter private boolean abilitiesEnabled;
    private Map<String, AbstractAbility> abilityMap = new LinkedHashMap<>();

    public AbilityHandler() {
        abilitiesEnabled = Foxtrot.getInstance().getConfig().getBoolean("abilities", true);
        if (!abilitiesEnabled) return;

        Arrays.asList(
                new SwitcherAbility(),
                new GrapplingAbility(),
                new RocketAbility(),
                new RotateAbility(),
                new PocketBardAbility(),
                new NauseaAbility()
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

}