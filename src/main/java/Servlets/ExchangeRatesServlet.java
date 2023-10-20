package Servlets;

import Service.ExchangeRateService;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;

@WebServlet(name = "ExchangeRatesServlet", value = "/ExchangeRatesServlet")
public class ExchangeRatesServlet extends HttpServlet {
    private ExchangeRateService service = new ExchangeRateService();
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        service.getSpecificExchangeRate(request,response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        service.addNewExchangeRate(request,response);
    }

}