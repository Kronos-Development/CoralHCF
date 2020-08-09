package net.frozenorb.foxtrot.powers;

import lombok.Getter;
import lombok.Setter;
import net.frozenorb.foxtrot.Foxtrot;
import net.frozenorb.foxtrot.team.Team;
import net.minecraft.server.v1_7_R4.Packet;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.HashMap;
import java.util.Map;

public class PowersListener implements Listener {
    @Getter @Setter private static Map<String, Boolean> inFights = new HashMap<>();

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

        if (affectedTeam.getMembers().size() < 10) return;
        if (attackerTeam.getMembers().size() < 10) return;

        inFights.put(attackerTeam.getName() + ":" + affectedTeam.getName(), true);
    }
}
