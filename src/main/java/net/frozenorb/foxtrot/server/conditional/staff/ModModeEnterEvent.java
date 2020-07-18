package net.frozenorb.foxtrot.server.conditional.staff;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

public class ModModeEnterEvent extends PlayerEvent
{
    private static HandlerList handlerList;

    public ModModeEnterEvent(Player player) {
        super(player);
    }

    public HandlerList getHandlers() {
        return ModModeEnterEvent.handlerList;
    }

    public static HandlerList getHandlerList() {
        return ModModeEnterEvent.handlerList;
    }

    static {
        ModModeEnterEvent.handlerList = new HandlerList();
    }
}
