package groovy.data

import java.sql.Connection
import java.sql.DriverManager

class DatabaseConnection {
    private static final String URL = System.getenv("LINKETINDER_DB_URL") ?: "jdbc:postgresql://localhost:5432/linketinder"
    private static final String USER = System.getenv("LINKETINDER_DB_USER") ?: "postgres"
    private static final String PASSWORD = System.getenv("LINKETINDER_DB_PASSWORD") ?: "7127"

    static Connection getConnection() {
        return DriverManager.getConnection(URL, USER, PASSWORD)
    }
}
