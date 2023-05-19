package com.es.phoneshop.model;

import java.util.ArrayList;
import java.util.List;

public class Cart {
    private Long id;
    private List<CartItem> cartItems;

    public Cart() {
        this.cartItems = new ArrayList<>();
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<CartItem> getCartItems() {
        return cartItems;
    }

    public void setCartItems(List<CartItem> cartItems) {
        this.cartItems = cartItems;
    }
}
