package com.es.phoneshop.service.impl;

import com.es.phoneshop.dao.ProductDao;
import com.es.phoneshop.model.BrowsingHistory;
import com.es.phoneshop.model.Product;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.LinkedList;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class BrowsingHistoryServiceImplTest {
    @Mock
    private ProductDao productDao;
    @Mock
    private HttpSession session;
    @InjectMocks
    private BrowsingHistoryServiceImpl browsingHistoryService;
    private BrowsingHistory browsingHistory;
    @Mock
    private HttpServletRequest request;
    @Before
    public void setup() {
        browsingHistory = new BrowsingHistory();
        Currency usd = Currency.getInstance("USD");
        LinkedList<Product> products = new LinkedList<>();
        products.add(new Product(1L, "sgs", "Samsung Galaxy S", new BigDecimal(100), usd, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg"));
        products.add(new Product(2L,"sgs3", "Samsung Galaxy S III", new BigDecimal(300), usd, 5, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S%20III.jpg"));
        products.add(new Product(3L,"sec901", "Sony Ericsson C901", new BigDecimal(420), usd, 30, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Sony/Sony%20Ericsson%20C901.jpg"));
        browsingHistory.setProducts(products);
    }

    @Test
    public void testAddProductInBrowsingHistory() {
        Currency usd = Currency.getInstance("USD");
        Product product = new Product("sgs", "Samsung Galaxy S", new BigDecimal(100), usd, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg");
        product.setId(1L);
        when(productDao.getProduct(anyLong())).thenReturn(product);

        browsingHistoryService.add(browsingHistory, 1L);

        int expectedSizeOfBrowsingHistory = 3;
        assertEquals(product, browsingHistory.getProducts().getFirst());
        assertEquals(expectedSizeOfBrowsingHistory, browsingHistory.getProducts().size());
    }

    @Test
    public void testGetBrowsingHistoryIfNotExist() {
        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute(anyString())).thenReturn(null);

        browsingHistoryService.getBrowsingHistory(request);

        verify(session).setAttribute(anyString(), any());
    }

    @Test
    public void testGetBrowsingHistoryIfExist() {
        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute(anyString())).thenReturn(browsingHistory);

        browsingHistoryService.getBrowsingHistory(request);

        verify(session, times(0)).setAttribute(anyString(), any());
    }

}