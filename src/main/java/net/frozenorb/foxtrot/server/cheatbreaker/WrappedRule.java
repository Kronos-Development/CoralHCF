package net.frozenorb.foxtrot.server.cheatbreaker;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Created by vape on 6/7/2020 at 5:18 PM.
 */
@Getter
@AllArgsConstructor
public enum WrappedRule {
    VOICE_ENABLED("voiceEnabled", Boolean.class),
    MINIMAP_STATUS("minimapStatus", String.class),
    SERVER_HANDLES_WAYPOINTS("serverHandlesWaypoints", Boolean.class),
    COMPETITIVE_GAMEMODE("competitiveGame", Boolean.class);

    String rule;
    Class value;
}