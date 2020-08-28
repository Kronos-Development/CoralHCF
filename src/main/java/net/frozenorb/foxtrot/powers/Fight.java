package net.frozenorb.foxtrot.powers;

import lombok.Getter;
import lombok.Setter;
import net.frozenorb.foxtrot.team.Team;

import java.util.HashMap;
import java.util.Map;

public class Fight {

    @Getter private String id;

    @Getter private final Team team2;

    @Getter private final Team team1;

    @Setter
    @Getter private Long started;

    @Getter private Map<Team, Integer> hits;

    @Getter private Map<Team, Integer> deathsduring;

    public Fight(String id, Team team1, Team team2, Long started, int deathsduring) {
        this.id = id;
        this.team1 = team1;
        this.team2 = team2;
        this.started = started;
        this.hits = new HashMap<>();
        this.deathsduring = new HashMap<>();
    }

}
