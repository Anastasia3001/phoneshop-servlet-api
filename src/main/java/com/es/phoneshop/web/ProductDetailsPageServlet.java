package com.es.phoneshop.web;

import com.es.phoneshop.dao.ProductDao;
import com.es.phoneshop.dao.impl.ArrayListProductDao;
import com.es.phoneshop.exception.OutOfStockException;
import com.es.phoneshop.model.BrowsingHistory;
import com.es.phoneshop.model.Cart;
import com.es.phoneshop.service.BrowsingHistoryService;
import com.es.phoneshop.service.CartService;
import com.es.phoneshop.service.impl.BrowsingHistoryServiceImpl;
import com.es.phoneshop.service.impl.CartServiceImpl;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.text.NumberFormat;
import java.text.ParseException;

public class ProductDetailsPageServlet extends HttpServlet {
    private ProductDao listProductDao;
    private CartService cartService;
    private BrowsingHistoryService browsingHistoryService;
    private static final String PRODUCT = "product";
    private static final String QUANTITY = "quantity";
    private static final String ERROR = "error";
    private static final String MESSAGE = "message";
    private static final String RECENTLY_VIEWED_PRODUCTS = "recentlyViewedProducts";

    @Override
    public void init() {
        listProductDao = ArrayListProductDao.getInstance();
        cartService = CartServiceImpl.getInstance();
        browsingHistoryService = BrowsingHistoryServiceImpl.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Long productId = getProductIdFromUrl(request);
        BrowsingHistory browsingHistory = browsingHistoryService.getBrowsingHistory(request);
        request.setAttribute(PRODUCT, listProductDao.getProduct(productId));
        request.setAttribute(RECENTLY_VIEWED_PRODUCTS, browsingHistory.getProducts());
        request.getRequestDispatcher("/WEB-INF/pages/productDetails.jsp").forward(request, response);
        browsingHistoryService.add(browsingHistory, productId);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Long productId = getProductIdFromUrl(request);
        int quantity;
        try {
            NumberFormat format = NumberFormat.getInstance(request.getLocale());
            quantity = format.parse(request.getParameter(QUANTITY)).intValue();
        } catch (ParseException ex) {
            request.setAttribute(ERROR, "Not a number");
            doGet(request, response);
            return;
        }
        Cart cart = cartService.getCart(request);
        try {
            cartService.add(cart, productId, quantity);
        } catch (OutOfStockException e) {
            request.setAttribute(ERROR, "Not enough stock");
            doGet(request, response);
            return;
        }
        request.setAttribute(MESSAGE, "Added to cart successfully");
        response.sendRedirect(request.getContextPath() + "/products/" + productId + "?message=Added to cart successfully");
    }

    private Long getProductIdFromUrl(HttpServletRequest request) {
        return Long.parseLong(request.getPathInfo().substring(1));
    }
}
