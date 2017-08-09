package fr.dinnerwolph.api.listener.player;

import fr.dinnerwolph.api.Api;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 * @author Dinnerwolph
 */

public class Quit implements Listener {

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Api.getSkyPlayer(event.getPlayer()).saveData();
        Api.getInstance().removeSkyPlayer(event.getPlayer());
        Api.getInstance().getScoreboardSigns().get(event.getPlayer()).destroy();
        Api.getInstance().getScoreboardSigns().remove(event.getPlayer());
    }
}
