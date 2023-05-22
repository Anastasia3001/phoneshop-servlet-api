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
import org.springframework.web.util.WebUtils;

import java.util.LinkedList;
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
            LinkedList<Product> products = browsingHistory.getProducts();
            if (products.contains(product)) {
                products.remove(product);
            }
            products.addFirst(product);
            if (products.size() > SIZE_OF_RECENTLY_VIEWED_HISTORY) {
                products.removeLast();
            }
        });
    }

    @Override
    public BrowsingHistory getBrowsingHistory(HttpServletRequest request) {
        BrowsingHistory browsingHistory = new BrowsingHistory();
        HttpSession session = request.getSession(false);
        if (session != null) {
            Object mutex = WebUtils.getSessionMutex(session);
            synchronized (mutex) {
                browsingHistory = (BrowsingHistory) session.getAttribute(SEPARATE_BROWSING_HISTORY_SESSION_ATTRIBUTE);
                if (Optional.ofNullable(browsingHistory).isEmpty()) {
                    session.setAttribute(SEPARATE_BROWSING_HISTORY_SESSION_ATTRIBUTE, browsingHistory = new BrowsingHistory());
                }
            }
        }
        return browsingHistory;
    }
}
