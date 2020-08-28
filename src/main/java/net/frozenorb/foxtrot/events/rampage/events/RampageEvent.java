package net.frozenorb.foxtrot.events.rampage.events;

import lombok.Getter;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class RampageEvent extends Event {
    private static HandlerList handlers = new HandlerList();

    public HandlerList getHandlers() {
        return (handlers);
    }

    public static HandlerList getHandlerList() {
        return (handlers);
    }
}
