package fr.dinnerwolph.api.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * @author Dinnerwolph
 */

public class Database {

    private Connection connection;

    public Database(String host, String database, int port, String user, String password) {
        connect(host, database, port, user, password);
    }

    public void connect(String host, String database, int port, String user, String password) {
        if (!isConnected())
            try {
                connection = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database + "?autoReconnect=true&useUnicode=yes", user, password);
            } catch (SQLException e) {
                e.printStackTrace();
            }
    }

    private boolean isConnected() {
        try {
            if (!((connection == null) || connection.isClosed() || connection.isValid(5))) return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void disconnect() {
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Connection getConnection() {
        return connection;
    }
}
