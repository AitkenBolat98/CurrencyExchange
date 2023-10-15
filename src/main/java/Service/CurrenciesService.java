package Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;

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

        String fullname = request.getParameter("fullname");
        String code = request.getParameter("code");
        String sign = request.getParameter("sign");

        if(fullname == null){

        }

        String query = "INSERT INTO Currencies(fullname,code,sign) VALUES('" + code +"','" + fullname + "','" +sign +"')";
        try {
            Statement statement = getConnection().createStatement();
            statement.executeUpdate(query);
            ResultSet rs = statement.executeQuery(query);
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
            throw new RuntimeException(e);
        }
    }
}
