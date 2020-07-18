package net.frozenorb.foxtrot.persist.maps;

import net.frozenorb.foxtrot.persist.PersistMap;
import org.bukkit.ChatColor;

import java.util.UUID;

public class TeamColorMap extends PersistMap<ChatColor> {

    public TeamColorMap() {
        super("TeamColor", "TeamColor");
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
        return (contains(check) ? getValue(check) : ChatColor.DARK_GREEN);
    }

    public void setColor(UUID update, ChatColor color) {
        updateValueSync(update, color);
    }


}