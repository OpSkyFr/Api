package fr.dinnerwolph.api.database;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author Dinnerwolph
 */

public interface SQLDatabase {

    Connection getConnection();

    void execute(SQLOperationExecutor sqlOperationExecutor);

    <T> T query(SQLOperationReturn<T> operationReturn);

    interface SQLOperationExecutor {
        void execute(Connection connection) throws SQLException;
    }

    interface SQLOperationReturn<T> {
        T execute(Connection connection) throws SQLException;
    }
}
