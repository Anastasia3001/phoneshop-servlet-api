package com.es.phoneshop.security.impl;

import com.es.phoneshop.service.security.impl.DosProtectionServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Map;

import static org.junit.Assert.*;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DosProtectionServiceImplTest {
    @InjectMocks
    private DosProtectionServiceImpl dosProtectionService;
    @Mock
    private Map<String, Long> requestMap;
    private static final Long LARGE_COUNT = 30L;
    private static final String IP = "127.0.0.1";


    @Test
    public void isAllowedWithTrueResult() {
        when(requestMap.get(anyString())).thenReturn(null);

        boolean result = dosProtectionService.isAllowed(IP);

        assertTrue(result);
    }

    @Test
    public void isAllowedWithFalseResult() {
        when(requestMap.get(IP)).thenReturn(LARGE_COUNT);

        boolean result = dosProtectionService.isAllowed(IP);

        assertFalse(result);
    }
}
