package groovy.data

import groovy.data.pool.DatabaseConnectionPool

import java.sql.Connection

class DatabaseConnection {
    static Connection getConnection() {
        return DatabaseConnectionPool.getInstance().getConnection()
    }
}
