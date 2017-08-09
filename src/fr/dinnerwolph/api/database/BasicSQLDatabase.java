package fr.dinnerwolph.api.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * @author Dinnerwolph
 */

public class BasicSQLDatabase implements SQLDatabase {

    Database database;

    public BasicSQLDatabase(Database database) {
        this.database = database;
    }

    @Override
    public Connection getConnection() {
        return database.getConnection();
    }

    public void execute(String s, Object... args) {
        execute(connection -> {
            PreparedStatement statement = connection.prepareStatement(s);
            for (int i = 0; i < args.length; i++) {
                statement.setObject(i + 1, args[i]);
            }
            statement.execute();
        });
    }

    @Override
    public void execute(SQLOperationExecutor sqlOperationExecutor) {
        try {
            sqlOperationExecutor.execute(getConnection());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public <T> T query(String s, Object... args) {
        return query(operationReturn -> {
            T result = null;
            PreparedStatement statement = getConnection().prepareStatement(s);
            for (int i = 0; i < args.length; i++) {
                statement.setObject(i + 1, args[i]);
            }
            result = (T) statement.executeQuery();
            return result;
        });
    }

    @Override
    public <T> T query(SQLOperationReturn<T> operationReturn) {
        T result = null;
        try {
            result = operationReturn.execute(getConnection());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }
}
