package com.es.phoneshop.web;

import jakarta.servlet.ServletContextEvent;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class ProductDemodataServletContextListenerTest {
    @Mock
    private ProductDemodataServletContextListener servletContextListener;
    @Mock
    private ServletContextEvent servletContextEvent;

    @Test
    public void testContextInitialized() {
        servletContextListener.contextInitialized(servletContextEvent);

        verify(servletContextListener).contextInitialized(any());
    }

    @Test
    public void testContextDestroyed() {
        servletContextListener.contextDestroyed(servletContextEvent);

        verify(servletContextListener).contextDestroyed(any());
    }
}
