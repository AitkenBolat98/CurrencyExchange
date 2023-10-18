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


    public void getAllExchangeRates(HttpServletResponse response) throws SQLException, IOException {

        ArrayNode arrayNodeCurrencies = objectMapper.createArrayNode();
        ArrayNode arrayNodeRate = objectMapper.createArrayNode();
        PrintWriter pw = response.getWriter();

        try {

            Statement statement = getConnection().createStatement();
            ResultSet rsCurrencies = statement.executeQuery("SELECT c.* FROM Currencies c INNER JOIN exchangerates er" +
                    " ON c.id = er.basecurrencyid");

            while (rsCurrencies.next()) {
                ObjectNode rowObjectCurrencies = objectMapper.createObjectNode();
                rowObjectCurrencies.put("id", rsCurrencies.getInt("id"));
                rowObjectCurrencies.put("code", rsCurrencies.getString("code"));
                rowObjectCurrencies.put("fullname", rsCurrencies.getString("fullname"));
                rowObjectCurrencies.put("sign", rsCurrencies.getString("sign"));
                arrayNodeCurrencies.add(rowObjectCurrencies);
            }
            ResultSet rsRate = statement.executeQuery("SELECT er.rate FROM exchangerates er");
            if(rsRate.next()){
                ObjectNode rowObjectRate = objectMapper.createObjectNode();
                double rate = rsRate.getDouble("rate");
                rowObjectRate.put("rate",rate);
                arrayNodeRate.add(rowObjectRate);
            }
            response.setStatus(200);
            pw.print(arrayNodeCurrencies.toString());
            pw.print(arrayNodeRate.toString());
            statement.close();
            getConnection().close();
        }catch (SQLException e){
            log.error(e.getMessage());
            response.setStatus(500);
        }
    }

    public void getSpecificExchangeRate(HttpServletRequest request,HttpServletResponse response) throws IOException {

        PrintWriter pw = response.getWriter();
        ArrayNode arrayNode = objectMapper.createArrayNode();
        String[] url = request.getRequestURI().toString().split("/");

        String base = url[url.length-1].substring(0,3);
        String target = url[url.length-1].substring(3);

        if(base.length() != 3 || target.length() != 3){
            response.setStatus(400);
        }

        try {
            Statement statement = getConnection().createStatement();
            String sql = "SELECT bc.*, tc.*, er.rate " +
                    "FROM exchangerates er " +
                    "INNER JOIN currencies bc ON er.basecurrencyid = bc.id " +
                    "INNER JOIN currencies tc ON er.targetcurrencyid = tc.id " +
                    "WHERE bc.code = '" + base + "' AND tc.code = '" + target + "'";
            ResultSet rs = statement.executeQuery(sql);
            if (rs.next()){
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

                arrayNode.add(rowObject);
            }else {
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
