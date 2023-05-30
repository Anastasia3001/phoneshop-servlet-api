package com.es.phoneshop.web;

import com.es.phoneshop.security.DosProtectionService;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;

import jakarta.servlet.ServletException;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.IOException;

import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DosFilterTest {
    @Mock
    private DosProtectionService dosProtectionService;
    @Mock
    private ServletRequest request;
    @Mock
    private ServletResponse response;
    @Mock
    private FilterChain filterChain;
    @Mock
    private FilterConfig filterConfig;
    private DosFilter filter = new DosFilter();

    @Before
    public void setup() {
        filter.init(filterConfig);
    }

    @Test
    public void testDoFilter() throws ServletException, IOException {
        when(dosProtectionService.isAllowed(anyString())).thenReturn(true);

        filter.doFilter(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
    }

    @Test
    public void testDoFilterWithError() throws ServletException, IOException {
        when(dosProtectionService.isAllowed(anyString())).thenReturn(false);

        filter.doFilter(request, response, filterChain);

        verify((HttpServletResponse)response).setStatus(anyInt());
    }
}

