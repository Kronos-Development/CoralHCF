package net.frozenorb.foxtrot.persist.maps;

import net.frozenorb.foxtrot.persist.PersistMap;
import org.bukkit.ChatColor;

import java.util.UUID;

public class FocusColorMap extends PersistMap<ChatColor> {

    public FocusColorMap() {
        super("FocusColor", "FocusColor");
    }

    @Override
    public String getRedisValue(ChatColor color) {
        return color.name();
    }

    @Override
    public ChatColor getJavaObject(String str) {
        return ChatColor.valueOf(str);
    }

    @Override
    public Object getMongoValue(ChatColor color) {
        return (color.name());
    }

    public ChatColor getColor(UUID check) {
        return (contains(check) ? getValue(check) : ChatColor.LIGHT_PURPLE);
    }

    public void setColor(UUID update, ChatColor color) {
        updateValueSync(update, color);
    }


}