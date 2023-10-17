package Servlets;

import Service.ExchangeRateService;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet(name = "ExchangeRateServlet", value = "/ExchangeRateServlet")
public class ExchangeRateServlet extends HttpServlet {
    private ExchangeRateService service = new ExchangeRateService();
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            service.getExchangeRates(request,response);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}