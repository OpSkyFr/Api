package fr.dinnerwolph.api.database.tables;

import fr.dinnerwolph.api.Api;
import fr.dinnerwolph.api.database.BasicSQLDatabase;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author Dinnerwolph
 */

public class OpSkyBlock {

    private static BasicSQLDatabase database = new BasicSQLDatabase(Api.getData());

    public static void create(int id) {
        if (!contains(id))
            database.execute("INSERT INTO `OpSkyblock`(`opid`) VALUES (?)", id);
    }

    public static boolean contains(int id) {
        ResultSet resultSet = database.query("SELECT * FROM `OpSkyblock` WHERE `opid`=?", id);
        try {
            if (resultSet.next())
                return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static Integer getOpCoins(int id) {
        ResultSet resultSet = database.query("SELECT `opcoins` FROM `OpSkyblock` WHERE `opid` =?", id);

        try {
            if (resultSet.next())
                return resultSet.getInt("opcoins");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static void setOpCoins(int id, int opcoins) {
        database.execute("UPDATE `OpSkyblock` SET `opcoins`=? WHERE `opid`=?", new Object[]{opcoins, id});
    }
}
