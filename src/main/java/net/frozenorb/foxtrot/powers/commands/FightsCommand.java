package net.frozenorb.foxtrot.powers.commands;

import com.minexd.zoot.util.CC;
import mkremins.fanciful.FancyMessage;
import net.frozenorb.foxtrot.Foxtrot;
import net.frozenorb.foxtrot.powers.Fight;
import net.frozenorb.foxtrot.team.Team;
import net.frozenorb.qlib.command.Command;
import net.frozenorb.qlib.command.Param;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.concurrent.atomic.AtomicInteger;

/*
Made by Cody at 2:45 AM on 8/10/20
 */
public class FightsCommand {

    AtomicInteger i = new AtomicInteger();
    //a quick way to debug fights
    @Command(names = "fights", permission = "foxtrot.managefights")
    public static void onFights(Player player) {

        if (Foxtrot.getInstance().getFightHandler().getFights().size() == 0) {
            player.sendMessage(CC.translate("&cNo fights currently"));
            return;
        }

        FancyMessage fancyMessage = new FancyMessage();

        player.sendMessage(CC.MENU_BAR);
        player.sendMessage(CC.translate("&cActive Fights"));
        player.sendMessage(CC.MENU_BAR);
        for (Fight fight : Foxtrot.getInstance().getFightHandler().getFights()) {
            Team team = fight.getTeam1();
            Team team2 = fight.getTeam2();

            fancyMessage.text(CC.translate("&cAttacking: ") + team.getName() + "(" + team.getOnlineMembers().size() + ") " + ChatColor.GREEN + " Defending: " + team2.getName() + "(" + team2.getOnlineMembers().size() + ")")
                    .tooltip(ChatColor.GREEN + "More info")
                    .command("/fight info " + team.getName() + " " + team2.getName()).send(player);
        }
        player.sendMessage(CC.MENU_BAR);
    }

    @Command(names = "fight info", permission = "foxtrot.managefights")
    public static void onFightInfo(Player player, @Param(name = "team") Team team, @Param(name = "team2") Team team2) {

        Fight fight = Foxtrot.getInstance().getFightHandler().getFight(team + ":" + team2);

        player.sendMessage(CC.translate("&cFight Statistics:"));
        player.sendMessage(CC.translate("&c" + team.getName() + " hits: " + fight.getHits().get(team)));
        player.sendMessage(CC.translate("&c" + team2.getName() + " hits: " + fight.getHits().get(team2)));
        player.sendMessage(CC.translate("&c" + (fight.getHits().get(team) > fight.getHits().get(team2) ? team.getName() + " is winning" : team2.getName() + " is winning")));

    }

    @Command(names = {"addfight", "makefight", "createfight", "fightmake"}, permission = "foxtrot.managefights", hidden = true)
    public static void onFightAdd(Player player, @Param(name = "team1", tabCompleteFlags = {"no teams on"})Team team1, @Param(name = "team2", tabCompleteFlags = {"no teams on"})Team team2) {
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
            return;
        }

        Fight fight = new Fight(team1.getName() + ":" + team2.getName(), team1, team2, 0L, 0);
        Foxtrot.getInstance().getFightHandler().createFight(fight);
    }

    @Command(names = {"removefight", "killfight", "remfight", "fightremove"}, permission = "foxtrot.managefights", hidden = true)
    public static void onFightRemove(Player player, @Param(name = "team1", tabCompleteFlags = {"no teams on"})Team team1, @Param(name = "team2") Team team2) {
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

        Foxtrot.getInstance().getFightHandler().removeFight(team1.getName() + ":" + team2.getName());
    }

}
