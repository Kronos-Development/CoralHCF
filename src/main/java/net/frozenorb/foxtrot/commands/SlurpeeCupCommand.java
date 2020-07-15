package net.frozenorb.foxtrot.commands;

import com.google.common.collect.ImmutableList;
import com.minexd.zoot.Zoot;
import net.frozenorb.qlib.command.Command;
import net.frozenorb.qlib.qLib;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.List;

public class SlurpeeCupCommand {

    private static final List<String> reasons = ImmutableList.of(
            "&cFucking &6double &egulp &acup &2for &ba &9fucking &dslurpee",
            "&cOhhhh Shiet",
            "&cYou do this everytime",
            "&cEverytime you make a big deal about it",
            "&cLets be friends, I always pay for it whats wrong",
            "&cTHIS ONE IS THE SLURPEE CUP",
            "&cI don't care about your slurpeecup",
            "&cLOOK WHAT YOU DID, YOU MADE A MESS!"
    );

    @Command(names= {"slurp", "slurpeecup", "fuckingslurpeecup", "biggulp"}, permission = "")
    public static void slurp (Player sender){
        String reason = reasons.get(qLib.RANDOM.nextInt(reasons.size()));
        sender.kickPlayer(ChatColor.translateAlternateColorCodes('&', reason));
    }
}
