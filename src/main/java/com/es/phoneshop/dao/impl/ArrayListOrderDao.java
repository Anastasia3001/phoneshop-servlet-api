package com.es.phoneshop.dao.impl;

import com.es.phoneshop.dao.GenericDao;
import com.es.phoneshop.dao.OrderDao;
import com.es.phoneshop.exception.OrderNotFoundException;
import com.es.phoneshop.model.Order;

import java.util.ArrayList;
import java.util.Optional;

public class ArrayListOrderDao extends GenericDao<Order> implements OrderDao {
    private ArrayListOrderDao() {
        id = 0L;
        items = new ArrayList<>();
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
            return items.stream()
                    .filter(order -> order.getId().equals(id))
                    .findAny()
                    .orElseThrow(() -> new OrderNotFoundException("Order with id " + id + " not found"));
        });
    }

    @Override
    public Order getOrderBySecureId(String secureId) {
        return lock.read(() -> {
            Optional.ofNullable(secureId)
                    .orElseThrow(() -> new IllegalArgumentException("Unable to find order with null id"));
            return items.stream()
                    .filter(order -> order.getSecureId().equals(secureId))
                    .findAny()
                    .orElseThrow(() -> new OrderNotFoundException("Order with id " + secureId + " not found"));
        });
    }

    @Override
    public void save(Order order) {
        lock.write(() -> {
            Optional.ofNullable(order)
                    .orElseThrow(() -> new IllegalArgumentException("Order equals null"));
            order.setId(id++);
            items.add(order);
        });
    }
}
