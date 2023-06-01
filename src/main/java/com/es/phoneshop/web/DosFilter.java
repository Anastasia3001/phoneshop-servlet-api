package com.es.phoneshop.web;

import com.es.phoneshop.service.security.DosProtectionService;
import com.es.phoneshop.service.security.impl.DosProtectionServiceImpl;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class DosFilter implements Filter {
    private DosProtectionService dosProtectionService;

    @Override
    public void init(FilterConfig filterConfig) {
        dosProtectionService = DosProtectionServiceImpl.getInstance();
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (dosProtectionService.isAllowed(request.getRemoteAddr())) {
            filterChain.doFilter(request, response);
        }
        else {
            ((HttpServletResponse)response).setStatus(429);
        }
    }

    @Override
    public void destroy() {

    }
}
