package com.es.phoneshop.web;

import com.es.phoneshop.dao.PriceHistoryDao;
import com.es.phoneshop.dao.ProductDao;
import com.es.phoneshop.dao.impl.ArrayListPriceHistoryDao;
import com.es.phoneshop.dao.impl.ArrayListProductDao;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class PriceHistoryPageServlet extends HttpServlet {
    private PriceHistoryDao priceHistoryDao;
    private ProductDao productDao;
    private static final String PRICE_HISTORY = "priceHistory";
    private static final String PRODUCT = "product";
    private static final String PRICE_HISTORY_JSP = "/WEB-INF/pages/priceHistory.jsp";

    @Override
    public void init() {
        priceHistoryDao = ArrayListPriceHistoryDao.getInstance();
        productDao = ArrayListProductDao.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String productId = request.getPathInfo();
        request.setAttribute(PRICE_HISTORY, priceHistoryDao.getPriceHistoryOfProduct(Long.valueOf(productId.substring(1))));
        request.setAttribute(PRODUCT, productDao.getProduct(Long.valueOf(productId.substring(1))));
        request.getRequestDispatcher(PRICE_HISTORY_JSP).forward(request, response);
    }
}
