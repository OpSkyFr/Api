package fr.dinnerwolph.api.listener.player;

import fr.dinnerwolph.api.Api;
import fr.dinnerwolph.api.database.tables.PlayerInfo;
import fr.dinnerwolph.api.player.SkyPlayer;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_12_R1.CraftServer;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

/**
 * @author Dinnerwolph
 */

public class Join implements Listener {

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerJoin(PlayerJoinEvent event) {
        PlayerInfo.create(event.getPlayer());
        Api.getInstance().addSkyPlayer(new SkyPlayer((CraftServer) Bukkit.getServer(), ((CraftPlayer) event.getPlayer()).getHandle()));
    }
}
