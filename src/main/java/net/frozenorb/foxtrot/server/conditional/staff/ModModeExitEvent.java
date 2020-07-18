package net.frozenorb.foxtrot.server.conditional.staff;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

public class ModModeExitEvent extends PlayerEvent
{
    private static HandlerList handlerList;

    public ModModeExitEvent(Player player) {
        super(player);
    }

    public HandlerList getHandlers() {
        return ModModeExitEvent.handlerList;
    }

    public static HandlerList getHandlerList() {
        return ModModeExitEvent.handlerList;
    }

    static {
        ModModeExitEvent.handlerList = new HandlerList();
    }
}
