package Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.extern.java.Log;
import lombok.extern.log4j.Log4j2;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
@Log4j2
public class ExchangeService extends Config{
    private ObjectMapper objectMapper = new ObjectMapper();

    public void getExchangeAmount(HttpServletRequest request, HttpServletResponse response) throws IOException {
        PrintWriter pw = response.getWriter();
        ArrayNode arrayNode = objectMapper.createArrayNode();

        String[] uri = request.getRequestURI().toString().split("\\?");
        String  keyValuePairs = uri[uri.length-1];


        String[] formData = keyValuePairs.split("&");
        ArrayList<String> values = new ArrayList<String>();

        for (String formDataPair : formData) {
            String[] pair = formDataPair.split("=");
            if (pair.length == 2) {
                String value = pair[1];
                values.add(value);
            }else {
                response.setStatus(400);
            }
        }
        try {
            Statement statement = getConnection().createStatement();
            String sql = "SELECT bc.*, tc.*, er.rate " +
                    "FROM exchangerates er " +
                    "INNER JOIN currencies bc ON er.basecurrencyid = bc.id " +
                    "INNER JOIN currencies tc ON er.targetcurrencyid = tc.id " +
                    "WHERE bc.code = '" + values.get(0) + "' AND tc.code = '" + values.get(1) + "'";
            ResultSet rs = statement.executeQuery(sql);
            if (rs.next()) {
                ObjectNode rowObject = objectMapper.createObjectNode();

                rowObject.put("basecurrency_id", rs.getInt("id"));
                rowObject.put("basecurrency_code", rs.getString("code"));
                rowObject.put("basecurrency_fullname", rs.getString("fullname"));
                rowObject.put("basecurrency_sign", rs.getString("sign"));
                rowObject.put("targetcurrency_id", rs.getInt("id"));
                rowObject.put("targetcurrency_code", rs.getString("code"));
                rowObject.put("targetcurrency_fullname", rs.getString("fullname"));
                rowObject.put("targetcurrency_sign", rs.getString("sign"));
                rowObject.put("rate", rs.getDouble("rate"));
                rowObject.put("amount",values.get(2));
                double rate = Double.parseDouble(rs.getString("rate"));
                double value = Double.parseDouble(values.get(2));
                rowObject.put("convertedAmount",value * rate);
                arrayNode.add(rowObject);
            } else {
                response.setStatus(400);
            }
            pw.print(arrayNode.toString());
            response.setStatus(200);
            statement.close();
            getConnection().close();
        } catch (SQLException e) {
            response.setStatus(500);
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }

    }
}
