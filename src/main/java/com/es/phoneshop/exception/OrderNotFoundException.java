package com.es.phoneshop.exception;

import java.util.NoSuchElementException;

public class OrderNotFoundException extends NoSuchElementException {
    private String message;

    public OrderNotFoundException(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
