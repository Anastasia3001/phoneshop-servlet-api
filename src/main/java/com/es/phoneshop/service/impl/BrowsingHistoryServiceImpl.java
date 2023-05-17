package com.es.phoneshop.service.impl;

import com.es.phoneshop.FunctionalReadWriteLock;
import com.es.phoneshop.dao.ProductDao;
import com.es.phoneshop.dao.impl.ArrayListProductDao;
import com.es.phoneshop.exception.ProductNotFoundException;
import com.es.phoneshop.model.BrowsingHistory;
import com.es.phoneshop.model.Product;
import com.es.phoneshop.service.BrowsingHistoryService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import java.util.Optional;

public class BrowsingHistoryServiceImpl implements BrowsingHistoryService {
    private ProductDao productDao;
    private final FunctionalReadWriteLock lock;
    private static final String SEPARATE_BROWSING_HISTORY_SESSION_ATTRIBUTE = BrowsingHistoryServiceImpl.class.getName() + ".browsingHistory";
    private static final int SIZE_OF_RECENTLY_VIEWED_HISTORY = 3;

    public BrowsingHistoryServiceImpl() {
        productDao = ArrayListProductDao.getInstance();
        lock = new FunctionalReadWriteLock();
    }

    public static BrowsingHistoryServiceImpl getInstance() {
        return SingletonHolder.INSTANCE;
    }

    private static class SingletonHolder {
        private static final BrowsingHistoryServiceImpl INSTANCE = new BrowsingHistoryServiceImpl();
    }

    @Override
    public void add(BrowsingHistory browsingHistory, Long productId) {
        lock.write(() -> {
            Product product = productDao.getProduct(productId);
            Optional.ofNullable(product)
                    .orElseThrow(() -> new ProductNotFoundException("Product with id = " + productId + "not found"));
            if (browsingHistory.getProducts().size() == SIZE_OF_RECENTLY_VIEWED_HISTORY) {
                browsingHistory.getProducts().remove(0);
            }
            browsingHistory.getProducts().add(product);
        });
    }

    @Override
    public BrowsingHistory getBrowsingHistory(HttpServletRequest request) {
        HttpSession session = request.getSession();
        BrowsingHistory browsingHistory = (BrowsingHistory) session.getAttribute(SEPARATE_BROWSING_HISTORY_SESSION_ATTRIBUTE);
        if (!Optional.ofNullable(browsingHistory).isPresent()) {
            session.setAttribute(SEPARATE_BROWSING_HISTORY_SESSION_ATTRIBUTE, browsingHistory = new BrowsingHistory());
        }
        return browsingHistory;
    }
}
