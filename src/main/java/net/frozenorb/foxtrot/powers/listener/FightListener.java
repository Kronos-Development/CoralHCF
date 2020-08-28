package net.frozenorb.foxtrot.powers.listener;

import com.minexd.zoot.util.CC;
import net.frozenorb.foxtrot.Foxtrot;
import net.frozenorb.foxtrot.powers.events.FightEndEvent;
import net.frozenorb.foxtrot.powers.events.FightStartEvent;
import net.frozenorb.foxtrot.team.Team;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.junit.internal.runners.statements.FailOnTimeout;

public class FightListener implements Listener {

    @EventHandler
    public void onStart(FightStartEvent event) {
        Bukkit.broadcastMessage(CC.translate("&6[TeamFights] &9" + event.getTeam1().getName() + " and " + event.getTeam2().getName() + " have entered a fight."));

        new BukkitRunnable() {

            @Override
            public void run() {
                Foxtrot.getInstance().getFightHandler().removeFight(event.getId());
            }
        }.runTaskLater(Foxtrot.getInstance(), 20 * 60); // 30m
    }

    @EventHandler
    public void onEnd(FightEndEvent event) {
        Bukkit.broadcastMessage(CC.translate("&6[TeamFights] &9" + event.getTeam1().getName() + " and " + event.getTeam2().getName() + " have exited a fight."));
        if (Foxtrot.getInstance().getFightHandler().getFight(event.getId()).getDeathsduring() != null) {
            if (Foxtrot.getInstance().getFightHandler().getFight(event.getId()).getDeathsduring().get(event.getTeam1()) > Foxtrot.getInstance().getFightHandler().getFight(event.getId()).getDeathsduring().get(event.getTeam2())) {
                Bukkit.broadcastMessage(CC.translate("&6[TeamFights] &9" + event.getTeam1().getName() + " has won the fight!"));
                event.getTeam1().setTeamfightsWon(event.getTeam1().getTeamfightsWon() + 1);
            } else {
                Bukkit.broadcastMessage(CC.translate("&6[TeamFights] &9" + event.getTeam2().getName() + " has won the fight!"));
                event.getTeam2().setTeamfightsWon(event.getTeam2().getTeamfightsWon() + 1);
            }
        }

    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        Team team = Foxtrot.getInstance().getTeamHandler().getTeam(event.getEntity());
        if (team == null) return;

        if (Foxtrot.getInstance().getFightHandler().getFights().contains(team.getName())) {
            if (Foxtrot.getInstance().getFightHandler().getFight(team.getName()).getDeathsduring().get(team) != null) {
                Foxtrot.getInstance().getFightHandler().getFight(team.getName()).getDeathsduring().put(team, Foxtrot.getInstance().getFightHandler().getFight(team.getName()).getDeathsduring().get(team) + 1);
            } else {
                Foxtrot.getInstance().getFightHandler().getFight(team.getName()).getDeathsduring().put(team, 1);
            }

        }

    }
}
