package net.frozenorb.foxtrot.abilities;

import net.frozenorb.foxtrot.Foxtrot;
import net.frozenorb.foxtrot.abilities.menu.AbilityMenu;
import net.frozenorb.qlib.command.Command;
import net.frozenorb.qlib.command.Flag;
import net.frozenorb.qlib.command.Param;
import net.frozenorb.qlib.util.TimeUtils;
import net.minecraft.util.org.apache.commons.lang3.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AbilityCommands {

    @Command(names = {"ability", "ability menu", "abilities"}, permission = "")
    public static void abilityMenuCommand(Player sender) {
        if (Foxtrot.getInstance().getDeathbanMap().isDeathbanned(sender.getUniqueId())) {
            sender.sendMessage(ChatColor.RED + "You can't do this while you are deathbanned.");
            return;
        }

        if (!Foxtrot.getInstance().getAbilityHandler().isAbilitiesEnabled()) {
            sender.sendMessage("§cThis command can only be used if Abilities are enabled.");
            return;
        }

        new AbilityMenu().openMenu(sender);
    }

    @Command(names = {"ability list"}, permission = "foxtrot.ability.list")
    public static void abilityListCommand(CommandSender sender) {
        if (!Foxtrot.getInstance().getAbilityHandler().isAbilitiesEnabled()) {
            sender.sendMessage("§cThis command can only be used if Abilities are enabled.");
            return;
        }

        sender.sendMessage(ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH + StringUtils.repeat('-', 35));
        sender.sendMessage(ChatColor.DARK_AQUA.toString() + ChatColor.BOLD + "Abilities");
        for (AbstractAbility ability : Foxtrot.getInstance().getAbilityHandler().getAbilities()) {
            sender.sendMessage("");
            sender.sendMessage(ability.getDisplayName() + ChatColor.GRAY + " (ID: " + ability.getId() + ")");
            if (ability.getMaxUses() > 0)
                sender.sendMessage(ChatColor.AQUA + "   Maximum Uses: " + ChatColor.WHITE + ability.getMaxUses());
            sender.sendMessage(ChatColor.AQUA + "   Cooldown: " + ChatColor.WHITE + TimeUtils.formatIntoDetailedString((int)ability.getCooldown() / 1000));
        }
        sender.sendMessage(ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH + StringUtils.repeat('-', 35));
    }

    @Command(names = {"ability give"}, permission = "foxtrot.ability.give")
    public static void abilityGiveCommand(CommandSender sender, @Flag(value = {"s"}, description = "Give the ability item silently.") boolean silent, @Param(name = "target")Player target, @Param(name = "ability") String abilityName, @Param(name = "amount", defaultValue = "1") int amount) {
        if (!Foxtrot.getInstance().getAbilityHandler().isAbilitiesEnabled()) {
            sender.sendMessage("§cThis command can only be used if Abilities are enabled.");
            return;
        }

        AbstractAbility ability = Foxtrot.getInstance().getAbilityHandler().findAbility(abilityName);

        if (ability == null) {
            sender.sendMessage(ChatColor.RED + "Ability with the name " + abilityName + " could not be found.");
            return;
        }

        sender.sendMessage(ChatColor.YELLOW + "You gave " + ChatColor.RED + target.getDisplayName() + ChatColor.YELLOW + " a " + ability.getDisplayName() + ChatColor.YELLOW + "!");
        target.getInventory().addItem(ability.constructItem(amount));
        if (!silent)
            target.sendMessage(ChatColor.YELLOW + "You have been given a " + ability.getDisplayName() + ChatColor.YELLOW + " by " + ChatColor.RED + sender.getName());
    }

}