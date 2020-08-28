package net.frozenorb.foxtrot.powers.events;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.frozenorb.foxtrot.team.Team;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

@AllArgsConstructor
@Getter
public class FightEndEvent extends Event {

    @Getter private static HandlerList handlerList = new HandlerList();

    private String id;
    private Team team1;
    private Team team2;

    @Override
    public HandlerList getHandlers() {
        return handlerList;
    }

}
