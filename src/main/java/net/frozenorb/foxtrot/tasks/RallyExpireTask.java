package net.frozenorb.foxtrot.tasks;

import com.cheatbreaker.api.CheatBreakerAPI;
import net.frozenorb.foxtrot.Foxtrot;
import net.frozenorb.foxtrot.team.Team;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class RallyExpireTask extends BukkitRunnable {

    @Override
    public void run() {
        for (Team team : Foxtrot.getInstance().getTeamHandler().getTeams()) {
            if(team.isRallyActive()) return;
            else if(!team.isRallyActive() && team.getRally() != null) {
                for (Player onlineMember : team.getOnlineMembers()) {
                    CheatBreakerAPI.getInstance().removeWaypoint(onlineMember, team.getRally().getCbWaypoint());
                }
                team.setRally(null);
            }
        }
    }
}
