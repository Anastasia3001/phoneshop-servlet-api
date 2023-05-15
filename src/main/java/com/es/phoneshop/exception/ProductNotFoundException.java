package com.es.phoneshop.exception;

import java.util.NoSuchElementException;

public class ProductNotFoundException extends NoSuchElementException {
    private String message;

    public ProductNotFoundException(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
