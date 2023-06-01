package com.es.phoneshop.service.security.impl;

import com.es.phoneshop.service.security.DosProtectionService;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class DosProtectionServiceImpl implements DosProtectionService {
    private static final long COUNT_OF_REQUESTS = 20L;
    private static final long DURATION = 60000L;

    private class RequestCounter {
        private Long countOfRequests;
        private Long timeOfLastRequest;

        public RequestCounter(Long countOfRequests, Long timeOfLastRequest) {
            this.countOfRequests = countOfRequests;
            this.timeOfLastRequest = timeOfLastRequest;
        }

        public Long getCountOfRequests() {
            return countOfRequests;
        }

        public void setCountOfRequests(Long countOfRequests) {
            this.countOfRequests = countOfRequests;
        }

        public Long getTimeOfLastRequest() {
            return timeOfLastRequest;
        }

        public void setTimeOfLastRequest(Long timeOfLastRequest) {
            this.timeOfLastRequest = timeOfLastRequest;
        }
    }

    private static Map<String, RequestCounter> countMap = new ConcurrentHashMap<>();

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
        RequestCounter requestCounter = countMap.get(ip);
        if (Optional.ofNullable(requestCounter).isEmpty()) {
            countMap.put(ip, new RequestCounter(1L, System.currentTimeMillis()));
        } else {
            if (System.currentTimeMillis() - requestCounter.getTimeOfLastRequest() > DURATION) {
                requestCounter.setCountOfRequests(1L);
                requestCounter.setTimeOfLastRequest(System.currentTimeMillis());
            } else {
                if (requestCounter.getCountOfRequests() > COUNT_OF_REQUESTS) {
                    return false;
                }
                requestCounter.setCountOfRequests(requestCounter.getCountOfRequests() + 1L);
            }
        }
        return true;
    }
}
