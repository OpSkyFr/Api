package fr.dinnerwolph.api.listener;

import fr.dinnerwolph.api.Api;
import fr.dinnerwolph.api.listener.player.Join;
import fr.dinnerwolph.api.listener.player.Quit;
import org.bukkit.plugin.PluginManager;

/**
 * @author Dinnerwolph
 */

public class ListenerManager {

    private final Api plugin;

    public ListenerManager() {
        plugin = Api.getInstance();
        PluginManager pluginManager = plugin.getServer().getPluginManager();
        pluginManager.registerEvents(new Join(), plugin);
        pluginManager.registerEvents(new Quit(), plugin);
    }
}
