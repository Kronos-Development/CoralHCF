package net.frozenorb.foxtrot.powers;

import net.frozenorb.foxtrot.Foxtrot;
import net.frozenorb.foxtrot.team.Team;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

/*
Made by Cody at 2:24 AM on 8/10/20
 */

public class PowersListener implements Listener {

    @EventHandler
    public void onFight (EntityDamageByEntityEvent event) {
        //just trying to make things as efficient as possible.
        if(!(event.getEntity() instanceof Player)) return;
        if(!(event.getDamager() instanceof Player)) return;

        Team attackerTeam = Foxtrot.getInstance().getTeamHandler().getTeam((Player) event.getDamager());
        Team affectedTeam = Foxtrot.getInstance().getTeamHandler().getTeam((Player) event.getEntity());

        //we're just making this clear here so we don't use excess materials to calculate fights @sorrow.cc.
        if (attackerTeam == null) return;
        if (affectedTeam == null) return;

        //Only 5 mans + count as teamfights. I'll include kills on tier list aswell but they will not be considered if its a teamfight
        if (attackerTeam.getMembers().size() < 5) return;

        //FUCK HANK ALL MY HOMIES HATE HANK WE DON'T NEED TO STORE THIS AFTER RESTART THIS IS A FINE AND VALID WAY TO DO THIS THANKS
        /*
        note: we need to store won fights.
         */
        PowersHandler.getInFights().put(attackerTeam.getName(), true);
        PowersHandler.getInFights().put(affectedTeam.getName(), true);
    }
}
