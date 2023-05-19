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
        request.setAttribute(PRODUCT, listProductDao.getProduct(productId));
        request.setAttribute(RECENTLY_VIEWED_PRODUCTS, browsingHistoryService.getBrowsingHistory(request).getProducts());
        request.getRequestDispatcher("/WEB-INF/pages/productDetails.jsp").forward(request, response);
        BrowsingHistory browsingHistory = browsingHistoryService.getBrowsingHistory(request);
        browsingHistoryService.add(browsingHistory, productId);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Long productId = getProductIdFromUrl(request);
        int quantity;
        try {
            String quantityValue = request.getParameter(QUANTITY);
            if (!quantityValue.matches("^\\d+$")) {
                throw new NumberFormatException();
            }
            NumberFormat format = NumberFormat.getInstance(request.getLocale());
            quantity = format.parse(quantityValue).intValue();
            Cart cart = cartService.getCart(request);
            cartService.add(cart, productId, quantity);
        } catch (ParseException | NumberFormatException e) {
            request.setAttribute(ERROR, "Not a number");
            doGet(request, response);
            return;
        } catch (OutOfStockException e) {
            request.setAttribute(ERROR, e.getMessage());
            doGet(request, response);
            return;
        }
        response.sendRedirect(String.format("%s/products/%d?message=Added to card successfully", request.getContextPath(), productId));
    }

    private Long getProductIdFromUrl(HttpServletRequest request) {
        return Long.parseLong(request.getPathInfo().substring(1));
    }
}
