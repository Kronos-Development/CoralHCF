package net.frozenorb.foxtrot.powers;

import com.minexd.zoot.util.CC;
import lombok.Getter;
import lombok.Setter;
import net.frozenorb.foxtrot.Foxtrot;
import net.frozenorb.foxtrot.team.Team;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class PowersHandler {

    @Getter @Setter private static Map<String, Boolean> inFights = new HashMap<>();

    public void setInFight(Team team1, Team team2) {
        inFights.put(team1.getName(), true);
        inFights.put(team2.getName(), true);
    }

    public void removeFromFight(Team team1, Team team2) {
        if (inFights.containsKey(team1.getName())) {
            inFights.remove(team1.getName());
            return;
        }
        if (inFights.containsKey(team2.getName())) {
            inFights.remove(team2.getName());
            return;
        }

    }


}
