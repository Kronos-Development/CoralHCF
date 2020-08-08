package net.frozenorb.foxtrot.team.commands.team;

import net.frozenorb.foxtrot.Foxtrot;
import net.frozenorb.qlib.command.Command;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class TeamCommand {

    @Command(names={ "team", "t", "f", "faction", "fac" }, permission="")
    public static void team(Player sender) {

        if (!Foxtrot.getInstance().getServerHandler().isCustomHelp()) {
            sender.sendMessage(HCTEAMS_HELP);
            return;
        }

        String sectionColor = ChatColor.translateAlternateColorCodes('&', Foxtrot.getInstance().getServerHandler().getHelpSectionColor());
        String entryColor = ChatColor.translateAlternateColorCodes('&', Foxtrot.getInstance().getServerHandler().getHelpEntryColor());
        String infoColor = ChatColor.translateAlternateColorCodes('&', Foxtrot.getInstance().getServerHandler().getHelpInfoColor());

        String[] msg = {

                "§7§m-----------------------------------------------------",
                sectionColor + "§lTeam Help §7- " + infoColor + "To help you get started",
                "§7§m-----------------------------------------------------",


                sectionColor + "General Commands:",
                entryColor + "/t create <teamName> §7-" + infoColor + " Create a new team",
                entryColor + "/t accept <teamName> §7-" + infoColor + " Accept a pending invitation",
                entryColor + "/t lives add <amount> §7-" + infoColor + " Irreversibly add lives to your faction",
                entryColor + "/t leave §7-" + infoColor + " Leave your current team",
                entryColor + "/t home §7-" + infoColor + " Teleport to your team home",
                entryColor + "/t stuck §7-" + infoColor + " Teleport out of enemy territory",
                entryColor + "/t deposit <amount|all> §7-" + infoColor + " Deposit money into your team balance",


                "",
                sectionColor + "Information Commands:",
                entryColor + "/t who [player|teamName] §7-" + infoColor + " Display team information",
                entryColor + "/t map §7-" + infoColor + " Show nearby claims (identified by pillars)",
                entryColor + "/t list §7-" + infoColor + " Show list of teams online (sorted by most online)",


                "",
                sectionColor + "Captain Commands:",
                entryColor + "/t invite <player> §7-" + infoColor + " Invite a player to your team",
                entryColor + "/t uninvite <player> §7-" + infoColor + " Revoke an invitation",
                entryColor + "/t invites §7-" + infoColor + " List all open invitations",
                entryColor + "/t kick <player> §7-" + infoColor + " Kick a player from your team",
                entryColor + "/t claim §7-" + infoColor + " Start a claim for your team",
                entryColor + "/t subclaim §7-" + infoColor + " Show the subclaim help page",
                entryColor + "/t sethome §7-" + infoColor + " Set your team's home at your current location",
                entryColor + "/t withdraw <amount> §7-" + infoColor + " Withdraw money from your team's balance",
                entryColor + "/t announcement [message here] §7-" + infoColor + " Set your team's announcement",

                "",
                sectionColor + "Leader Commands:",
                entryColor + "/t coleader <add|remove> <player> §7-" + infoColor + " Add or remove a co-leader",
                entryColor + "/t captain <add|remove> <player> §7-" + infoColor + " Add or remove a captain",
                entryColor + "/t revive <player> §7-" + infoColor + " Revive a teammate using team lives",
                entryColor + "/t unclaim [all] §7-" + infoColor + " Unclaim land",
                entryColor + "/t rename <newName> §7-" + infoColor + " Rename your team",
                entryColor + "/t disband §7-" + infoColor + " Disband your team",


                "§7§m-----------------------------------------------------",



        };
        sender.sendMessage(msg);
    }

    private static String[] HCTEAMS_HELP = {

        "§7§m-----------------------------------------------------",
                "§9§lTeam Help §7- §eTeam Help",
                "§7§m-----------------------------------------------------",


                "§9General Commands:",
                "§e/t create <teamName> §7- Create a new team",
                "§e/t accept <teamName> §7- Accept a pending invitation",
                "§e/t lives add <amount> §7- Irreversibly add lives to your faction",
                "§e/t leave §7- Leave your current team",
                "§e/t home §7- Teleport to your team home",
                "§e/t stuck §7- Teleport out of enemy territory",
                "§e/t deposit <amount§7|§eall> §7- Deposit money into your team balance",


                "",
                "§9Information Commands:",
                "§e/t who [player§7|§eteamName] §7- Display team information",
                "§e/t map §7- Show nearby claims (identified by pillars)",
                "§e/t list §7- Show list of teams online (sorted by most online)",


                "",
                "§9Captain Commands:",
                "§e/t invite <player> §7- Invite a player to your team",
                "§e/t uninvite <player> §7- Revoke an invitation",
                "§e/t invites §7- List all open invitations",
                "§e/t kick <player> §7- Kick a player from your team",
                "§e/t claim §7- Start a claim for your team",
                "§e/t subclaim §7- Show the subclaim help page",
                "§e/t sethome §7- Set your team's home at your current location",
                "§e/t withdraw <amount> §7- Withdraw money from your team's balance",
                "§e/t announcement [message here] §7- Set your team's announcement",

                "",
                "§9Leader Commands:",

                "§e/t coleader <add|remove> <player> §7- Add or remove a co-leader",
                "§e/t captain <add|remove> <player> §7- Add or remove a captain",
                "§e/t revive <player> §7- Revive a teammate using team lives",
                "§e/t unclaim [all] §7- Unclaim land",
                "§e/t rename <newName> §7- Rename your team",
                "§e/t disband §7- Disband your team",


                "§7§m-----------------------------------------------------",



    };

}