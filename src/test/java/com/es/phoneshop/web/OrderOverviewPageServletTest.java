package com.es.phoneshop.web;

import com.es.phoneshop.dao.OrderDao;
import com.es.phoneshop.dao.impl.ArrayListOrderDao;
import com.es.phoneshop.model.Order;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.IOException;
import static org.mockito.Mockito.*;

@RunWith(PowerMockRunner.class)
@PrepareForTest(ArrayListOrderDao.class)
public class OrderOverviewPageServletTest {
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private RequestDispatcher requestDispatcher;
    private OrderOverviewPageServlet servlet = new OrderOverviewPageServlet();
    private static final String ORDER_SECURE_ID_FROM_URL = "/1";
    private static final String SECURE_ID = "1";

    @Test
    public void testDoGet() throws ServletException, IOException {
        OrderDao orderDao = PowerMockito.mock(ArrayListOrderDao.class);
        Order order = new Order();
        order.setSecureId(SECURE_ID);
        when(request.getPathInfo()).thenReturn(ORDER_SECURE_ID_FROM_URL);
        when(orderDao.getOrderBySecureId(anyString())).thenReturn(order);

        servlet.doGet(request, response);

        verify(requestDispatcher).forward(request, response);
    }
}