package com.es.phoneshop.dao.impl;

import com.es.phoneshop.dao.OrderDao;
import com.es.phoneshop.exception.OrderNotFoundException;
import com.es.phoneshop.model.Order;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ArrayListOrderDaoTest {
    @Mock
    private OrderDao orderDao;
    private static final Long ORDER_ID = 1L;
    private static final String ORDER_SECURE_ID = "1";

    @Before
    public void setup() {
    }

    @Test
    public void testGetOrder() {
        Order order = new Order();
        order.setId(ORDER_ID);
        when(orderDao.getOrder(anyLong())).thenReturn(order);

        Order result = orderDao.getOrder(ORDER_ID);

        Long expectedOrderId = 1L;
        assertEquals(expectedOrderId, result.getId());
    }

    @Test
    public void testGetOrderBySecureId() {
        Order order = new Order();
        order.setSecureId(ORDER_SECURE_ID);
        when(orderDao.getOrderBySecureId(anyString())).thenReturn(order);

        Order result = orderDao.getOrderBySecureId(ORDER_SECURE_ID);

        String expectedSecureOrderId = "1";
        assertEquals(expectedSecureOrderId, result.getSecureId());
    }
    @Test
    public void testGetOrderBySecureIdNotNull() {
        Order order = new Order();
        order.setSecureId(ORDER_SECURE_ID);
        when(orderDao.getOrderBySecureId(anyString())).thenReturn(order);

        Order result = orderDao.getOrderBySecureId(ORDER_SECURE_ID);

        assertNotNull(result);
    }

    @Test
    public void testSave() {
        Order order = new Order();
        order.setSecureId(ORDER_SECURE_ID);

        orderDao.save(order);

        verify(orderDao).save(order);
    }

    @Test(expected = OrderNotFoundException.class)
    public void testExceptionForNotFoundOrderBySecureId() {
        when(orderDao.getOrderBySecureId(anyString())).thenThrow(new OrderNotFoundException(""));

        orderDao.getOrderBySecureId(ORDER_SECURE_ID);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testExceptionForNullOrderParameter() {
        doThrow(new IllegalArgumentException()).when(orderDao).save(null);

        orderDao.save(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testExceptionForNullSecureIdParameter() {
        doThrow(new IllegalArgumentException()).when(orderDao).getOrderBySecureId(null);

        orderDao.getOrderBySecureId(null);
    }
}
