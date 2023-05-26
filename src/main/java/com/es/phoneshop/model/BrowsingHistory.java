package com.es.phoneshop.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class BrowsingHistory implements Serializable {
    private LinkedList<Product> products;
    private static final long serialVersionUID = 1112L;

    public BrowsingHistory() {
        products = new LinkedList<>();
    }

    public LinkedList<Product> getProducts() {
        return products;
    }

    public void setProducts(LinkedList<Product> products) {
        this.products = products;
    }
}
