package com.es.phoneshop.service.impl;

import com.es.phoneshop.dao.ProductDao;
import com.es.phoneshop.exception.OutOfStockException;
import com.es.phoneshop.model.Cart;
import com.es.phoneshop.model.CartItem;
import com.es.phoneshop.model.Product;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class CartServiceImplTest {
    @Mock
    private ProductDao productDao;
    @Mock
    private HttpSession session;
    @InjectMocks
    private CartServiceImpl cartService;
    @Mock
    private HttpServletRequest request;
    private Cart cart;
    @Before
    public void setup() {
        cart = new Cart();
        Currency usd = Currency.getInstance("USD");
        List<CartItem> cartItems = new ArrayList<>();
        cartItems.add(new CartItem(new Product(1L, "sgs", "Samsung Galaxy S", new BigDecimal(100), usd, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg"), 1));
        cartItems.add(new CartItem(new Product(2L,"sgs3", "Samsung Galaxy S III", new BigDecimal(300), usd, 5, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S%20III.jpg"), 2));
        cartItems.add(new CartItem(new Product(3L,"sec901", "Sony Ericsson C901", new BigDecimal(420), usd, 30, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Sony/Sony%20Ericsson%20C901.jpg"), 1));
        cart.setCartItems(cartItems);
    }

    @Test
    public void testAddIfCartOfProductNotExist() {
        Currency usd = Currency.getInstance("USD");
        Product product = new Product("sgs", "Samsung Galaxy S", new BigDecimal(100), usd, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg");
        product.setId(3L);
        when(productDao.getProduct(anyLong())).thenReturn(product);

        cartService.add(cart, 3L, 1);

        int expectedQuantityOfCartItem = 2;
        assertEquals(expectedQuantityOfCartItem, cart.getCartItems().get(2).getQuantity());
    }

    @Test
    public void testAddIfCartOfProductExist() {
        Currency usd = Currency.getInstance("USD");
        Product product = new Product("sgs", "Samsung Galaxy S", new BigDecimal(100), usd, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg");
        product.setId(3L);
        when(productDao.getProduct(anyLong())).thenReturn(product);

        cartService.add(cart, 3L, 1);

        int expectedCountOfCartItems = 3;
        assertEquals(expectedCountOfCartItems, cart.getCartItems().size());
    }

    @Test(expected = OutOfStockException.class)
    public void testOutOfStockExceptionForStock() {
        Currency usd = Currency.getInstance("USD");
        Product product = new Product("sgs", "Samsung Galaxy S", new BigDecimal(100), usd, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg");
        product.setId(4L);
        when(productDao.getProduct(anyLong())).thenReturn(product);

        cartService.add(cart, 4L, 102);
    }

    @Test(expected = OutOfStockException.class)
    public void testOutOfStockExceptionForQuantity() {
        Currency usd = Currency.getInstance("USD");
        Product product = new Product(2L,"sgs3", "Samsung Galaxy S III", new BigDecimal(300), usd, 5, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S%20III.jpg");
        when(productDao.getProduct(anyLong())).thenReturn(product);

        cartService.add(cart, 2L, 4);
    }

    @Test
    public void testGetCartIfNotExist() {
        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute(anyString())).thenReturn(null);

        cartService.getCart(request);

        verify(session).setAttribute(anyString(), any());
    }

    @Test
    public void testGetCartIfExist() {
        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute(anyString())).thenReturn(cart);

        cartService.getCart(request);

        verify(session, times(0)).setAttribute(anyString(), any());
    }
}