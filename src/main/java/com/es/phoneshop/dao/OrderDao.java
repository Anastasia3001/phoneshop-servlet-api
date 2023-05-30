package com.es.phoneshop.dao;

import com.es.phoneshop.enums.SortingField;
import com.es.phoneshop.enums.SortingType;
import com.es.phoneshop.model.Order;
import com.es.phoneshop.model.Product;

import java.util.List;

public interface OrderDao {
    Order getOrder(Long id);
    Order getOrderBySecureId(String secureId);
    void save(Order order);
}
