package com.es.phoneshop.web;

import com.es.phoneshop.dao.OrderDao;
import com.es.phoneshop.dao.impl.ArrayListOrderDao;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class OrderOverviewPageServlet extends HttpServlet {
    private OrderDao orderDao;
    private static final String ORDER = "order";
    private static final String ORDER_OVERVIEW_JSP = "/WEB-INF/pages/orderOverview.jsp";

    @Override
    public void init() {
        orderDao = ArrayListOrderDao.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String secureId = request.getPathInfo().substring(1);
        request.setAttribute(ORDER, orderDao.getOrderBySecureId(secureId));
        request.getRequestDispatcher(ORDER_OVERVIEW_JSP).forward(request, response);
    }
}
