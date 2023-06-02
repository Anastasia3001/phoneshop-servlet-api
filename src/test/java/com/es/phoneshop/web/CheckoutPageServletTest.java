package com.es.phoneshop.web;

import com.es.phoneshop.model.Cart;
import com.es.phoneshop.model.Order;
import com.es.phoneshop.service.CartService;
import com.es.phoneshop.service.OrderService;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.IOException;

import static org.mockito.Matchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CheckoutPageServletTest {
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private RequestDispatcher requestDispatcher;
    @Mock
    private CartService cartService;
    @Mock
    private OrderService orderService;
    private CheckoutPageServlet servlet = new CheckoutPageServlet();
    private Cart cart = new Cart();
    private Order order = new Order();

    @Before
    public void setup() {
        when(request.getRequestDispatcher(anyString())).thenReturn(requestDispatcher);
    }

    @Test
    public void testDoGet() throws ServletException, IOException {
        when(cartService.getCart(request)).thenReturn(cart);

        servlet.doGet(request, response);

        verify(request).setAttribute(anyString(), any());
        verify(request).setAttribute(anyString(), anyList());
        verify(requestDispatcher).forward(request, response);
    }

    @Test
    public void testDoPost() throws ServletException, IOException {
        when(cartService.getCart(request)).thenReturn(cart);
        when(orderService.getOrder(any())).thenReturn(order);

        servlet.doPost(request, response);

        verify(response).sendRedirect(anyString());
    }
}
