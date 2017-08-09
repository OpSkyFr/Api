package fr.dinnerwolph.api.commands;

import fr.dinnerwolph.api.command.SkyCommand;
import org.bukkit.command.Command;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

/**
 * @author Dinnerwolph
 */

public class Ping extends SkyCommand {

    @Override
    public boolean execute(Player player, Command command, String label, String[] args) {
        sendMessage(((CraftPlayer) player).getHandle().ping + "");
        return false;
    }
}
