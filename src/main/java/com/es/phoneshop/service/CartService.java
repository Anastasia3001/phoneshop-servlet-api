package com.es.phoneshop.service;

import com.es.phoneshop.model.Cart;
import jakarta.servlet.http.HttpServletRequest;

public interface CartService {
    void add(Long productId, int quantity);
    Cart getCart();
}
