package Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.extern.log4j.Log4j2;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.util.ArrayList;

@Log4j2
public class CurrenciesService extends SQLConnection {
    private ObjectMapper objectMapper = new ObjectMapper();

    public void getAllCurrencies(HttpServletResponse response) throws IOException {

        ArrayNode jsonArray = objectMapper.createArrayNode();
        PrintWriter pw = response.getWriter();

        try {
            Statement statement = getConnection().createStatement();
            ResultSet rs = statement.executeQuery("SELECT * FROM currencies");
            while (rs.next()){
                ObjectNode rowObject = objectMapper.createObjectNode();

                rowObject.put("id",rs.getInt("id"));
                rowObject.put("code",rs.getString("code"));
                rowObject.put("fullname",rs.getString("fullname"));
                rowObject.put("sign",rs.getString("sign"));

                jsonArray.add(rowObject);
            }
            response.setStatus(200);
            String jsonString = jsonArray.toString();
            pw.print(jsonString);
            statement.close();
            rs.close();
        } catch (SQLException e) {
            response.setStatus(500);
            throw new RuntimeException(e);
        }
    }
    public void addNewCurrency(HttpServletRequest request,HttpServletResponse response) throws SQLException, IOException {

        PrintWriter pw = response.getWriter();
        ArrayNode jsonArray = objectMapper.createArrayNode();

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

        String queryInsert = "INSERT INTO Currencies(fullname,code,sign) VALUES('" + values.get(0) +"','" + values.get(1) + "','"
                + values.get(2) +"')";
        String querySelect = "SELECT * FROM Currencies c WHERE c.code = '" + values.get(1) + "'";
        try {
            Statement statement = getConnection().createStatement();
            statement.executeUpdate(queryInsert);
            ResultSet rs = statement.executeQuery(querySelect);
            if(rs.next()){
                ObjectNode rowObject = objectMapper.createObjectNode();

                rowObject.put("id",rs.getInt("id"));
                rowObject.put("code",rs.getString("code"));
                rowObject.put(
                        "fullname",rs.getString("fullname"));
                rowObject.put("sign",rs.getString("sign"));

                jsonArray.add(rowObject);
            }
            String jsonString = jsonArray.toString();
            pw.print(jsonString);
            response.setStatus(200);
            statement.close();
        }catch (SQLException e){
            response.setStatus(500);
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
