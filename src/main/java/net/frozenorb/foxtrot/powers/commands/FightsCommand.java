package net.frozenorb.foxtrot.powers.commands;

import com.minexd.zoot.util.CC;
import net.frozenorb.foxtrot.Foxtrot;
import net.frozenorb.foxtrot.powers.PowersHandler;
import net.frozenorb.foxtrot.team.Team;
import net.frozenorb.qlib.command.Command;
import net.frozenorb.qlib.command.Param;
import org.bukkit.entity.Player;

import java.util.Objects;

/*
Made by Cody at 2:45 AM on 8/10/20
 */
public class FightsCommand {

    //a quick way to debug fights
    @Command(names = "fighting", permission = "foxtrot.managefights")
    public static void onFights(Player player) {
        PowersHandler powersHandler = Foxtrot.getInstance().getPowersHandler();

        if (PowersHandler.getInFights().size() == 0) {
            player.sendMessage(CC.translate("No fights currently"));
            return;
        }
        PowersHandler.getInFights().values().stream().map(Objects::nonNull).forEach(fight -> {
            player.sendMessage(fight.toString());
        });
    }

    @Command(names = {"addfight", "makefight", "createfight", "fightmake"}, permission = "foxtrot.managefights", hidden = true)
    public static void onFightAdd(Player player, @Param(name = "team1", tabCompleteFlags = {"no teams on"})Team team1, @Param(name = "team2", tabCompleteFlags = {"no teams on"})Team team2) {
        PowersHandler powersHandler = Foxtrot.getInstance().getPowersHandler();
        //Standard error checking procedure.
        if (team1 == null) {
            player.sendMessage(CC.translate("Team one is null or non existent"));
            return;
        }
        if (team2 == null) {
            player.sendMessage(CC.translate("Team two is null or non existent"));
            return;
        }
        if(team1.getMembers().size() == 0 || team2.getMembers().size() == 0) {
            player.kickPlayer(CC.translate("&cThere are no members on in one of the teams, are you on meth?"));
        }

        powersHandler.setInFight(team1, team2);
    }

    @Command(names = {"removefight", "killfight", "remfight", "fightremove"}, permission = "foxtrot.managefights", hidden = true)
    public static void onFightRemove(Player player, @Param(name = "team1", tabCompleteFlags = {"no teams on"})Team team1, @Param(name = "team2", tabCompleteFlags = {"no teams on"})Team team2) {
        PowersHandler powersHandler = Foxtrot.getInstance().getPowersHandler();
        //Standard error checking procedure.
        if (team1 == null) {
            player.sendMessage(CC.translate("Team one is null or non existent"));
            return;
        }
        if (team2 == null) {
            player.sendMessage(CC.translate("Team two is null or non existent"));
            return;
        }
        if(team1.getMembers().size() == 0 || team2.getMembers().size() == 0) {
            player.kickPlayer(CC.translate("&cThere are no members on in one of the teams, are you on meth?"));
        }

        powersHandler.removeFromFight(team1, team2);
    }

}
