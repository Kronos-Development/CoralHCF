package net.frozenorb.foxtrot.events.conquest;

import lombok.Getter;
import lombok.Setter;
import net.frozenorb.foxtrot.Foxtrot;
import net.frozenorb.foxtrot.events.conquest.game.ConquestGame;
import org.bukkit.ChatColor;

public class ConquestHandler {

    public static String PREFIX = ChatColor.YELLOW + "[Conquest]";

    public static int POINTS_DEATH_PENALTY = 20;
    public static String KOTH_NAME_PREFIX = "Conquest-";
    public static int TIME_TO_CAP = 30;

    @Getter @Setter private ConquestGame game;

    public static int getPointsToWin() {
        return Foxtrot.getInstance().getConfig().getInt("conquestWinPoints", 250);
    }
}