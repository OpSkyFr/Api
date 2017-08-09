package fr.dinnerwolph.api.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * @author Dinnerwolph
 */

public abstract class SkyCommand implements CommandExecutor {

    private Player player;

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (commandSender instanceof Player) {
            player = (Player) commandSender;
            if (!execute(player, command, s, strings))
                displayHelp();
            return true;
        } else {
            commandSender.sendMessage("La console n'est pas authoriser à utiliser cette commande.");
            return true;
        }
    }

    public abstract boolean execute(Player player, Command command, String label, String[] args);

    /**
     * display help on return false
     */
    protected void displayHelp() {

    }

    protected void sendMessage(String... args) {
        player.sendMessage(args);
    }

}
