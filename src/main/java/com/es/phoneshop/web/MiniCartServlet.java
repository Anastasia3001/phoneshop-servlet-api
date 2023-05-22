package com.es.phoneshop.web;

import com.es.phoneshop.exception.OutOfStockException;
import com.es.phoneshop.model.Cart;
import com.es.phoneshop.service.CartService;
import com.es.phoneshop.service.impl.CartServiceImpl;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

public class MiniCartServlet extends HttpServlet {
    private CartService cartService;
    private static final String CART = "cart";
    private static final String CART_JSP = "/WEB-INF/pages/miniCart.jsp";

    @Override
    public void init() {
        cartService = CartServiceImpl.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute(CART, cartService.getCart(request));
        request.getRequestDispatcher(CART_JSP).include(request, response);
    }
}
