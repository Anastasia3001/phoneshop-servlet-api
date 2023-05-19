package com.es.phoneshop.model;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class BrowsingHistory {
    private LinkedList<Product> products;

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
