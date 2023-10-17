package Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ExchangeRateService extends SQLConnection{

    ObjectMapper objectMapper = new ObjectMapper();


    public void getExchangeRates(HttpServletRequest request, HttpServletResponse response) throws SQLException {

        ArrayNode arrayNode = objectMapper.createArrayNode();

        Statement statement = getConnection().createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM exchangerates");


        while (resultSet.next()){

        }
    }
}
