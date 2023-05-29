package com.es.phoneshop.dao.impl;

import com.es.phoneshop.FunctionalReadWriteLock;
import com.es.phoneshop.dao.OrderDao;
import com.es.phoneshop.dao.ProductDao;
import com.es.phoneshop.enums.SortingField;
import com.es.phoneshop.enums.SortingType;
import com.es.phoneshop.exception.OrderNotFoundException;
import com.es.phoneshop.exception.ProductNotFoundException;
import com.es.phoneshop.model.Order;
import com.es.phoneshop.model.Product;
import com.es.phoneshop.model.comparator.DescriptionAndPriceComparator;
import com.es.phoneshop.model.comparator.DescriptionComparator;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ArrayListOrderDao implements OrderDao {
    private List<Order> orders;
    private final AtomicLong id;
    private final FunctionalReadWriteLock lock;

    private ArrayListOrderDao() {
        orders = new ArrayList<>();
        id = new AtomicLong();
        lock = new FunctionalReadWriteLock();
    }

    public static ArrayListOrderDao getInstance() {
        return SingletonHolder.INSTANCE;
    }

    private static class SingletonHolder {
        private static final ArrayListOrderDao INSTANCE = new ArrayListOrderDao();
    }

    @Override
    public Order getOrder(Long id) {
        return lock.read(() -> {
            Optional.ofNullable(id)
                    .orElseThrow(() -> new IllegalArgumentException("Unable to find order with null id"));
            return orders.stream()
                    .filter(order -> order.getId().equals(id))
                    .findAny()
                    .orElseThrow(() -> new OrderNotFoundException("Order with id " + id + " not found"));
        });
    }

    @Override
    public void save(Order order) {
        lock.write(() -> {
            Optional.ofNullable(order)
                    .orElseThrow(() -> new IllegalArgumentException("Order equals null"));
            order.setId(id.incrementAndGet());
            orders.add(order);
        });
    }
}
