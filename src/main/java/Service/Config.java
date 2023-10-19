package Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Properties;

public class Config {

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

    protected ArrayList<String> getValues(HttpServletRequest request, HttpServletResponse response) throws IOException {
        BufferedReader reader = request.getReader();
        String line;
        StringBuilder requestBody = new StringBuilder();

        while ((line = reader.readLine()) != null) {
            requestBody.append(line);
        }

        String requestBodyString = requestBody.toString();
        String[] formData = requestBodyString.split("&");
        ArrayList<String> values = new ArrayList<String>();

        for (String formDataPair : formData) {
            String[] pair = formDataPair.split("=");
            if (pair.length == 2) {
                String key = pair[0];
                String value = pair[1];
                values.add(value);
            }else {
                response.setStatus(400);
            }
        }
        return values;

    }
}
