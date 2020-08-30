package net.frozenorb.foxtrot.settings;

import com.google.common.collect.ImmutableList;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.frozenorb.foxtrot.Foxtrot;
import net.frozenorb.foxtrot.settings.menu.button.SettingButton;
import net.frozenorb.foxtrot.tab.TabListMode;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.Collection;

@AllArgsConstructor
public enum  Setting {

    SCOREBOARD_STAFF_BOARD(
            ChatColor.LIGHT_PURPLE + "Staff Scorboard",
            ImmutableList.of(
                    ChatColor.BLUE + "Do you want to see",
                    ChatColor.BLUE + "your staff scoreboard?"
            ),
            Material.BLAZE_ROD,
            ChatColor.YELLOW + "Show scoreboard",
            ChatColor.YELLOW + "Hide scoreboard",
            true
    ) {
        @Override
        public void toggle(Player player) {
            boolean value = !Foxtrot.getInstance().getStaffBoardMap().isBoardToggled(player.getUniqueId());

            Foxtrot.getInstance().getStaffBoardMap().setBoardToggled(player.getUniqueId(), value);
            player.sendMessage(ChatColor.YELLOW + "You are now " + (value ? ChatColor.GREEN + "able" : ChatColor.RED + "unable") + ChatColor.YELLOW + " to see your staff scoreboard.");
        }

        @Override
        public boolean isEnabled(Player player) {
            return Foxtrot.getInstance().getStaffBoardMap().isBoardToggled(player.getUniqueId());
        }
    },

    PUBLIC_CHAT(
            ChatColor.LIGHT_PURPLE + "Public Chat",
            ImmutableList.of(
                    ChatColor.BLUE + "Do you want to see",
                    ChatColor.BLUE + "public chat messages?"
            ),
            Material.SIGN,
            ChatColor.YELLOW + "Show public chat",
            ChatColor.YELLOW + "Hide public chat",
            true
    ) {

        @Override
        public void toggle(Player player) {
            boolean value = !Foxtrot.getInstance().getToggleGlobalChatMap().isGlobalChatToggled(player.getUniqueId());

            Foxtrot.getInstance().getToggleGlobalChatMap().setGlobalChatToggled(player.getUniqueId(), value);
            player.sendMessage(ChatColor.YELLOW + "You are now " + (value ? ChatColor.GREEN + "able" : ChatColor.RED + "unable") + ChatColor.YELLOW + " to see global chat messages.");
        }

        @Override
        public boolean isEnabled(Player player) {
            return Foxtrot.getInstance().getToggleGlobalChatMap().isGlobalChatToggled(player.getUniqueId());
        }

    },
    LFF_MESSAGES(
            ChatColor.LIGHT_PURPLE + "LFF Messages",
            ImmutableList.of(
                    ChatColor.BLUE + "Do you want to see",
                    ChatColor.BLUE + "LFF messages?"
            ),
            Material.EYE_OF_ENDER,
            ChatColor.YELLOW + "Show LFF messages",
            ChatColor.YELLOW + "Hide LFF messages",
            true
    ) {

        @Override
        public void toggle(Player player) {
            boolean value = !Foxtrot.getInstance().getToggleLFFMessageMap().isEnabled(player.getUniqueId());

            Foxtrot.getInstance().getToggleLFFMessageMap().setEnabled(player.getUniqueId(), value);
            player.sendMessage(ChatColor.YELLOW + "You are now " + (value ? ChatColor.GREEN + "able" : ChatColor.RED + "unable") + ChatColor.YELLOW + " to see LFF messages.");
        }

        @Override
        public boolean isEnabled(Player player) {
            return Foxtrot.getInstance().getToggleGlobalChatMap().isGlobalChatToggled(player.getUniqueId());
        }

    },
    TAB_LIST(
            ChatColor.LIGHT_PURPLE + "Tab List Info",
            ImmutableList.of(
                    ChatColor.BLUE + "Do you want to see",
                    ChatColor.BLUE + "extra info on your",
                    ChatColor.BLUE + "tab list?"
            ),
            Material.ENCHANTED_BOOK,
            ChatColor.YELLOW + "",
            ChatColor.YELLOW + "",
            true
    ) {

        @Override
        public void toggle(Player player) {
            TabListMode mode = SettingButton.next(Foxtrot.getInstance().getTabListModeMap().getTabListMode(player.getUniqueId()));

            Foxtrot.getInstance().getTabListModeMap().setTabListMode(player.getUniqueId(), mode);
            player.sendMessage(ChatColor.YELLOW + "You've set your tab list mode to " + ChatColor.LIGHT_PURPLE + mode.getName() + ChatColor.YELLOW + ".");
        }

        @Override
        public boolean isEnabled(Player player) {
            return true;
        }

    }, FOUND_DIAMONDS(
            ChatColor.LIGHT_PURPLE + "Found Diamonds",
            ImmutableList.of(
                    ChatColor.BLUE + "Do you want to see",
                    ChatColor.BLUE + "found-diamonds messages?"
            ),
            Material.DIAMOND,
            ChatColor.YELLOW + "Show messages",
            ChatColor.YELLOW + "Hide messages",
            true
    ) {

        @Override
        public void toggle(Player player) {
            boolean value = !Foxtrot.getInstance().getToggleFoundDiamondsMap().isFoundDiamondToggled(player.getUniqueId());

            Foxtrot.getInstance().getToggleFoundDiamondsMap().setFoundDiamondToggled(player.getUniqueId(), value);
            player.sendMessage(ChatColor.YELLOW + "You are now " + (value ? ChatColor.GREEN + "able" : ChatColor.RED + "unable") + ChatColor.YELLOW + " to see found diamond messages.");
        }

        @Override
        public boolean isEnabled(Player player) {
            return Foxtrot.getInstance().getToggleFoundDiamondsMap().isFoundDiamondToggled(player.getUniqueId());
        }

    },

    DEATH_MESSAGES(
            ChatColor.LIGHT_PURPLE + "Death Messages",
            ImmutableList.of(
                    ChatColor.BLUE + "Do you want to see",
                    ChatColor.BLUE + "death messages?"
            ),
            Material.SKULL_ITEM,
            ChatColor.YELLOW + "Show messages",
            ChatColor.YELLOW + "Hide messages",
            true
    ) {
        @Override
        public void toggle(Player player) {
            boolean value = !Foxtrot.getInstance().getToggleDeathMessageMap().areDeathMessagesEnabled(player.getUniqueId());

            Foxtrot.getInstance().getToggleDeathMessageMap().setDeathMessagesEnabled(player.getUniqueId(), value);
            player.sendMessage(ChatColor.YELLOW + "You are now " + (value ? ChatColor.GREEN + "able" : ChatColor.RED + "unable") + ChatColor.YELLOW + " to see death messages.");
        }

        @Override
        public boolean isEnabled(Player player) {
            return Foxtrot.getInstance().getToggleDeathMessageMap().areDeathMessagesEnabled(player.getUniqueId());
        }
    },

    AUTOMATICALLY_F_DISPLAY(
            ChatColor.LIGHT_PURPLE + "Automatic Faction Display",
            ImmutableList.of(
                    ChatColor.BLUE + "Do you want to",
                    ChatColor.BLUE + "run faction display",
                    ChatColor.BLUE + "when doing /f who?"
            ),
            Material.BEACON,
            ChatColor.YELLOW + "Run F Display",
            ChatColor.YELLOW + "Don't Run F Display",
            false
    ) {
        @Override
        public void toggle(Player player) {
            boolean value = !Foxtrot.getInstance().getFDisplayMap().isToggled(player.getUniqueId());

            Foxtrot.getInstance().getFDisplayMap().setToggled(player.getUniqueId(), value);
            player.sendMessage(ChatColor.YELLOW + "F Display will " + (value ? ChatColor.GREEN + "now" : ChatColor.RED + "no longer") + ChatColor.YELLOW + " run when doing /f who.");
        }

        @Override
        public boolean isEnabled(Player player) {
            return Foxtrot.getInstance().getClassCooldownsMap().isCooldownsToggled(player.getUniqueId());
        }
    },

    FACTION_INVITES(
            ChatColor.LIGHT_PURPLE + "Faction Invites",
            ImmutableList.of(
                    ChatColor.BLUE + "Do you want others",
                    ChatColor.BLUE + "to be able to invite",
                    ChatColor.BLUE + "you to their faction?"
            ),
            Material.BEACON,
            ChatColor.YELLOW + "Allow Invites",
            ChatColor.YELLOW + "Don't Allow Invites",
            true
    ) {
        @Override
        public void toggle(Player player) {
            boolean value = !Foxtrot.getInstance().getReceiveFactionInviteMap().isToggled(player.getUniqueId());

            Foxtrot.getInstance().getReceiveFactionInviteMap().setToggled(player.getUniqueId(), value);
            player.sendMessage(ChatColor.YELLOW + "You will " + (value ? ChatColor.GREEN + "now" : ChatColor.RED + "no longer") + ChatColor.YELLOW + " be able to see faction invites.");
        }

        @Override
        public boolean isEnabled(Player player) {
            return Foxtrot.getInstance().getClassCooldownsMap().isCooldownsToggled(player.getUniqueId());
        }
    },

    SEE_CB_NOTIS(
            ChatColor.LIGHT_PURPLE + "Cheatbreaker Notifications",
            ImmutableList.of(
                    ChatColor.BLUE + "Do you want to",
                    ChatColor.BLUE + "see cheatbreaker notifications"
            ),
            Material.BEACON,
            ChatColor.YELLOW + "See Notifications",
            ChatColor.YELLOW + "Don't see Notifications",
            false
    ) {
        @Override
        public void toggle(Player player) {
            boolean value = !Foxtrot.getInstance().getCheatbreakerNotificationMap().isToggled(player.getUniqueId());

            Foxtrot.getInstance().getCheatbreakerNotificationMap().setToggled(player.getUniqueId(), value);
            player.sendMessage(ChatColor.YELLOW + "Cheatbreaker Notifications " + (value ? ChatColor.GREEN + "now" : ChatColor.RED + "no longer") + ChatColor.YELLOW + " shown.");
        }

        @Override
        public boolean isEnabled(Player player) {
            return Foxtrot.getInstance().getClassCooldownsMap().isCooldownsToggled(player.getUniqueId());
        }
    },;

    @Getter private String name;
    @Getter private Collection<String> description;
    @Getter private Material icon;
    @Getter private String enabledText;
    @Getter private String disabledText;
    private boolean defaultValue;

    // Using @Getter means the method would be 'isDefaultValue',
    // which doesn't correctly represent this variable.
    public boolean getDefaultValue() {
        return (defaultValue);
    }

    public SettingButton toButton() {
        return new SettingButton(this);
    }

    public abstract void toggle(Player player);

    public abstract boolean isEnabled(Player player);

}
