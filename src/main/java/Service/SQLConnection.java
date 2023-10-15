package Service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class SQLConnection {

    protected Connection getConnection() throws SQLException {

        Properties props = new Properties();
        props.setProperty("user", "postgres");
        props.setProperty("password", "3101");
        props.setProperty("characterEncoding", "UTF-8");

        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/CurrencyExchange",
                "postgres",
                "3101");
        return connection;
    }
}
