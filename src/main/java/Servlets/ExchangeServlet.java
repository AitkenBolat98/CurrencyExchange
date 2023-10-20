package Servlets;

import Service.ExchangeService;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;

@WebServlet(name = "ExchangeServlet", value = "/ExchangeServlet")
public class ExchangeServlet extends HttpServlet {
    private ExchangeService service = new ExchangeService();
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        service.getExchangeAmount(request,response);
    }


}