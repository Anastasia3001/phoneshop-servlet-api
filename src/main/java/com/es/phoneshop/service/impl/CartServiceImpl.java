package com.es.phoneshop.service.impl;

import com.es.phoneshop.FunctionalReadWriteLock;
import com.es.phoneshop.dao.ProductDao;
import com.es.phoneshop.dao.impl.ArrayListProductDao;
import com.es.phoneshop.model.Cart;
import com.es.phoneshop.model.CartItem;
import com.es.phoneshop.model.Product;
import com.es.phoneshop.service.CartService;

public class CartServiceImpl implements CartService {
    private ProductDao productDao;
    private Cart cart;
    private final FunctionalReadWriteLock lock;

    private CartServiceImpl() {
        this.productDao = ArrayListProductDao.getInstance();
        cart = new Cart();
        lock = new FunctionalReadWriteLock();
    }

    public static CartServiceImpl getInstance() {
        return CartServiceImpl.SingletonHolder.INSTANCE;
    }

    private static class SingletonHolder {
        private static final CartServiceImpl INSTANCE = new CartServiceImpl();
    }

    @Override
    public void add(Long productId, int quantity) {
        lock.write(() -> {
            Product product = productDao.getProduct(productId);
            cart.getCartItems().add(new CartItem(product, quantity));
            product.setStock(product.getStock() - quantity);
        });
    }
}
