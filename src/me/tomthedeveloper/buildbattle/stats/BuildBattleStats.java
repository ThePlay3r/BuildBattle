package me.tomthedeveloper.buildbattle.stats;

import me.tomthedeveloper.buildbattle.Main;
import me.tomthedeveloper.buildbattle.handlers.ConfigurationManager;
import me.tomthedeveloper.buildbattle.handlers.UserManager;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Created by Tom on 30/12/2015.
 */
public enum BuildBattleStats {
    BLOCKS_PLACED("blocksplaced"), BLOCKS_BROKEN("blocksbroken"), GAMES_PLAYED("gamesplayed"), WINS("wins"), LOSES("loses");

    public static Main plugin;
    private String name;


    BuildBattleStats(String name) {
        this.name = name;
    }

    private static Map sortByValue(Map unsortMap) {
        List list = new LinkedList(unsortMap.entrySet());

        list.sort((o1, o2) -> ((Comparable) ((Map.Entry) (o1)).getValue()).compareTo(((Map.Entry) (o2)).getValue()));

        Map sortedMap = new LinkedHashMap();
        for(Iterator it = list.iterator(); it.hasNext(); ) {
            Map.Entry entry = (Map.Entry) it.next();
            sortedMap.put(entry.getKey(), entry.getValue());
        }
        return sortedMap;
    }

    public Map<UUID, Integer> getStats() {
        if(plugin.isDatabaseActivated()) return plugin.getMySQLDatabase().getColumn(name);
        else {
            FileConfiguration config = ConfigurationManager.getConfig("STATS");
            Map<UUID, Integer> stats = new LinkedHashMap<>();
            for(String string : config.getKeys(false)) {
                stats.put(UUID.fromString(string), config.getInt(string + "." + name));
            }
            return sortByValue(stats);
        }

    }

    public int getStat(Player player) {
        return UserManager.getUser(player.getUniqueId()).getInt(name);
    }


}
