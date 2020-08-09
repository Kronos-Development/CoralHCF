package net.frozenorb.foxtrot.powers;

import net.frozenorb.foxtrot.Foxtrot;

public class PowersHandler {
    public String isInFight(String factionName) {
        if(!Foxtrot.getInstance().getTeamHandler().getTeams().contains(factionName)) return "LOL!";
        if(Foxtrot.getInstance().getTeamHandler().getTeam(factionName) == null) return "wrong name retard";

        return PowersListener.getInFights().containsKey(factionName) ? String.valueOf(PowersListener.getInFights().containsKey(factionName)) : "not fighting";
    }



}
