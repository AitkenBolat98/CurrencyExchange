package Servlets;

import Service.CurrenciesService;
import Service.CurrencyService;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet(name = "CurrencyServlet", value = "/CurrencyServlet")
public class CurrencyServlet extends HttpServlet {
    private CurrencyService currencyService = new CurrencyService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            currencyService.getCurrency(request,response);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
