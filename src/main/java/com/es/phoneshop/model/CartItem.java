package com.es.phoneshop.model;

import java.io.Serializable;

public class CartItem implements Serializable, Cloneable {
    private Long id;
    private Product product;
    private int quantity;
    private static final long serialVersionUID = 1113L;

    public CartItem(Product product, int quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    public Long getId() {
        return id;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        CartItem cartItem = (CartItem) super.clone();
        cartItem.setProduct((Product) this.getProduct().clone());
        return cartItem;
    }
}
