package net.frozenorb.foxtrot.powers.listener;
import com.minexd.zoot.util.CC;
import lombok.Getter;
import lombok.Setter;
import net.frozenorb.foxtrot.Foxtrot;
import net.frozenorb.foxtrot.powers.Fight;
import net.frozenorb.foxtrot.team.Team;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.HashMap;
import java.util.Map;

/*
Made by Cody at 1:17 AM on 8/26/20
 */

public class FightDamageListener implements Listener {

    public Map<Team, Integer> attackerHits = new HashMap<>();

    @EventHandler
    public void onFight (EntityDamageByEntityEvent event) {
        //just trying to make things as efficient as possible.
        if (!(event.getEntity() instanceof Player)) return;
        if (!(event.getDamager() instanceof Player)) return;

        Team attackerTeam = Foxtrot.getInstance().getTeamHandler().getTeam((Player) event.getDamager());
        Team affectedTeam = Foxtrot.getInstance().getTeamHandler().getTeam((Player) event.getEntity());

        //we're just making this clear here so we don't use excess materials to calculate fights @sorrow.cc.
        if (attackerTeam == null) return;
        if (affectedTeam == null) return;

        //Only 7 mans + count as teamfights. I'll include kills on tier list aswell but they will not be considered if its a teamfight


                if (Foxtrot.getInstance().getFightHandler().getFights().contains(attackerTeam.getName() + ":" + affectedTeam.getName()))
                    return;

        if (attackerTeam.getOnlineMembers().contains(((Player) event.getDamager()).getPlayer())) {
                if (attackerHits.get(attackerTeam) != null) {
                    if (attackerHits.get(attackerTeam) > 20) return;
                    attackerHits.put(attackerTeam, attackerHits.get(attackerTeam) + 1);
                    ((Player) event.getDamager()).sendMessage(String.valueOf(attackerHits.get(attackerTeam).intValue()));
                } else {
                    attackerHits.put(attackerTeam, 1);
                }
                if (attackerHits.get(attackerTeam) == 20) {
                if (Foxtrot.getInstance().getFightHandler().getFight(attackerTeam.getName() + ":" + affectedTeam.getName()) == null) {
                    Bukkit.broadcastMessage(CC.translate("&6[TeamFights] &9" + attackerTeam.getName() + " and " + affectedTeam.getName() + " have entered a fight."));

                    //create the fight
                    Fight fight = new Fight(attackerTeam.getName() + ":" + affectedTeam.getName(), attackerTeam, affectedTeam, 0L, 0);
                    fight.getHits().put(fight.getTeam1(), 1);
                    fight.getHits().put(fight.getTeam2(), 1);
                    Foxtrot.getInstance().getFightHandler().createFight(fight);

                }

            }
        }

            //Handle Hits
            if (Foxtrot.getInstance().getFightHandler().getFight(attackerTeam.getName() + ":" + affectedTeam.getName()) != null) {

                Fight fight = Foxtrot.getInstance().getFightHandler().getFight(attackerTeam.getName() + ":" + affectedTeam.getName());
                if (fight.getTeam1().getOnlineMembers().contains(event.getDamager())) {
                    if (fight.getHits().get(fight.getTeam1()) != null) {
                        fight.getHits().put(fight.getTeam1(), fight.getHits().get(fight.getTeam1()) + 1);
                    }

                }
                if (fight.getTeam2().getOnlineMembers().contains(event.getDamager())) {
                    if (fight.getHits().get(fight.getTeam2()) != null) {
                        fight.getHits().put(fight.getTeam2(), fight.getHits().get(fight.getTeam2()) + 1);
                    }
                }

            }

    }

}
