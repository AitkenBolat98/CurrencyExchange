package Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.extern.log4j.Log4j2;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
@Log4j2
public class ExchangeRateService extends SQLConnection{

    ObjectMapper objectMapper = new ObjectMapper();


    public void getExchangeRates(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException {

        ArrayNode arrayNode = objectMapper.createArrayNode();
        PrintWriter pw = response.getWriter();

        try {

            Statement statement = getConnection().createStatement();
            ResultSet rs = statement.executeQuery("SELECT c.*,er.rate FROM Currencies c INNER JOIN exchangerates er" +
                    " ON c.id = er.basecurrencyid");


            while (rs.next()) {
                ObjectNode rowObject = objectMapper.createObjectNode();
                rowObject.put("id", rs.getInt("id"));
                rowObject.put("code", rs.getString("code"));
                rowObject.put("fullname", rs.getString("fullname"));
                rowObject.put("sign", rs.getString("sign"));
                rowObject.put("rate", rs.getDouble("rate"));
                arrayNode.add(rowObject);
            }
            response.setStatus(200);
            pw.print(arrayNode.toString());
            statement.close();
            getConnection().close();
        }catch (SQLException e){
            log.error(e.getMessage());
            response.setStatus(500);
        }
    }
}
