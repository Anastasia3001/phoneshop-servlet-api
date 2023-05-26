package com.es.phoneshop.web;

import com.es.phoneshop.dao.ProductDao;
import com.es.phoneshop.dao.impl.ArrayListProductDao;
import com.es.phoneshop.enums.SortingField;
import com.es.phoneshop.enums.SortingType;
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
import java.util.HashMap;
import java.util.Map;

public class ProductListPageServlet extends HttpServlet {
    private ProductDao listProductDao;
    private BrowsingHistoryService browsingHistoryService;
    private CartService cartService;
    private static final String DESCRIPTION = "description";
    private static final String SORTING = "sorting";
    private static final String TYPE = "type";
    private static final String PRODUCTS = "products";
    private static final String RECENTLY_VIEWED_PRODUCTS = "recentlyViewedProducts";
    private static final String QUANTITY = "quantity";
    private static final String ERROR = "error";
    private static final String PRODUCT_LIST_JSP = "/WEB-INF/pages/productList.jsp";

    @Override
    public void init() {
        listProductDao = ArrayListProductDao.getInstance();
        browsingHistoryService = BrowsingHistoryServiceImpl.getInstance();
        cartService = CartServiceImpl.getInstance();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Map<Long, String> error = new HashMap<>();
        Long productId = getProductIdFromUrl(request);
        try {
            int quantity = validateQuantity(request);
            Cart cart = cartService.getCart(request);
            cartService.add(cart, productId, quantity);
        } catch (ParseException | NumberFormatException | OutOfStockException e) {
            error.put(productId, e.getMessage());
        }
        if (error.isEmpty()) {
            response.sendRedirect(String.format("%s/products/addCartItem/%d?message=Added to cart successfully", request.getContextPath(), productId));
        } else {
            request.setAttribute(ERROR, error);
            doGet(request, response);
        }
    }

    private int validateQuantity(HttpServletRequest request) throws ParseException {
        String quantityValue = request.getParameter(QUANTITY);
        if (!quantityValue.matches("^[1-9]|[1-9]\\d+$")) {
            throw new NumberFormatException("Not a number or quantity should be greater then 0");
        }
        NumberFormat format = NumberFormat.getInstance(request.getLocale());
        return format.parse(quantityValue).intValue();
    }

    private Long getProductIdFromUrl(HttpServletRequest request) {
        return Long.parseLong(request.getPathInfo().substring(1));
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String description = request.getParameter(DESCRIPTION);
        String sortingField = request.getParameter(SORTING);
        String sortingType = request.getParameter(TYPE);
        request.setAttribute(PRODUCTS, listProductDao.findProducts(description,
                sortingField != null ? SortingField.valueOf(sortingField) : null,
                sortingType != null ? SortingType.valueOf(sortingType) : null));
        BrowsingHistory browsingHistory = browsingHistoryService.getBrowsingHistory(request);
        request.setAttribute(RECENTLY_VIEWED_PRODUCTS, browsingHistory.getProducts());
        request.getRequestDispatcher(PRODUCT_LIST_JSP).forward(request, response);
    }
}
