package com.es.phoneshop.web;

import com.es.phoneshop.dao.ProductDao;
import com.es.phoneshop.dao.impl.ArrayListProductDao;
import com.es.phoneshop.enums.SearchingType;
import com.es.phoneshop.enums.SortingField;
import com.es.phoneshop.enums.SortingType;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.math.BigDecimal;

public class AdvancedSearchServlet extends HttpServlet {
    private ProductDao listProductDao;
    private static final String PRODUCTS = "products";
    private static final String ADVANCED_SEARCH_JSP = "/WEB-INF/pages/advancedSearch.jsp";
    private static final String DESCRIPTION = "description";
    private static final String MIN_PRICE = "minPrice";
    private static final String MAX_PRICE = "maxPrice";
    private static final String SEARCHING_TYPE = "searchingType";

    @Override
    public void init() {
        listProductDao = ArrayListProductDao.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String description = request.getParameter(DESCRIPTION);
        String minPrice = request.getParameter(MIN_PRICE);
        String maxPrice = request.getParameter(MAX_PRICE);
        String searchingType = request.getParameter(MAX_PRICE);
        request.setAttribute(PRODUCTS, listProductDao.findProductsByAdvancedSearching(description,
                minPrice != null ? BigDecimal.valueOf(Long.parseLong(minPrice)) : null,
                maxPrice != null ? BigDecimal.valueOf(Long.parseLong(maxPrice)) : null,
                searchingType != null ? SearchingType.valueOf(searchingType) : null));
        request.getRequestDispatcher(ADVANCED_SEARCH_JSP).forward(request, response);
    }
}
