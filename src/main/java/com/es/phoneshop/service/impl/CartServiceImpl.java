package com.es.phoneshop.service.impl;

import com.es.phoneshop.FunctionalReadWriteLock;
import com.es.phoneshop.dao.ProductDao;
import com.es.phoneshop.dao.impl.ArrayListProductDao;
import com.es.phoneshop.exception.OutOfStockException;
import com.es.phoneshop.exception.ProductNotFoundException;
import com.es.phoneshop.model.Cart;
import com.es.phoneshop.model.CartItem;
import com.es.phoneshop.model.Product;
import com.es.phoneshop.service.CartService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import java.util.Optional;

public class CartServiceImpl implements CartService {
    private ProductDao productDao;
    private final FunctionalReadWriteLock lock;
    private static final String SEPARATE_CARD_SESSION_ATTRIBUTE = CartServiceImpl.class.getName() + ".cart";

    private CartServiceImpl() {
        this.productDao = ArrayListProductDao.getInstance();
        lock = new FunctionalReadWriteLock();
    }

    public static CartServiceImpl getInstance() {
        return CartServiceImpl.SingletonHolder.INSTANCE;
    }

    private static class SingletonHolder {
        private static final CartServiceImpl INSTANCE = new CartServiceImpl();
    }

    @Override
    public void add(Cart cart, Long productId, int quantity) {
        lock.write(() -> {
            Product product = productDao.getProduct(productId);
            Optional.ofNullable(product)
                    .orElseThrow(() -> new ProductNotFoundException("Product with id = " + productId + "not found"));
            if (product.getStock() < quantity) {
                throw new OutOfStockException("Not enough stock");
            }
            Optional<CartItem> foundCartItem = cart.getCartItems().stream()
                    .filter(cartItem -> cartItem.getProduct().getId().equals(product.getId()))
                    .findFirst();
            if (foundCartItem.isPresent()) {
                foundCartItem.get().setQuantity(foundCartItem.get().getQuantity() + quantity);
            }
            else {
                cart.getCartItems().add(new CartItem(product, quantity));
            }
            product.setStock(product.getStock() - quantity);
        });
    }

    @Override
    public Cart getCart(HttpServletRequest request) {
        return lock.read(() -> {
            HttpSession session = request.getSession();
            Cart cart = (Cart) session.getAttribute(SEPARATE_CARD_SESSION_ATTRIBUTE);
            if (!Optional.ofNullable(cart).isPresent()) {
                session.setAttribute(SEPARATE_CARD_SESSION_ATTRIBUTE, cart = new Cart());
            }
            return cart;
        });
    }
}
