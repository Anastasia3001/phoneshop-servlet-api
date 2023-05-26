package com.es.phoneshop.service;

import com.es.phoneshop.model.Cart;
import jakarta.servlet.http.HttpServletRequest;

public interface CartService {
    void add(Cart cart, Long productId, int quantity);

    void update(Cart cart, Long productId, int quantity);

    Cart getCart(HttpServletRequest request);

    void delete(Cart cart, Long productId);
}
