import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Central place that owns the JDBC connection to the MySQL database.
 *
 * SETUP:
 * 1. Install MySQL and run schema.sql once (see that file's header comment).
 * 2. Download the MySQL Connector/J driver (mysql-connector-j-x.x.x.jar)
 *    from https://dev.mysql.com/downloads/connector/j/ and put it on your
 *    classpath (same folder works fine for a quick run).
 * 3. Edit URL / USER / PASSWORD below to match your local MySQL setup.
 */
public class DatabaseConnection {

    private static final String URL =
            "jdbc:mysql://localhost:3306/blood_bank_db?useSSL=false&serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASSWORD = "your_password_here";

    private static Connection connection;

    private DatabaseConnection() {
    }

    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
            } catch (ClassNotFoundException e) {
                throw new SQLException(
                        "MySQL JDBC driver not found. Add mysql-connector-j-*.jar to your classpath.", e);
            }
        }
        return connection;
    }

    public static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException ignored) {
                // nothing to do on shutdown
            }
        }
    }
}
