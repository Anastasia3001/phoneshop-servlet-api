package com.es.phoneshop.security.impl;

import com.es.phoneshop.security.DosProtectionService;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DosProtectionServiceImpl implements DosProtectionService {
    private static final long COUNT_OF_REQUESTS = 20;
    private static Map<String, Long> countMap = new ConcurrentHashMap<>();
    public DosProtectionServiceImpl() {
    }

    public static DosProtectionServiceImpl getInstance() {
        return DosProtectionServiceImpl.SingletonHolder.INSTANCE;
    }

    private static class SingletonHolder {
        private static final DosProtectionServiceImpl INSTANCE = new DosProtectionServiceImpl();
    }
    @Override
    public boolean isAllowed(String ip) {
        Long count = countMap.get(ip);
        if (count == null) {
            count = 1L;
        }
        else {
            if (count > COUNT_OF_REQUESTS) {
                return false;
            }
            count++;
        }
        countMap.put(ip, count);
        return true;
    }

    public static class MyTimerTask implements Runnable {
        @Override
        public void run() {
            countMap.clear();
        }
    }
}
