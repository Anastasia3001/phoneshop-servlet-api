package com.es.phoneshop.web;

import com.es.phoneshop.dao.ProductDao;
import com.es.phoneshop.dao.impl.ArrayListProductDao;
import com.es.phoneshop.enums.SortingField;
import com.es.phoneshop.enums.SortingType;
import com.es.phoneshop.model.BrowsingHistory;
import com.es.phoneshop.service.BrowsingHistoryService;
import com.es.phoneshop.service.impl.BrowsingHistoryServiceImpl;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class ProductListPageServlet extends HttpServlet {
    private ProductDao listProductDao;
    private BrowsingHistoryService browsingHistoryService;
    private static final String DESCRIPTION = "description";
    private static final String SORTING = "sorting";
    private static final String TYPE = "type";
    private static final String PRODUCTS = "products";
    private static final String RECENTLY_VIEWED_PRODUCTS = "recentlyViewedProducts";

    @Override
    public void init() {
        listProductDao = ArrayListProductDao.getInstance();
        browsingHistoryService = BrowsingHistoryServiceImpl.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String description = request.getParameter(DESCRIPTION);
        String sortingField = request.getParameter(SORTING);
        String sortingType = request.getParameter(TYPE);
        BrowsingHistory browsingHistory = browsingHistoryService.getBrowsingHistory(request);
        request.setAttribute(PRODUCTS, listProductDao.findProducts(description,
                sortingField != null ? SortingField.valueOf(sortingField) : null,
                sortingType != null ? SortingType.valueOf(sortingType) : null));
        request.setAttribute(RECENTLY_VIEWED_PRODUCTS, browsingHistory.getProducts());
        request.getRequestDispatcher("/WEB-INF/pages/productList.jsp").forward(request, response);
    }
}
