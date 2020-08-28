package net.frozenorb.foxtrot.events.rampage.commands;

import net.frozenorb.foxtrot.Foxtrot;
import net.frozenorb.foxtrot.events.rampage.events.RampageEvent;
import net.frozenorb.qlib.command.Command;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.conversations.*;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

public class RampageCommand {

    @Command(names = "rampage start", permission = "op")
    public static void onStart(Player player) {
        Foxtrot.getInstance().getRampageHandler().setRampageActive(true);
        Bukkit.getPluginManager().callEvent(new RampageEvent());

        new BukkitRunnable() {

            @Override
            public void run() {
                Foxtrot.getInstance().getRampageHandler().setRampageActive(false);
            }
        }.runTaskLater(Foxtrot.getInstance(), 20 * 60 * 60);
    }

    @Command(names = "rampage stop", permission = "op")
    public static void onStop(Player player) {

        ConversationFactory factory = new ConversationFactory(Foxtrot.getInstance()).withModality(true).withPrefix(new NullConversationPrefix()).withFirstPrompt(new StringPrompt() {

            public String getPromptText(ConversationContext context) {
                return "§aAre you sure you want to stop rampage? Type §byes§a to confirm or §cno§a to quit.";
            }

            @Override
            public Prompt acceptInput(ConversationContext cc, String s) {
                if (s.equalsIgnoreCase("yes")) {
                    Foxtrot.getInstance().getRampageHandler().setRampageActive(false);
                    Bukkit.getOnlinePlayers().forEach(player1 -> {
                        player1.removePotionEffect(PotionEffectType.INCREASE_DAMAGE);
                    });
                    cc.getForWhom().sendRawMessage(ChatColor.GREEN + "Rampage Deactivated");
                    return Prompt.END_OF_CONVERSATION;
                }

                if (s.equalsIgnoreCase("no")) {
                    cc.getForWhom().sendRawMessage(ChatColor.GREEN + "Cancelled.");
                    return Prompt.END_OF_CONVERSATION;
                }

                cc.getForWhom().sendRawMessage(ChatColor.GREEN + "Unrecognized response. Type §b/yes§a to confirm or §c/no§a to quit.");
                return Prompt.END_OF_CONVERSATION;
            }

        }).withLocalEcho(false).withEscapeSequence("/no").withTimeout(10).thatExcludesNonPlayersWithMessage("Go away evil console!");

        Conversation con = factory.buildConversation(player);
        player.beginConversation(con);

    }
}
