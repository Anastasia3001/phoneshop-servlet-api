package com.es.phoneshop.dao.impl;

import com.es.phoneshop.FunctionalReadWriteLock;
import com.es.phoneshop.dao.PriceHistoryDao;
import com.es.phoneshop.exception.ProductNotFoundException;
import com.es.phoneshop.model.PriceHistory;
import com.es.phoneshop.model.Product;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

public class ArrayListPriceHistoryDao implements PriceHistoryDao {
    private List<PriceHistory> priceHistories;
    private final AtomicLong id;
    private final FunctionalReadWriteLock lock;

    public ArrayListPriceHistoryDao() {
        priceHistories = new ArrayList<>();
        id = new AtomicLong();
        lock = new FunctionalReadWriteLock();
    }

    public static ArrayListPriceHistoryDao getInstance() {
        return ArrayListPriceHistoryDao.SingletonHolder.INSTANCE;
    }

    private static class SingletonHolder {
        private static final ArrayListPriceHistoryDao INSTANCE = new ArrayListPriceHistoryDao();
    }

    @Override
    public List<PriceHistory> getPriceHistoryOfProduct(Long productId) {
        return lock.read(() -> {
            if (productId == null) throw new IllegalArgumentException("Unable to find price history of product with null id");
            return priceHistories.stream()
                    .filter(priceHistory -> priceHistory.getProduct().getId().equals(productId))
                    .collect(Collectors.toList());
        });
    }

    @Override
    public void save(PriceHistory priceHistory) {
        lock.write(() -> {
            if (priceHistory == null) throw new IllegalArgumentException("Price history equals null");
            priceHistory.setId(id.incrementAndGet());
            priceHistories.add(priceHistory);
        });
    }
}
