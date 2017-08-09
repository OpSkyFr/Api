package fr.dinnerwolph.api.utils;

import net.minecraft.server.v1_12_R1.IChatBaseComponent;
import net.minecraft.server.v1_12_R1.PacketPlayOutTitle;
import net.minecraft.server.v1_12_R1.PlayerConnection;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

/**
 * @author Dinnerwolph
 */

public class TitleUtils {

    public static void senTitle() {

    }

    public static void sendSubTitle() {

    }

    private PacketPlayOutTitle getTitle() {
        PacketPlayOutTitle packet = new PacketPlayOutTitle();

        return packet;
    }

    public static void TitlePacketMessage(Player player, Integer fadeIn, Integer stay, Integer fadeOut, String title, String subtitle) {
        PlayerConnection connection = ((CraftPlayer) player).getHandle().playerConnection;
        PacketPlayOutTitle packetPlayOutTimes = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.TIMES, (IChatBaseComponent) null, fadeIn.intValue(), stay.intValue(), fadeOut.intValue());
        connection.sendPacket(packetPlayOutTimes);
        IChatBaseComponent titleMain;
        PacketPlayOutTitle packetPlayOutTitle;
        if (subtitle != null) {
            titleMain = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + subtitle + "\"}");
            packetPlayOutTitle = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.SUBTITLE, titleMain);
            connection.sendPacket(packetPlayOutTitle);
        }

        if (title != null) {
            titleMain = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + title + "\"}");
            packetPlayOutTitle = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.TITLE, titleMain);
            connection.sendPacket(packetPlayOutTitle);
        }

    }
}
