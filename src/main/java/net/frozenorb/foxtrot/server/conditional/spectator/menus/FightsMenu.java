package net.frozenorb.foxtrot.server.conditional.spectator.menus;

import com.minexd.zoot.util.CC;
import net.frozenorb.foxtrot.Foxtrot;
import net.frozenorb.foxtrot.powers.Fight;
import net.frozenorb.foxtrot.team.Team;
import net.frozenorb.qlib.menu.Button;
import net.frozenorb.qlib.menu.Menu;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class FightsMenu extends Menu {

    @Override
    public String getTitle(Player player) {
        return CC.translate("&bTeamfight Spectate Menu");
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();
        int i = 0;

        for(Fight fight : Foxtrot.getInstance().getFightHandler().getFights()) {
            buttons.put(i++, new Button() {
                Team team = fight.getTeam1();
                Team team2 = fight.getTeam2();

                int hits1 = fight.getHits().get(team);
                int hits2 = fight.getHits().get(team2);
                String duration = Foxtrot.getInstance().getFightHandler().getDuration(fight.getId());

                @Override
                public String getName(Player player) {
                    return CC.translate("Fight Between " + team.getName() + " and " + team2.getName());
                }

                @Override
                public List<String> getDescription(Player player) {
                    List<String> lore = new ArrayList<>();

                    lore.add(CC.MENU_BAR);
                    lore.add(CC.translate("&b" + team.getName() + "'s Members: " + team.getOnlineMembers().size()));
                    lore.add(CC.translate("&b" + team2.getName() + "'s Members: " + team2.getOnlineMembers().size()));
                    lore.add("");
                    lore.add(CC.translate("&b" + team.getName() + "'s Hits: " + hits1));
                    lore.add(CC.translate("&b" + team2.getName() + "'s Hits: " + hits2));
                    lore.add(CC.MENU_BAR);

                    return lore;
                }

                @Override
                public Material getMaterial(Player player) {
                    return Material.PAPER;
                }
            });
        }

        return buttons;
    }
}
