package com.es.phoneshop.service;

import com.es.phoneshop.model.BrowsingHistory;
import jakarta.servlet.http.HttpServletRequest;

public interface BrowsingHistoryService {
    void add(BrowsingHistory browsingHistory, Long productId);

    BrowsingHistory getBrowsingHistory(HttpServletRequest request);
}
