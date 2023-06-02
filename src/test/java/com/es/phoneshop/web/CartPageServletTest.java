package com.es.phoneshop.web;

import com.es.phoneshop.model.Cart;
import com.es.phoneshop.service.impl.CartServiceImpl;
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
import java.util.Locale;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class CartPageServletTest {
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private RequestDispatcher requestDispatcher;
    @Mock
    private CartServiceImpl cartService;
    private Locale locale;
    private CartPageServlet servlet = new CartPageServlet();

    @Before
    public void setup() {
        servlet.init();
        when(request.getRequestDispatcher(anyString())).thenReturn(requestDispatcher);
    }

    @Test
    public void testDoGet() throws ServletException, IOException {
        servlet.doGet(request, response);

        verify(request, times(2)).setAttribute(anyString(), any());
        verify(requestDispatcher).forward(request, response);
    }

    @Test
    public void testDoPost() throws ServletException, IOException {
        String[] productIds = { "1", "2"};
        String[] quantities = { "2", "3"};
        Cart cart = new Cart();
        cart.setId(1L);
        when(request.getParameterValues(anyString())).thenReturn(productIds);
        when(request.getParameterValues(anyString())).thenReturn(quantities);
        when(request.getLocale()).thenReturn(locale);
        when(cartService.getCart(any())).thenReturn(cart);

        servlet.doPost(request, response);

        verify(cartService).update(any(), anyLong(), anyInt());
        verify(response).sendRedirect(anyString());
    }
}