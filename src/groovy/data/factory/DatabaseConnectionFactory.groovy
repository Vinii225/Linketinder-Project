package groovy.data.factory

import java.sql.Connection
import java.sql.DriverManager

interface DatabaseConnectionFactory {
    Connection createConnection()
}

class PostgreSQLConnectionFactory implements DatabaseConnectionFactory {
    private final String url
    private final String user
    private final String password

    PostgreSQLConnectionFactory(String url, String user, String password) {
        this.url = url
        this.user = user
        this.password = password
    }

    @Override
    Connection createConnection() {
        return DriverManager.getConnection(url, user, password)
    }
}

class EnvironmentDatabaseConnectionFactory implements DatabaseConnectionFactory {
    private final DatabaseConnectionFactory delegate

    EnvironmentDatabaseConnectionFactory() {
        String url = System.getenv("LINKETINDER_DB_URL") ?: "jdbc:postgresql://localhost:5432/linketinder"
        String user = System.getenv("LINKETINDER_DB_USER") ?: "postgres"
        String password = System.getenv("LINKETINDER_DB_PASSWORD") ?: "7127"
        
        this.delegate = new PostgreSQLConnectionFactory(url, user, password)
    }

    @Override
    Connection createConnection() {
        return delegate.createConnection()
    }
}
