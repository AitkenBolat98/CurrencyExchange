package Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class CurrencyService extends SQLConnection{

    private ObjectMapper objectMapper = new ObjectMapper();

    public void getCurrency(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException {

        ArrayNode jsonArray = objectMapper.createArrayNode();
        PrintWriter pw = response.getWriter();
        String[] url = request.getRequestURI().split("/");
        String currency = url[url.length-1];

        if(currency == null){
            response.setStatus(400);
        }

        try {
            Statement statement = getConnection().createStatement();
            ResultSet rs = statement.executeQuery("SELECT * FROM Currencies c WHERE c.code = " + currency);
            if(rs.next()){

                ObjectNode rowObject = objectMapper.createObjectNode();

                rowObject.put("id",rs.getInt("id"));
                rowObject.put("code",rs.getString("code"));
                rowObject.put("fullname",rs.getString("fullname"));
                rowObject.put("sign",rs.getString("sign"));

                jsonArray.add(rowObject);
                String jsonString = jsonArray.get(0).toString();
                response.setStatus(200);
                pw.print(jsonString);

                rs.close();
                statement.close();
            }else {
                pw.print("sdsdasd");
                response.setStatus(404);
            }
        }catch (SQLException e){
            response.setStatus(500);
        }


    }
}
