package fr.dinnerwolph.api.database.tables;

import fr.dinnerwolph.api.Api;
import fr.dinnerwolph.api.database.BasicSQLDatabase;
import org.bukkit.entity.Player;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

/**
 * @author Dinnerwolph
 */

public class PlayerInfo {

    private static BasicSQLDatabase database = new BasicSQLDatabase(Api.getData());

    public static void create(Player player) {
        if (!contains(player.getUniqueId()))
            database.execute("INSERT INTO `Players`(`UUID`, `PlayerName`, `Group`) VALUES (?,?,?)", new Object[]{player.getUniqueId().toString(), player.getName(), 0});
    }

    public static boolean contains(UUID uuid) {
        ResultSet resultSet = database.query("SELECT * FROM `Players` WHERE UUID=?", uuid.toString());
        try {
            if (resultSet.next())
                return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static Integer getGroups(UUID uuid) {
        ResultSet resultSet = database.query("SELECT `Group` FROM `Players` WHERE `UUID`=?", uuid.toString());

        try {
            if (resultSet.next())
                return resultSet.getInt("Group");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static Integer getOpId(UUID uuid) {
        ResultSet resultSet = database.query("SELECT `id` FROM `Players` WHERE `UUID`=?", uuid.toString());

        try {
            if (resultSet.next())
                return resultSet.getInt("id");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
