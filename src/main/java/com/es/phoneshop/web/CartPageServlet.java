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

public class CartPageServlet extends HttpServlet {
    private CartService cartService;
    private static final String CART_ITEMS = "cartItems";
    private static final String PRODUCT_ID = "productId";
    private static final String QUANTITY = "quantity";
    private static final String ERROR = "error";

    @Override
    public void init() {
        cartService = CartServiceImpl.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute(CART_ITEMS, cartService.getCart(request).getCartItems());
        request.getRequestDispatcher("/WEB-INF/pages/cart.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String[] productIds = request.getParameterValues(PRODUCT_ID);
        String[] quantities = request.getParameterValues(QUANTITY);
        Map<Long, String> errors = new HashMap<>();
        for (int i = 0; i < productIds.length; i++) {
            Long productId = Long.valueOf(productIds[i]);
            try {
                int quantity = validateQuantity(request, quantities[i]);
                Cart cart = cartService.getCart(request);
                cartService.update(cart, productId, quantity);
            } catch (ParseException | NumberFormatException | OutOfStockException e) {
                errors.put(productId, e.getMessage());
                request.setAttribute(ERROR, e.getMessage());
            }
        }
        if (errors.isEmpty()) {
            response.sendRedirect(String.format("%s/cart?message=Cart updated successfully", request.getContextPath()));
        }
        else {
            request.setAttribute("errors", errors);
            doGet(request, response);
        }
    }

    private int validateQuantity(HttpServletRequest request, String quantityValue) throws ParseException {
        if (!quantityValue.matches("^\\d+$")) {
            throw new NumberFormatException("Not a number");
        }
        NumberFormat format = NumberFormat.getInstance(request.getLocale());
        return format.parse(quantityValue).intValue();
    }
}
