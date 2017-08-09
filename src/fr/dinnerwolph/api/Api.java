package fr.dinnerwolph.api;

import fr.dinnerwolph.api.commands.Ping;
import fr.dinnerwolph.api.database.Database;
import fr.dinnerwolph.api.database.tables.Group;
import fr.dinnerwolph.api.groups.Groups;
import fr.dinnerwolph.api.listener.ListenerManager;
import fr.dinnerwolph.api.player.SkyPlayer;
import fr.dinnerwolph.api.utils.ScoreboardSign;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author Dinnerwolph
 */

public class Api extends JavaPlugin {

    private static Api instance;
    private static Map<UUID, SkyPlayer> skyPlayers = new HashMap();
    private static Database database;
    private Map<Integer, Groups> groups = new HashMap();
    private Map<Player, ScoreboardSign> scoreboardSign = new HashMap();

    @Override

    public void onEnable() {
        instance = this;
        new ListenerManager();
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
            @Override
            public void run() {
                database.disconnect();
                database.connect("localhost", "OpSky", 3306, "root", "kokopops");
            }
        }, 144000L, 144000L);
        database = new Database("localhost", "OpSky", 3306, "root", "kokopops");
        new Group();
        getCommand("ping").setExecutor(new Ping());
    }


    public static Api getInstance() {
        return instance;
    }

    public static SkyPlayer getSkyPlayer(Player player) {
        return getSkyPlayer(player.getUniqueId());
    }

    public static SkyPlayer getSkyPlayer(UUID uuid) {
        return skyPlayers.get(uuid);
    }

    public void addSkyPlayer(SkyPlayer skyPlayer) {
        skyPlayers.put(skyPlayer.getUniqueId(), skyPlayer);
    }

    public void removeSkyPlayer(SkyPlayer skyPlayer) {
        removeSkyPlayer(skyPlayer.getUniqueId());
    }

    public void removeSkyPlayer(Player player) {
        removeSkyPlayer(player.getUniqueId());
    }

    public void removeSkyPlayer(UUID uuid) {
        skyPlayers.remove(uuid);
    }


    public static Database getData() {
        return database;
    }

    public void addGroups(Groups groups) {
        this.groups.put(groups.getLadder(), groups);
    }

    public Groups getGroups(int groupId) {
        return groups.get(groupId);
    }

    public Map<Player, ScoreboardSign> getScoreboardSigns() {
        return scoreboardSign;
    }
}
