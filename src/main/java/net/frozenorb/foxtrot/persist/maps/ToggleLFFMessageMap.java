package net.frozenorb.foxtrot.persist.maps;

import net.frozenorb.foxtrot.Foxtrot;
import net.frozenorb.foxtrot.persist.PersistMap;

import java.util.UUID;

public class ToggleLFFMessageMap extends PersistMap<Boolean> {

    public ToggleLFFMessageMap() {
        super("LFFMessageToggles", "LFFMessageEnabled");
    }

    @Override
    public String getRedisValue(Boolean toggled) {
        return String.valueOf(toggled);
    }

    @Override
    public Object getMongoValue(Boolean toggled) {
        return String.valueOf(toggled);
    }

    @Override
    public Boolean getJavaObject(String str) {
        return Boolean.valueOf(str);
    }

    public void setEnabled(UUID update, boolean toggled) {
        updateValueAsync(update, toggled);
    }

    public boolean isEnabled(UUID check) {
        return (contains(check) ? getValue(check) : true);
    }

}
