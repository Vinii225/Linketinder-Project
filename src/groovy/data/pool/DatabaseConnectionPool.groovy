package groovy.data.pool

import groovy.data.factory.DatabaseConnectionFactory
import groovy.data.factory.EnvironmentDatabaseConnectionFactory

import java.sql.Connection

class DatabaseConnectionPool {
    private static final DatabaseConnectionPool INSTANCE = new DatabaseConnectionPool()
    
    private final DatabaseConnectionFactory factory
    
    private DatabaseConnectionPool() {
        this.factory = new EnvironmentDatabaseConnectionFactory()
    }
    
    static DatabaseConnectionPool getInstance() {
        return INSTANCE
    }
    
    Connection getConnection() {
        return factory.createConnection()
    }
}
