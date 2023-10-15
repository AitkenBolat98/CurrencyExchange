package Servlets;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

import Service.CurrenciesService;


@WebServlet(name = "Servlets.CurrenciesServlet", value = "/Servlets.CurrenciesServlet")
public class CurrenciesServlet extends HttpServlet {
    private CurrenciesService currenciesService = new CurrenciesService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        currenciesService.getAllCurrencies(response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        try {
            currenciesService.addNewCurrency(request,response);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
}