package com.es.phoneshop.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Currency;

public class PriceHistory implements Serializable {
    private Long id;
    private LocalDate date;
    private BigDecimal price;
    private Currency currency;
    private Product product;

    public PriceHistory() {
    }

    public PriceHistory(LocalDate date, BigDecimal price, Currency currency, Product product) {
        this.date = date;
        this.price = price;
        this.currency = currency;
        this.product = product;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}
