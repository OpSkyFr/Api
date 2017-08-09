package fr.dinnerwolph.api.player;

import fr.dinnerwolph.api.Api;
import fr.dinnerwolph.api.database.tables.OpSkyBlock;
import fr.dinnerwolph.api.database.tables.PlayerInfo;
import fr.dinnerwolph.api.groups.Groups;
import net.minecraft.server.v1_12_R1.EntityPlayer;
import org.bukkit.craftbukkit.v1_12_R1.CraftServer;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.util.UUID;

/**
 * @author Dinnerwolph
 */

public class SkyPlayer extends CraftPlayer {

    private Player player;
    private UUID uuid;
    private int groupId;
    private int opid;
    private int opCoins;

    public SkyPlayer(CraftServer server, EntityPlayer entity) {
        super(server, entity);
        player = entity.getBukkitEntity();
        uuid = player.getUniqueId();
        load();
    }

    private void load() {
        groupId = PlayerInfo.getGroups(uuid);
        opid = PlayerInfo.getOpId(uuid);
        OpSkyBlock.create(opid);
        opCoins = OpSkyBlock.getOpCoins(opid);
    }

    public void saveData() {
        OpSkyBlock.setOpCoins(opid, opCoins);
    }

    public Integer getGroupId() {
        return groupId;
    }

    public Groups getGroup() {
        return Api.getInstance().getGroups(groupId);
    }

    public int getOpCoins() {
        return opCoins;
    }

    public void addOpCoins(int count) {
        opCoins = opCoins + count;
    }

    public void removeOpCoins(int count) {
        opCoins = opCoins - count;
    }
}
