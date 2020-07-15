package net.frozenorb.foxtrot.server.cheatbreaker;

import org.bukkit.entity.Player;

import java.lang.reflect.Method;

@SuppressWarnings({"unchecked", "rawtypes"})
public class CBAPIHook {

    public static boolean HOOKED;

    private static Class<?> API_CLASS, WAYPOINT_CLASS, CB_RULE_CLASS, LUNAR_RULE_CLASS;
    private static Object apiInstance;

    private static Method GIVE_STAFFMODS, DISABLE_STAFFMODS;
    private static Method SEND_WAYPOINT, REMOVE_WAYPOINT;
    private static Method CB_CHANGE_RULE, LUNAR_CHANGE_RULE;

    private static Method SET_COMP_GAME;

    public CBAPIHook() {
        try {
            API_CLASS = Class.forName("com.cheatbreaker.api.CheatBreakerAPI");
            WAYPOINT_CLASS = Class.forName("com.cheatbreaker.api.object.CBWaypoint");
            CB_RULE_CLASS = Class.forName("com.cheatbreaker.nethandler.obj.ServerRule");
            LUNAR_RULE_CLASS = Class.forName("com.moonsworth.client.nethandler.obj.ServerRule");

            apiInstance = API_CLASS.getMethod("getInstance").invoke(null);

            GIVE_STAFFMODS = API_CLASS.getMethod("giveAllStaffModules", Player.class);
            DISABLE_STAFFMODS = API_CLASS.getMethod("disableAllStaffModules", Player.class);

            SEND_WAYPOINT = API_CLASS.getMethod("sendWaypoint", Player.class, WAYPOINT_CLASS);
            REMOVE_WAYPOINT = API_CLASS.getMethod("removeWaypoint", Player.class, WAYPOINT_CLASS);

            CB_CHANGE_RULE = API_CLASS.getMethod("changeServerRule", Player.class, CB_RULE_CLASS, boolean.class);
            LUNAR_CHANGE_RULE = API_CLASS.getMethod("changeServerRule", Player.class, LUNAR_RULE_CLASS, boolean.class);

            SET_COMP_GAME = API_CLASS.getMethod("setCompetitiveGame", Player.class, boolean.class);

            System.out.println("Foxtrot hooked into CheatBreakerAPI.");
            HOOKED = true;
        } catch (Exception ex) {
            System.out.println("Foxtrot failed to hook into CheatBreakerAPI.");
        }
    }

    public static boolean giveAllStaffModules(Player player) {
        if (apiInstance == null || GIVE_STAFFMODS == null) return false;
        try {
            GIVE_STAFFMODS.invoke(apiInstance, player);
            return true;
        } catch (Exception ex) {}
        return false;
    }

    public static boolean disableAllStaffModules(Player player) {
        if (apiInstance == null || GIVE_STAFFMODS == null) return false;
        try {
            DISABLE_STAFFMODS.invoke(apiInstance, player);
            return true;
        } catch (Exception ex) {}
        return false;
    }

    public static boolean changeServerRule(Player player, WrappedRule rule, boolean value) {
        if (apiInstance == null || CB_CHANGE_RULE == null || LUNAR_CHANGE_RULE == null) return false;
        try {
            CB_CHANGE_RULE.invoke(apiInstance, player, getCbRule(rule), value);
            LUNAR_CHANGE_RULE.invoke(apiInstance, player, getLunarRule(rule), value);
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }

    public static boolean setCompetitiveGame(Player player, boolean value) {
        if (apiInstance == null || SET_COMP_GAME == null) return false;
        try {
            SET_COMP_GAME.invoke(apiInstance, player, value);
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }

    public static boolean sendWaypoint(Player player, WrappedWaypoint waypoint) {
        if (apiInstance == null || SEND_WAYPOINT == null) return false;
        try {
            SEND_WAYPOINT.invoke(apiInstance, player, constructWaypoint(waypoint));
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }

    public static boolean removeWaypoint(Player player, WrappedWaypoint waypoint) {
        if (apiInstance == null || REMOVE_WAYPOINT == null) return false;
        try {
            REMOVE_WAYPOINT.invoke(apiInstance, player, constructWaypoint(waypoint));
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }

    public static Object constructWaypoint(WrappedWaypoint wrapped) {
        if (apiInstance == null || WAYPOINT_CLASS == null) return null;
        try {
            return WAYPOINT_CLASS
                    .getConstructor(String.class, int.class, int.class, int.class, String.class, int.class, boolean.class, boolean.class)
                    .newInstance(wrapped.getName(), wrapped.getX(), wrapped.getY(), wrapped.getZ(), wrapped.getWorld(), wrapped.getColor(), wrapped.isForced(), wrapped.isVisible());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static Object getCbRule(WrappedRule rule) {
        if (apiInstance == null || CB_RULE_CLASS == null) return null;
        try {
            return Enum.valueOf((Class<Enum>)CB_RULE_CLASS, rule.name());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static Object getLunarRule(WrappedRule rule) {
        if (apiInstance == null || LUNAR_RULE_CLASS == null) return null;
        try {
            return Enum.valueOf((Class<Enum>)LUNAR_RULE_CLASS, rule.name());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

}