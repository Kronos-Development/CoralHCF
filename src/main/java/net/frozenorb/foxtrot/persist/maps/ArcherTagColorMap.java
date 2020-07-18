package net.frozenorb.foxtrot.persist.maps;

import net.frozenorb.foxtrot.Foxtrot;
import net.frozenorb.foxtrot.persist.PersistMap;
import net.frozenorb.foxtrot.team.Team;
import org.bukkit.ChatColor;

import java.util.UUID;

public class ArcherTagColorMap extends PersistMap<ChatColor> {

    public ArcherTagColorMap() {
        super("ArcherTagColor", "ArcherTagColor");
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
        return (contains(check) ? getValue(check) : Foxtrot.getInstance().getServerHandler().getArcherTagColor());
    }

    public void setColor(UUID update, ChatColor color) {
        updateValueSync(update, color);
    }


}