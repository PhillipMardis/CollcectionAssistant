import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnector {
    private static final String DB_URL = "jdbc:postgresql://localhost:5432/collection_assistant";
    private static final String DB_USERNAME = "postgres";
    private static final String DB_PASSWORD = "12345";
    public static Connection connection;

    // Method to establish a database connection
    public static Connection connect() throws SQLException {
        connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
        return connection;
    }

    // Method to close the database connection
    public void disconnect() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }
}
