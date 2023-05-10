package com.es.phoneshop.dao;

import com.es.phoneshop.model.PriceHistory;

import java.util.List;

public interface PriceHistoryDao {
    List<PriceHistory> getPriceHistoryOfProduct(Long productId);
    void save (PriceHistory priceHistory);
}
