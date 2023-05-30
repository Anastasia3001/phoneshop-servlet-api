package com.es.phoneshop.web;

import com.es.phoneshop.dao.OrderDao;
import com.es.phoneshop.dao.impl.ArrayListOrderDao;
import com.es.phoneshop.enums.PaymentMethod;
import com.es.phoneshop.model.Cart;
import com.es.phoneshop.model.Order;
import com.es.phoneshop.service.CartService;
import com.es.phoneshop.service.impl.CartServiceImpl;
import com.es.phoneshop.service.impl.OrderServiceImpl;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

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

    private Long getOrderIdFromUrl(HttpServletRequest request) {
        return Long.parseLong(request.getPathInfo().substring(1));
    }
}
